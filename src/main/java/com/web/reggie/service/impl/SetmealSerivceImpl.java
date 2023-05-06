package com.web.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.web.reggie.common.CustomException;
import com.web.reggie.dto.DishDto;
import com.web.reggie.dto.SetmealDto;
import com.web.reggie.entity.Setmeal;
import com.web.reggie.entity.SetmealDish;
import com.web.reggie.mapper.SetmealMapper;
import com.web.reggie.service.SetmealDishService;
import com.web.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author ZhangAohui
 * @Date 2023-05-04 14:48
 * @Description:
 */
@Service
public class SetmealSerivceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐同时保存套餐和菜品的关系
     * @param setmealDto
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveWithDish(SetmealDto setmealDto) {
//        保存套餐的基本信息 操作setmeal
        this.save(setmealDto);
//        保存套餐对应的菜品信息 操作setmeal_dish
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }
    /**
     * 删除套餐，以及删除套餐相关联的菜品信息
     * @param ids
     */
    @Override
    public void removeWithDish(List<Long> ids) {
//        首先删除套餐，查询套餐是否可以删除
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId, ids);
        lambdaQueryWrapper.eq(Setmeal::getStatus, 1);
        int count = this.count(lambdaQueryWrapper);
        if(count>0){
//            不能删除套餐 删除的套餐中存在在售的
            throw new CustomException("删除的套餐在售，无法删除");
        }
//        可以删除套餐
        this.removeByIds(ids);
//        删除套餐相关的菜品关系表信息
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lambdaQueryWrapper1);
    }
}
