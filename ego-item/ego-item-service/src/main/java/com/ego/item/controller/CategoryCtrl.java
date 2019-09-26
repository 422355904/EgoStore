package com.ego.item.controller;

import com.ego.item.pojo.Category;
import com.ego.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/9/24 15:15
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RestController
@RequestMapping("category")
public class CategoryCtrl {

    @Autowired
    private CategoryService categoryService;

    /**
     * 查所有种类父节点
     * @param id
     * @return
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> findCategoryByID(@RequestParam("pid") Long id){
        List<Category> categoryList= categoryService.findCategoryByPid(id);
        if (null==categoryList){
            return ResponseEntity.notFound().build();
        }else {
            return ResponseEntity.ok(categoryList);
        }
    }

    /**
     * 新增新种类
     * @param category
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> saveCategory(@RequestBody Category category){
        try {
            categoryService.saveCategory(category);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.noContent().build();
        }
    }

    /**
     * 更新种类名字
     * @param id
     * @param name
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateCategoryById(@RequestParam("id") Long id,@RequestParam("name") String name){
        try {
            categoryService.updateCategoryById(id,name);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 删除种类
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable("id") Long id){
        try {
            categoryService.deleteCategoryById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>>findCategoryByBid(@PathVariable("bid")Long bid){
        try {
            List<Category>categories=categoryService.findCategoryByBid(bid);
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }



}
