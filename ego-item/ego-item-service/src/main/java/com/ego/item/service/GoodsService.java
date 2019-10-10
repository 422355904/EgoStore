package com.ego.item.service;

import com.ego.common.pojo.PageResult;
import com.ego.item.mapper.*;
import com.ego.item.pojo.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author TheKing
 * @Date 2019/9/27 14:27
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Service
@Slf4j
public class GoodsService {

    @Autowired
    private SpuMapper spuMapper;
    @Autowired
    private BrandMapper brandMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SpuDetailMapper spuDetailMapper;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private StockMapper stockMapper;
    @Autowired
    private AmqpTemplate amqpTemplate;


    /**
     * 商品分页
     * @return
     */
    public PageResult<SpuBo> getGoodsByPage(Integer page, Integer rows,
                                            String sortBy, Boolean descending,
                                            String key, Boolean saleable) {
        //分页
        PageHelper.startPage(page,rows);
        //查询条件
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        //排序条件
        if (StringUtils.isNotBlank(sortBy)){
            example.setOrderByClause(sortBy+" "+(descending?"desc":"asc"));
        }
            //关键字条件
        if (StringUtils.isNotBlank(key)){
            criteria.andLike("title","%"+key+"%")
                    .orLike("subTitle","%"+key+"%");
        }
            //是否上架条件
        if (null!=saleable){
            criteria.andEqualTo("saleable",saleable);
        }
        //查询结果
        Page<Spu>pageInfo= (Page<Spu>)spuMapper.selectByExample(example);

        //追加结果
        List<SpuBo> spuBoList=pageInfo.stream().map(spu -> {
            //把 spu 变为 spuBo
            SpuBo spuBo = new SpuBo();
            //属性拷贝
            BeanUtils.copyProperties(spu, spuBo);
            //设置品牌名
            Long brandId = spu.getBrandId();
            Brand brand = brandMapper.selectByPrimaryKey(brandId);
            //设置spu品牌名字
            spuBo.setBrandName(brand.getName());
            //拼接种类字符串
            StringBuilder categoryNames=new StringBuilder("");
            List<Category> categoryList = categoryMapper.findCategoryByBid(brandId);
            for (Category category : categoryList) {
                categoryNames.append(category.getName()+"/");
            }
            categoryNames.deleteCharAt(categoryNames.lastIndexOf("/"));
            //设置spu分类名字
            spuBo.setCategoryNames(categoryNames.toString());
            return spuBo;
        }).collect(Collectors.toList());
        return new PageResult<SpuBo>(pageInfo.getTotal(),spuBoList);
    }

    //新增商品回显品牌
    public List<Brand> getGoodsBrand(Long cid) {
        return brandMapper.getGoodsBrandByCid(cid);
    }

