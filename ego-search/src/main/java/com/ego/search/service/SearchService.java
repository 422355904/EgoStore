package com.ego.search.service;

import com.ego.common.pojo.PageResult;

import com.ego.common.utils.NumberUtils;
import com.ego.item.pojo.Brand;
import com.ego.item.pojo.Category;
import com.ego.item.pojo.Sku;
import com.ego.item.pojo.SpuBo;
import com.ego.search.dto.SearchResponse;
import com.ego.search.pojo.Goods;
import com.ego.search.dto.SearchRequest;
import com.ego.search.repository.GoodsRepository;
import com.ego.search.web.client.BrandClient;
import com.ego.search.web.client.CategoryClient;
import com.ego.search.web.client.GoodsClient;
import com.ego.search.web.client.SpecClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.stats.InternalStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author TheKing
 * @Date 2019/9/29 17:08
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Service
@Slf4j
public class SearchService {

    @Autowired
    private GoodsClient goodsClient;
    @Autowired
    private BrandClient brandClient;
    @Autowired
    private SpecClient specClient;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private GoodsRepository goodsRepository;


    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private CategoryClient categoryClient;

    public Goods buildGoods(SpuBo spuBo) {
        Goods goods = new Goods();
        goods.setCid1(spuBo.getCid1());
        goods.setCid2(spuBo.getCid2());
        goods.setCid3(spuBo.getCid3());
        goods.setBrandId(spuBo.getBrandId());
        goods.setCreateTime(spuBo.getCreateTime());
        goods.setId(spuBo.getId());
        goods.setSubTitle(spuBo.getSubTitle());
        //所有需要被搜索的信息，包含标题，分类，品牌
        goods.setAll(spuBo.getTitle() + "  " + spuBo.getCategoryNames().replaceAll("/", " ") + "   " + spuBo.getBrandName());
        //设置skus
        List<Sku> skus = goodsClient.querySkusBySpuId(spuBo.getId()).getBody();
        List<Long> priceList = new ArrayList<>();
        List<Map<String, Object>> skuLists = new ArrayList<>();
        HashMap<String, Object> skuMap = new HashMap<>();
        for (Sku sku : skus) {
            //封装sku中的price
            priceList.add(sku.getPrice());
            skuMap.put("id", sku.getId());
            skuMap.put("title", sku.getTitle());
            skuMap.put("image", sku.getImages().split(",")[0]);
            skuMap.put("price", sku.getPrice());
            skuLists.add(skuMap);
        }
        try {
            //将 skus 转为Json
            String skusStr = objectMapper.writeValueAsString(skus);
            goods.setSkus(skusStr);
            goods.setPrice(priceList);

            //用于存储当前spu可搜索的规格参数
            Map<String, Object> specsMap = new HashMap<>();
            String specifications = goodsClient.querySpecificationBySpuId(spuBo.getId()).getBody();
            List<Map<String, Object>> specList = objectMapper.readValue(specifications, new TypeReference<List<Map<String, Object>>>() {
            });

            specList.forEach(spec -> {
                List<Map<String, Object>> params = (List<Map<String, Object>>) spec.get("params");
                params.forEach(param -> {
                    Boolean searchable = (Boolean) param.get("searchable");
                    if (searchable != null && searchable) {
                        //获取k 和  v
                        String k = (String) param.get("k");
                        //如果v是空的，就应该取options的值
                        if(param.get("v")!=null) {
                            Object v =  param.get("v");
                            //null的规格参数 就不用存到es中，免得聚合出问题
                            if (v != null) {
                                specsMap.put(k, v);
                            }
                        }
                        else {
                            Object v =  param.get("options");
                            specsMap.put(k, v);
                        }
                    }
                });
            });
            goods.setSpecs(specsMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return goods;
    }


    /**
     * 分页显示搜索查询商品
     *
     * @param searchRequest
     * @return
     */
    public PageResult<Goods> pageGoods(SearchRequest searchRequest) {
        String key = searchRequest.getKey();
        int page = searchRequest.getPage();
        int size = searchRequest.getSize();
        if (StringUtils.isBlank(key)) {
            return null;
        }
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //基础查询
        QueryBuilder basicQuery = buildBasicQueryWithFilter(searchRequest);
        //设置查询条件
        queryBuilder.withQuery(basicQuery);
        //设置指定字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "subTitle", "skus"}, null));
        //设置分页查询
        queryBuilder.withPageable(PageRequest.of(page - 1, searchRequest.getSize()));

        //执行查询
        Page<Goods> pageInfo = goodsRepository.search(queryBuilder.build());

        List<Category> categories = getCategroyList(basicQuery);

        List<Brand> brands = getBrandList(basicQuery);

        //查询第一个类别的过滤条件
        List<Map<String, Object>> specs = null;

        if (categories != null && categories.size() > 0) {
            specs = getSpecs(basicQuery, categories.get(0).getId());
        }

        return new SearchResponse(pageInfo.getTotalElements(), Long.valueOf(pageInfo.getTotalPages()), pageInfo.getContent(), categories, brands, specs);
    }

    private QueryBuilder buildBasicQueryWithFilter(SearchRequest searchRequest) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        //基本查询条件
        queryBuilder.must(QueryBuilders.matchQuery("all", searchRequest.getKey()).operator(Operator.AND));
        //过滤条件构造器
        BoolQueryBuilder filterQueryBuilder = QueryBuilders.boolQuery();
        //整理过滤条件
        Map<String, String> filter = searchRequest.getFilter();
        for (Map.Entry<String, String> entry : filter.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            String regex = "^(\\d+\\.?\\d*)-(\\d+\\.?\\d*)$";
            if (!"key".equals(key)) {
                if ("price".equals(key)) {
                    if (!value.contains("元以上")) {
                        String[] nums = StringUtils.substringBefore(value, "元").split("-");
                        filterQueryBuilder.must(QueryBuilders.rangeQuery(key).gte(Double.valueOf(nums[0]) * 100).lt(Double.valueOf(nums[1]) * 100));
                    } else {
                        String num = StringUtils.substringBefore(value, "元以上");
                        filterQueryBuilder.must(QueryBuilders.rangeQuery(key).gte(Double.valueOf(num) * 100));
                    }
                } else {
                    if (value.matches(regex)) {
                        Double[] nums = NumberUtils.searchNumber(value, regex);
                        //数值类型进行范围查询   lt:小于  gte:大于等于
                        filterQueryBuilder.must(QueryBuilders.rangeQuery("specs." + key).gte(nums[0]).lt(nums[1]));
                    } else {
                        //商品分类和品牌要特殊处理
                        if (key.equals("分类")) {
                            key = "cid3";
                        } else if (key.equals("品牌")) {
                            key = "brandId";
                        } else {
                            key = "specs." + key + ".keyword";
                        }
                        //字符串类型，进行term查询
                        filterQueryBuilder.must(QueryBuilders.termQuery(key, value));
                    }
                }
            } else {
                break;
            }
        }
        //添加过滤条件
        queryBuilder.filter(filterQueryBuilder);
        return queryBuilder;
    }

    /**
     * 聚合查询到对应的商品类别列表
     *
     * @param basicQuery
     * @return
     */
    private List<Category> getCategroyList(QueryBuilder basicQuery) {

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        queryBuilder.withQuery(basicQuery);
        //添加聚合条件
        queryBuilder.addAggregation(AggregationBuilders.terms("类别").field("cid3"));

        Map<String, Aggregation> aggMaps = elasticsearchTemplate.query(queryBuilder.build(), searchResponse -> searchResponse.getAggregations().asMap());

        LongTerms agg = (LongTerms) aggMaps.get("类别");

        List<Long> ids = agg.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());

        if (ids != null && ids.size() > 0) {
            //根据ids查询到对应的类别
            return categoryClient.queryListByIds(ids).getBody();
        }
        return null;
    }

