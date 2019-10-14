package com.ego.goodsweb.web.controller;

import com.ego.goodsweb.service.GoodsHtmlService;
import com.ego.goodsweb.service.GoodsService;
import com.ego.item.pojo.SpuBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author TheKing
 * @Date 2019/10/8 21:26
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Controller
@RequestMapping("item")
public class GoodsController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private GoodsHtmlService goodsHtmlService;

    //定长线程池
    ExecutorService threadPool = Executors.newFixedThreadPool(10);

    /**
     * 跳转到商品详情页
     *
     * @param model
     * @param spuId
     * @return
     */
    @GetMapping("{spuId}.html")
    public String toItemPage(Model model, @PathVariable("spuId") Long spuId) {
        //加载数据
        Map<String, Object> modelMap = this.goodsService.loadModel(spuId);
        //把数据放入模型中
        model.addAllAttributes(modelMap);
        //生成静态页面
        threadPool.submit(() -> {
            // TODO 暂时关闭生成静态页面
            //goodsHtmlService.createHtml(spuId);
        });

        return "item";
    }

}
