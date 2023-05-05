package com.web.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.reggie.dto.DishDto;
import com.web.reggie.entity.Dish;
import com.web.reggie.entity.DishFlavor;

/**
 * @Description
 * @Author ZhangAohui
 * @Date 2023-05-04 14:45
 */
public interface DishService extends IService<Dish> {
//   新增菜品，同时插入菜品对应得口味数据，需要同时操作两张表 Dish DishFlavor
    public  void saveWithFlavor(DishDto dishDto);
    public DishDto getByIdWithFlavor(Long id);
    public void updateWithFlavor(DishDto dishDto);
}
