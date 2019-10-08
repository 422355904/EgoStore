package com.ego.search.web.controller;

import com.ego.common.pojo.PageResult;
import com.ego.search.pojo.Goods;
import com.ego.search.dto.SearchRequest;
import com.ego.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author TheKing
 * @Date 2019/9/29 17:13
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RestController
public class SearchCtrl {
    @Autowired
    private SearchService searchService;

    @PostMapping("page")
    public ResponseEntity<PageResult<Goods>> page(@RequestBody SearchRequest searchRequest) {
        PageResult<Goods> pageGoods = searchService.pageGoods(searchRequest);
        if (pageGoods==null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pageGoods);
    }
}
