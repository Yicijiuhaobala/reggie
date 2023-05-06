package com.web.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.web.reggie.dto.DishDto;
import com.web.reggie.dto.SetmealDto;
import com.web.reggie.entity.Setmeal;

import java.util.List;

/**
 * @Description
 * @Author ZhangAohui
 * @Date 2023-05-04 14:46
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐同时保存套餐和菜品的关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐，以及删除套餐相关联的菜品信息
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
