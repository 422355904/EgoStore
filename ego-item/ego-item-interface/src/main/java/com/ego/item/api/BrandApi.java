package com.ego.item.api;

import com.ego.common.pojo.PageResult;
import com.ego.item.pojo.Brand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/10/6 12:01
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RequestMapping("brand")
public interface BrandApi {
    @GetMapping("page")
    public ResponseEntity<PageResult<Brand>> page(
            @RequestParam(value = "pageNo",defaultValue = "1")Integer pageNo,
            @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize,
            @RequestParam(value = "sortBy")String sortBy,
            @RequestParam(value = "descending",defaultValue = "true")Boolean descending,
            @RequestParam(value = "key")String key
    );

    @PostMapping
    public ResponseEntity<Void> save(Brand brand, @RequestParam("cids") Long[] cids);

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandListByCid(@PathVariable("cid") Long cid) ;

    @GetMapping("list/ids")
    public ResponseEntity<List<Brand>> queryListByIds(@RequestParam("ids") List<Long> ids);
}
