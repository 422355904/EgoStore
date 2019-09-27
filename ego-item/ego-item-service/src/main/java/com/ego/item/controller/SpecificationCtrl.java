package com.ego.item.controller;

import com.ego.item.pojo.Specification;
import com.ego.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @Author TheKing
 * @Date 2019/9/27 13:50
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RestController
@RequestMapping("spec")
public class SpecificationCtrl {

    @Autowired
    private SpecificationService specService;

    /**
     * 根据种类查询模板
     * @param cid
     * @return
     */
    @GetMapping("{cid}")
    public ResponseEntity<String> queryByCid(@PathVariable("cid") Long cid) {
        try {
            Specification spec = specService.queryByCid(cid);
            return ResponseEntity.ok(spec.getSpecifications());
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * 新增种类规格模板
     * @param specification
     * @return
     */
    @PostMapping
    public ResponseEntity<Void>saveSpecification(Specification specification){
        try {
            specService.saveSpecification(specification);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }


}
