package com.ego.item.controller;

import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.Promotion;
import com.ego.item.pojo.TypeEnum;
import com.ego.item.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/10/18 11:39
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 *
 * 促销商品
 */
@RestController
@RequestMapping("promotion")
public class SeckillGoodsCtrl {

    @Autowired
    private PromotionService promotionService;


    //促销商品分页
    @GetMapping("page")
    public ResponseEntity<PageResult<Promotion>> getSeckillGoodsByPage(
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "rows", defaultValue = "5") Integer pageSize,
            @RequestParam(value = "sortBy", required = false) String sortBy,
            @RequestParam(value = "descending", required = false) Boolean descending,
            @RequestParam(value = "key") String key) {
        try {
            PageResult<Promotion> result = promotionService.getPromotionByPage(pageNo, pageSize, sortBy, descending, key);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }


    /**
     * 查所有促销种类
     */
    @GetMapping("typeList")
    public ResponseEntity<List<String>> findCategoryByID() {
        List<String> typeList = Arrays.asList(
                TypeEnum.FULL_PROMOTION.msg(),
                TypeEnum.NO_PROMOTION.msg(),
                TypeEnum.SEND_PROMOTION.msg(),
                TypeEnum.SECKILL_PROMOTION.msg());
        return ResponseEntity.ok(typeList);
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePromotion(@RequestParam("id")Long id){
        try {
            promotionService.deletePromotion(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

}