    /**
     * 保存商品
     * @param spuBo
     */
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        Date newDate = new Date();
        //保存Spu
        spuBo.setCreateTime(newDate);
        spuBo.setLastUpdateTime(newDate);
        this.spuMapper.insert(spuBo);
        Long spuId = spuBo.getId();
        //保存SpuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuId);
        this.spuDetailMapper.insert(spuDetail);
        //保存Sku
        List<Sku> skus = spuBo.getSkus();
        for (Sku sku : skus) {
            sku.setSpuId(spuId);
            sku.setCreateTime(newDate);
            sku.setLastUpdateTime(newDate);
            skuMapper.insertSelective(sku);
            //保存库存
            Stock stock = sku.getStock();
            stock.setSkuId(spuId);
            stockMapper.insertSelective(stock);
        }
    }

    /**
     * 编辑商品回显数据
     * @param spuId
     * @return
     */
    public SpuBo updateGoods(Long spuId) {
        SpuBo spuBo=new SpuBo();
        //查询Spu，赋值给SpuBo
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        BeanUtils.copyProperties(spu,spuBo);
        //查询SpuDetail，赋值给SpuBo
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        spuBo.setSpuDetail(spuDetail);
        //查询Sku
        Example example=new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId",spuId);
        List<Sku> skuList = skuMapper.selectByExample(example);
        //查询Stock，赋值给SpuBo
        for (Sku sku : skuList) {
            Stock stock = stockMapper.selectByPrimaryKey(sku.getId());
            //给当前sku赋值stock
            sku.setStock(stock);
        }
        //skuList赋值给SpuBo
        spuBo.setSkus(skuList);
        return spuBo;
    }



    @Transactional
    public void deleteGoods(Long spuId) {
        //删除Spu
        spuMapper.deleteByPrimaryKey(spuId);
        //删除SpuDetail
        spuDetailMapper.deleteByPrimaryKey(spuId);
        //删除Sku
        Example example=new Example(Sku.class);
        example.createCriteria().andEqualTo("spuId",spuId);
        List<Sku> skuList = skuMapper.selectByExample(example);
        skuMapper.deleteByExample(example);
        //删除Stock
        for (Sku sku : skuList) {
           stockMapper.deleteByPrimaryKey(sku.getId());
        }
    }

    public List<Sku> findSkusBySpuId(Long spuId) {
        Sku sku = new Sku();
        sku.setSpuId(spuId);
        return skuMapper.select(sku);
    }

    public SpuDetail findSpuDetailBySpuId(Long spuId) {
        return spuDetailMapper.selectByPrimaryKey(spuId);
    }


    public SpuBo findSpuBoById(Long spuId) {

        SpuBo spuBO = new SpuBo();
        Spu spu = spuMapper.selectByPrimaryKey(spuId);
        BeanUtils.copyProperties(spu, spuBO);

        //spuDetail
        SpuDetail spuDetail = spuDetailMapper.selectByPrimaryKey(spuId);
        spuBO.setSpuDetail(spuDetail);

        //skus
        Sku sku = new Sku();
        sku.setSpuId(spuId);

        List<Sku> skus = skuMapper.select(sku);
        spuBO.setSkus(skus);

        //类名名字
        String categoryName="";
        List<Category> categoryList = categoryMapper.selectByIdList(Arrays.asList(spuBO.getCid1(), spuBO.getCid2(), spuBO.getCid3()));
        for (Category category : categoryList) {
            categoryName += category.getName() + "/";
        }
        spuBO.setCategoryNames(categoryName);

        spuBO.setBrandName(brandMapper.selectByPrimaryKey(spuBO.getBrandId()).getName());

        return spuBO;
    }


    /**
     * 修改商品
     */
    @Transactional
    public void update(SpuBo spuBo) {
        //1.修改spu
        spuBo.setLastUpdateTime(new Date());
        spuMapper.updateByPrimaryKeySelective(spuBo);
        //2.修改spu detail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetailMapper.updateByPrimaryKeySelective(spuDetail);
        //3.刪除以前的sku以及stock
        Sku skuExample = new Sku();
        skuExample.setSpuId(spuBo.getId());
        List<Sku> skuList = skuMapper.select(skuExample);
        skuList.forEach(s -> {
            skuMapper.deleteByPrimaryKey(s);
            stockMapper.deleteByPrimaryKey(s.getId());
        });

        //4.添加sku&stock
        if (spuBo.getSkus() != null && spuBo.getSkus().size() > 0) {
            spuBo.getSkus().forEach(sku -> {
                sku.setId(null);
                sku.setCreateTime(spuBo.getCreateTime());
                sku.setLastUpdateTime(spuBo.getCreateTime());
                sku.setSpuId(spuBo.getId());
                skuMapper.insertSelective(sku);

                sku.getStock().setSkuId(sku.getId());
                stockMapper.insertSelective(sku.getStock());
            });
        }

        //发消息到mq
        sendMessage("item.update", spuBo.getId());

    }

    /**
     * 使用RabbitMQ进行数据同步
     * @param type queue类型，便于topic适配
     * @param spuid
     */
    public void sendMessage(String type, Long spuid) {
        try {
            amqpTemplate.convertAndSend(type,spuid);
        } catch (AmqpException e) {
            log.error("{}商品消息发送异常，商品id：{}", type, spuid, e);
        }
    }
}