    private List<Brand> getBrandList(QueryBuilder basicQuery) {

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        queryBuilder.withQuery(basicQuery);
        //添加聚合条件
        queryBuilder.addAggregation(AggregationBuilders.terms("品牌").field("brandId"));

        Map<String, Aggregation> aggMaps = elasticsearchTemplate.query(queryBuilder.build(), searchResponse -> searchResponse.getAggregations().asMap());

        LongTerms agg = (LongTerms) aggMaps.get("品牌");

        List<Long> ids = agg.getBuckets().stream().map(bucket -> bucket.getKeyAsNumber().longValue()).collect(Collectors.toList());


        if (ids != null && ids.size() > 0) {
            return brandClient.queryListByIds(ids).getBody();
        }

        return null;
    }

    /**
     * 根据类别id聚合查询到对应规格参数
     *
     * @param basicQuery
     * @param id
     * @return
     */
    private List<Map<String, Object>> getSpecs(QueryBuilder basicQuery, Long id) {

        List<Map<String, Object>> result = new ArrayList<>();

        try {
            //查询到规格参数json
            String json = specClient.querySpecByCid(id).getBody();
            //json-->对象
            List<Map<String, Object>> specs = objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>() {
            });


            //区分数字型/字符型规格参数
            Map<String, String> numSpecs = new HashMap<>();
            Set<String> strSpecs = new HashSet<>();
            specs.forEach(group -> {
                        List<Map<String, Object>> params = (List<Map<String, Object>>) group.get("params");

                        params.forEach(param -> {
                            Boolean searchable = (Boolean) param.get("searchable");
                            if (searchable != null && searchable) {
                                //判断是不是数字
                                Boolean numerical = (Boolean) param.get("numerical");
                                String k = (String) param.get("k");
                                if (numerical != null && numerical) {
                                    String unit = (String) param.get("unit");
                                    numSpecs.put(k, unit);
                                } else {
                                    strSpecs.add(k);
                                }
                            }
                        });
                    }
            );

            //查询到数字型对应的间隔值
            Map<String, Double> numInterval = getInterval(numSpecs, basicQuery);

            //分别聚合查询数字型/字符型
            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            queryBuilder.withQuery(basicQuery);
            //字符型聚合
            strSpecs.forEach(k -> {
                queryBuilder.addAggregation(AggregationBuilders.terms(k).field("specs." + k + ".keyword"));
            });
            //数字型聚合
            numSpecs.forEach((k, v) -> {
                if (numInterval.get(k) > 0) {
                    queryBuilder.addAggregation(AggregationBuilders.histogram(k).field("specs." + k).interval(numInterval.get(k)));
                }
            });

            //执行查询
            Map<String, Aggregation> aggregationMap = elasticsearchTemplate.query(queryBuilder.build(), searchResponse -> searchResponse.getAggregations().asMap());


            //分别解析数字型/字符型
            strSpecs.forEach(k -> {
                Map<String, Object> map = new HashMap<>();

                List<String> list = new ArrayList<>();
                map.put("k", k);

                try {
                    StringTerms aggregation = (StringTerms) aggregationMap.get(k);
                    aggregation.getBuckets().forEach(bucket -> list.add(bucket.getKeyAsString()));
                    map.put("option", list);
                    result.add(map);

                } catch (Exception e) {
                    log.debug("解析聚合结果{}出错", k);
                    e.printStackTrace();
                }


            });

            numSpecs.forEach((k, v) -> {
                Map<String, Object> map = new HashMap<>();
                List<String> list = new ArrayList<>();
                map.put("k", k);

                InternalHistogram agg = (InternalHistogram) aggregationMap.get(k);
                if (agg != null) {
                    agg.getBuckets().forEach(bucket -> {
                        String str = "";
                        Double begin = (Double) bucket.getKey();
                        Double end = begin + numInterval.get(k);

                        if (NumberUtils.isInt(begin) && NumberUtils.isInt(end)) {
                            str = begin + "-" + end;
                        } else {
                            str = NumberUtils.scale(begin, 1) + "-" + NumberUtils.scale(end, 1);
                        }
                        //str += numSpecs.get(k);
                        list.add(str);
                    });
                }


                map.put("option", list);
                map.put("unit", numSpecs.get(k));
                result.add(map);

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 计算数字型参数的间隔
     *
     * @param numSpecs
     * @param basicQuery
     * @return
     */
    private Map<String, Double> getInterval(Map<String, String> numSpecs, QueryBuilder basicQuery) {
        Map<String, Double> result = new HashMap<>();

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        queryBuilder.withQuery(basicQuery);


        numSpecs.forEach((k, v) -> {
            //添加stats聚合
            queryBuilder.addAggregation(AggregationBuilders.stats(k).field("specs." + k));
        });

        Map<String, Aggregation> aggregationMap = elasticsearchTemplate.query(queryBuilder.build(), searchResponse -> searchResponse.getAggregations().asMap());

        //解析每个参数的聚合数据,并且计算出每个参数的间隔
        numSpecs.forEach((k, v) -> {
            InternalStats stats = (InternalStats) aggregationMap.get(k);
            double interval = NumberUtils.getInterval(stats.getMin(), stats.getMax(), stats.getSum());
            result.put(k, interval);
        });
        return result;
    }
}
