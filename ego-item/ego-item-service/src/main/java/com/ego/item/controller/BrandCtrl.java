package com.ego.item.controller;

import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.Brand;
import com.ego.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author TheKing
 * @Date 2019/9/24 22:32
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RestController
@RequestMapping("brand")
public class BrandCtrl {

    @Autowired
    private BrandService brandService;

    /**
     * 分页查询所有品牌
     * @param pageNo
     * @param pageSize
     * @param sortBy
     * @param descending
     * @param key
     * @return
     */
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>>getBrandByPage(
            @RequestParam Integer pageNo, @RequestParam Integer pageSize,
            @RequestParam String sortBy,@RequestParam Boolean descending,
            @RequestParam String key){
        PageResult<Brand> pageResult=brandService.getBrandByPage(pageNo,pageSize,sortBy,descending,key);
        if (pageResult == null || pageResult.getItems().size() == 0) {
            return ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok(pageResult);
    }

}
