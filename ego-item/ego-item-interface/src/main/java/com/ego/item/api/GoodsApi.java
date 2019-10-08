package com.ego.item.api;

import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.Brand;
import com.ego.item.pojo.Sku;
import com.ego.item.pojo.SpuBo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/9/29 16:13
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RequestMapping("goods")
public interface GoodsApi {

    //商品分页
    @GetMapping("/spu/page")
    public ResponseEntity<PageResult<SpuBo>> getGoodsByPage(
                    @RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(value = "rows",defaultValue = "5") Integer rows,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "descending", required = false) Boolean descending,
            @RequestParam(value = "key") String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable);

    //新增商品回显品牌
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> getGoodsBrand(@PathVariable("cid") Long cid);

    @GetMapping("skus")
    public ResponseEntity<List<Sku>> querySkusBySpuId(@RequestParam(value = "spuId", required = false) Long spuId);

    @GetMapping("spec")
    public ResponseEntity<String> querySpecificationBySpuId(@RequestParam("spuId")Long spuId);
}
