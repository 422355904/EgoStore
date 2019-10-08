package com.ego.item.api;

import com.ego.item.pojo.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/10/6 12:17
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RequestMapping("category")
public interface CategoryApi {
    @GetMapping("list")
    public ResponseEntity<List<Category>> findCategoryByID(@RequestParam("pid") Long pid) ;

    @PostMapping
    public ResponseEntity<Void> saveCategory(@RequestBody Category category);

    @PutMapping
    public ResponseEntity<Void> updateCategoryById(@RequestParam("id") Long id, @RequestParam("name") String name) ;

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable("id")Long id);

    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryListByIds(@RequestParam("ids") List<Long> ids);
}
