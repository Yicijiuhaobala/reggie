package com.web.reggie.controller;

import com.web.reggie.common.R;
import com.web.reggie.entity.ShoppingCart;
import com.web.reggie.service.ShoppingCartService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author ZhangAohui
 * @Date 2023-05-07 21:59
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
@Api(tags = "购物车管理API接口")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("购物车数据： {}", shoppingCart);
//        设置用户id,指定当前时哪个用户的购物车数据
//        查询当前菜品或者套餐是否在购物车中
//        如果已经存在，就在原来的数量基础上加一
//        如果不存在，则添加到购物车上，数量默认为一
        return null;
    }
}
