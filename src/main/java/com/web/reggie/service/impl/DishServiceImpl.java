package com.web.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.reggie.entity.Dish;
import com.web.reggie.mapper.DishMapper;
import com.web.reggie.service.DishService;
import com.web.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author ZhangAohui
 * @Date 2023-05-04 14:47
 * @Description:
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
