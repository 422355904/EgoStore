package com.ego.cart.service;

import com.ego.auth.pojo.UserInfo;
import com.ego.cart.pojo.Cart;
import com.ego.cart.web.client.GoodsClient;
import com.ego.cart.web.interceptor.LoginInterceptor;
import com.ego.common.utils.JsonUtils;
import com.ego.item.pojo.Sku;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author TheKing
 * @Date 2019/10/12 16:59
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Service
public class CartService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private GoodsClient goodsClient;

    private static final String KEY_PREFIX = "ego:cart:uid:";

    public void addCart(String skuId, Integer num) {
        //获取用户信息,被存入了当前线程域
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        //获取当前用户的购物车，就算购物车为空，carts也不会为空值null
        BoundHashOperations<String, Object, Object> carts = stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        //判断车中是否有购物项
        Cart cart  = new Cart();
        Boolean hasKey = carts.hasKey(skuId.toString());
        if (hasKey){//有购物项
            String cartJson = (String) carts.get(skuId.toString());
            cart = JsonUtils.parse(cartJson, Cart.class);
            // 修改购物车数量
            cart.setNum(num+cart.getNum());
        }else {//无购物项
            //先查询到Sku
            Sku sku =goodsClient.querySkuById((Long.valueOf(skuId))).getBody();
            //新增购物项
            addItem(sku,num,userInfo,skuId,cart);
            //将cart->json
            String json = JsonUtils.serialize(cart);
            //存入redis
            carts.put(skuId.toString(), json);
        }
        //TODO 存入Redis，这里Carts里存入太多购物信息，不好同步更改数据，考虑Redis存入结构
        carts.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }

    private void addItem(Sku sku,Integer num,UserInfo userInfo,String skuId,Cart cart) {
        cart.setNum(num);
        cart.setImage(sku.getImages().split(",")[0]);
        cart.setTitle(sku.getTitle());
        cart.setOwnSpec(sku.getOwnSpec());
        cart.setPrice(sku.getPrice());
        cart.setUserId(userInfo.getId());
        cart.setSkuId(Long.valueOf(skuId));
    }

    /**
     * 登录状态下，查询Redis中的购物项
     * @return
     */
    public List<Cart> queryCartList() {
        //获取用户信息,被存入了当前线程域
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        //获取当前用户的购物车，就算购物车为空，carts也不会为空值null
        BoundHashOperations<String, Object, Object> carts = stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        List<Object> cartList = carts.values();
        // 判断是否有数据
        if(CollectionUtils.isEmpty(cartList)){
            return null;
        }
        return cartList.stream().map(cartStr->JsonUtils.parse(cartStr.toString(),Cart.class)).collect(Collectors.toList());
    }

    public void updateNum(String skuId, Integer num) {
        //获取用户信息,被存入了当前线程域
        UserInfo userInfo = LoginInterceptor.getLoginUser();
        //获取当前用户的购物车，就算购物车为空，carts也不会为空值null
        BoundHashOperations<String, Object, Object> carts = stringRedisTemplate.boundHashOps(KEY_PREFIX + userInfo.getId());
        String cartJson = carts.get(skuId).toString();
        Cart cart = JsonUtils.parse(cartJson, Cart.class);
        cart.setNum(num);
        //重新存入Redis
        carts.put(cart.getSkuId().toString(), JsonUtils.serialize(cart));
    }

    public void deleteCart(Map map) {
        // TODO: 2019/10/15 删除Redis购物车
        map.get("skuIds");
        map.get("userId");
    }
}
