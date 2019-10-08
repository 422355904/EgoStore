package com.ego.item.controller;

import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.*;
import com.ego.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/9/27 14:17
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RestController
@RequestMapping("goods")
public class GoodsCtrl {

    @Autowired
    private GoodsService goodsService;

    //商品分页
    @GetMapping("spu/page")
    public ResponseEntity<PageResult<SpuBo>> getGoodsByPage(
            @RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(value = "rows",defaultValue = "5") Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "descending", required = false) Boolean descending,
            @RequestParam(value = "key") String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable) {
        try {
            PageResult<SpuBo> result = goodsService.getGoodsByPage(pageNo, pageSize, sortBy, descending, key,saleable);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    //新增商品回显品牌
    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> getGoodsBrand(@PathVariable("cid") Long cid) {
        try {
            List<Brand> brandList = goodsService.getGoodsBrand(cid);
            return ResponseEntity.ok(brandList);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    //新增商品
    @PostMapping
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo) {
        try {
            goodsService.saveGoods(spuBo);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuBo> updateGoods(@PathVariable("spuId") Long spuId) {
        try {
            SpuBo spuBo = goodsService.updateGoods(spuId);
            return ResponseEntity.ok(spuBo);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteGoods(@RequestParam("id") Long spuId) {
        try {
            goodsService.deleteGoods(spuId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("skus")
    public ResponseEntity<List<Sku>> querySkusBySpuId(@RequestParam(value = "spuId", required = false) Long spuId) {
        try {
            List<Sku> result = goodsService.findSkusBySpuId(spuId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping("spec")
    public ResponseEntity<String> querySpecificationBySpuId(@RequestParam("spuId")Long spuId){
        SpuDetail spuDetail = goodsService.findSpuDetailBySpuId(spuId);
        if(spuDetail==null)
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail.getSpecifications());
    }


}
