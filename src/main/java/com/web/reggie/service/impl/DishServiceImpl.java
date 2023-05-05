package com.web.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.reggie.dto.DishDto;
import com.web.reggie.entity.Dish;
import com.web.reggie.entity.DishFlavor;
import com.web.reggie.mapper.DishMapper;
import com.web.reggie.service.DishFlavorService;
import com.web.reggie.service.DishService;
import com.web.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author ZhangAohui
 * @Date 2023-05-04 14:47
 * @Description:
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时保存对应得口味信息
     *
     * @param dishDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWithFlavor(DishDto dishDto) {
//        保存基本的菜品表
        this.save(dishDto);
//        获取菜品Id
        Long dishID = dishDto.getId();
        List<DishFlavor> dishFlavorList = dishDto.getFlavors();
        dishFlavorList = dishFlavorList.stream().map((item) -> {
            item.setDishId(dishID);
            return item;
        }).collect(Collectors.toList());
//        保存对应的菜品口味
        dishFlavorService.saveBatch(dishFlavorList);
    }

    /**
     * 根据id查询菜品信息和口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
//        查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
//        查询菜品口味信息，从dish_flavor表中查询
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);

        dishDto.setFlavors(dishFlavorList);
        return dishDto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWithFlavor(DishDto dishDto) {
//        更新基本的菜品信息表 -dish表
        this.updateById(dishDto);
//        清空数据库里菜品的口味信息 - dish_flavor表
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(lambdaQueryWrapper);

//        更新提交过来的菜品的口味信息 - dish_flavor表
        Long dishId = dishDto.getId();
        List<DishFlavor> dishFlavorList = dishDto.getFlavors();
        List<DishFlavor> dishFlavors = dishFlavorList.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishFlavors);
    }
}

