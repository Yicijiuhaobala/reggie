package com.web.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.web.reggie.common.R;
import com.web.reggie.dto.DishDto;
import com.web.reggie.entity.Dish;
import com.web.reggie.entity.DishFlavor;
import com.web.reggie.service.CategoryService;
import com.web.reggie.service.DishFlavorService;
import com.web.reggie.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description 菜品管理
 * @Author ZhangAohui
 * @Date 2023-05-04 20:19
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/dish")
@Api(tags = "菜品管理API接口")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping("/")
    @ApiOperation("新增菜品")
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 查修菜品分页信息
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("page")
    @ApiOperation(value = "查询菜品分页信息")
    public R<Page> getDish(int page, int pageSize, String name){
        log.info("page={}, pageSize={}, name={}", page, pageSize, name);
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);
//        添加过滤条件
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(name!=null,Dish::getName, name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage, lambdaQueryWrapper);
//        复制属性 对象拷贝
        BeanUtils.copyProperties(dishPage, dishDtoPage,"records");
        List<Dish> dishList = dishPage.getRecords();
        List<DishDto> dishDtoList = null;
        dishDtoList = dishList.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            String categoryName =  categoryService.getById(item.getCategoryId()).getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询菜品信息和口味信息")
    public R<DishDto> getDish(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 更新菜品信息和口味信息
     * @param dishDto
     * @return
     */
    @PutMapping("/")
    @ApiOperation(value = "更新菜品信息和口味信息")
    public R<String> updateDish(@RequestBody DishDto dishDto){
        log.info("修改菜品");
        dishService.updateWithFlavor(dishDto);
        return R.success("修改菜品信息成功");
    }

//    /**
//     * 根据菜品id查询对应的菜数据
//     * @param dish
//     * @return
//     */
//    @GetMapping("/list")
//    @ApiOperation(value = "根据菜品id查询对应的菜数据")
//    public R<List<Dish>> getList(Dish dish){
//        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
////        根据CategoryId查询对应菜信息
//        lambdaQueryWrapper.eq(dish.getCategoryId()!=null, Dish::getCategoryId, dish.getCategoryId());
////        查询菜品为起售状态
//        lambdaQueryWrapper.eq(Dish::getStatus,1);
//        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> dishList = dishService.list(lambdaQueryWrapper);
//        return R.success(dishList);
//    }

    /**
     * 根据菜品id查询对应的菜数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    @ApiOperation(value = "根据菜品id查询对应的菜数据")
    public R<List<DishDto>> getList(Dish dish){
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
//        根据CategoryId查询对应菜信息
        lambdaQueryWrapper.eq(dish.getCategoryId()!=null, Dish::getCategoryId, dish.getCategoryId());
//        查询菜品为起售状态
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = dishService.list(lambdaQueryWrapper);

        List<DishDto> dishList1 = dishList.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            String categoryName =  categoryService.getById(item.getCategoryId()).getName();
            dishDto.setCategoryName(categoryName);
            Long id = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
            lambdaQueryWrapper1.eq(DishFlavor::getDishId, id);
            List<DishFlavor> list = dishFlavorService.list(lambdaQueryWrapper1);
            dishDto.setFlavors(list);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishList1);
    }

}
