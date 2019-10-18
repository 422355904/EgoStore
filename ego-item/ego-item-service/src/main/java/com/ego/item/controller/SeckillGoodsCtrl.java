package com.ego.item.controller;

import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.SpuBo;
import com.ego.item.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author TheKing
 * @Date 2019/10/18 11:39
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RestController
@RequestMapping("seckillgoods")
public class SeckillGoodsCtrl {

    @Autowired
    private SeckillGoodsService seckillGoodsService;

    //促销商品分页
    @GetMapping("seckillGoods/page")
    public ResponseEntity<PageResult<SpuBo>> getSeckillGoodsByPage(
            @RequestParam(value = "pageNo",defaultValue = "1") Integer pageNo,
            @RequestParam(value = "rows",defaultValue = "5") Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "descending", required = false) Boolean descending,
            @RequestParam(value = "key") String key,
            @RequestParam(value = "saleable", required = false) Boolean saleable) {
        try {
            PageResult<SpuBo> result = seckillGoodsService.getGoodsByPage(pageNo, pageSize, sortBy, descending, key,saleable);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
