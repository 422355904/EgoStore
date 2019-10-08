package com.ego.item.api;

import com.ego.item.pojo.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author TheKing
 * @Date 2019/10/6 12:35
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RequestMapping("spec")
public interface SpecApi {

    @GetMapping("{cid}")
    public ResponseEntity<String> querySpecByCid(@PathVariable("cid") Long cid);


    @PostMapping
    public ResponseEntity<Void> saveSpecification(Specification specification);
}
