package com.ego.item.controller;

import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.Brand;
import com.ego.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /**
     * 新增品牌
     * @param brand
     * @param cids
     * @return
     */
    @PostMapping
    public ResponseEntity<Void>saveBrand(Brand brand,@RequestParam("cids") Long[] cids){
        try {
            brandService.saveBrand(brand,cids);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 修改品牌
     * @return
     */
    @PutMapping
    public ResponseEntity<Void>updateBrand(Brand brand,@RequestParam("cids")Long[] cids){
        try {
            brandService.updateBrand(brand,cids);
            return  ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 删除品牌
     * @param bid
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void>deleteBrand(@RequestParam("id")Long bid){
        try {
            brandService.deleteBrand(bid);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandListByCid(@PathVariable("cid") Long cid) {
        List<Brand> result = brandService.findListByCid(cid);
        return ResponseEntity.ok(result);
    }

    @GetMapping("list/ids")
    public ResponseEntity<List<Brand>> queryListByIds(@RequestParam("ids") List<Long> ids){
        return ResponseEntity.ok(brandService.findListByIds(ids));

    }

}
