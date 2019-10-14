package com.ego.cart.web.controller;

import com.ego.cart.pojo.Cart;
import com.ego.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/10/12 16:24
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RestController
public class CartConroller {

    @Autowired
    private CartService cartService;

    /**
     * 登录状态下，添加商品到购物车
     * @param skuId
     * @param num
     * @return
     */
    @PostMapping
    public ResponseEntity<Void> addCart(@RequestParam("skuId")String skuId,@RequestParam("num")Integer num){
        cartService.addCart(skuId,num);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 登录状态下查询购物车
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Cart>> queryCartList(){
        List<Cart> carts=cartService.queryCartList();
        if (carts == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(carts);
    }

    /**
     * 修改购物项数量
     * @param skuId
     * @param num
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> updateNum(@RequestParam("skuId") String skuId,
                                          @RequestParam("num") Integer num) {
        this.cartService.updateNum(skuId, num);
        return ResponseEntity.ok().build();
    }
}
