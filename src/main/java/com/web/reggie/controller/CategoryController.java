package com.web.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.web.reggie.common.R;
import com.web.reggie.entity.Category;
import com.web.reggie.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description 菜品分类
 * @Author ZhangAohui
 * @Date 2023-04-28 16:55
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/category")
@Api(tags = "Category菜品分类管理 API 接口")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品分类
     * @param category
     * @return
     */
    @PostMapping("/")
    @ApiOperation(value = "新增菜品或者套餐分类")
    public R<String> add(@RequestBody Category category){
        log.info("新增菜品");
        categoryService.save(category);
        return R.success("新增菜品成功");
    }

    /**
     * 获取菜品或者套餐分类
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "获取菜品或者套餐分类")
    public R<Page> getCategory(int page, int pageSize){
        Page<Category> pageInfo = new Page<>();
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo, lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @DeleteMapping("/")
    @ApiOperation(value = "删除分类，如果存在菜品或者套餐被关联，则无法删除分类")
    public R<String> deleteCategory(Long id){
        log.info("删除id为{}的套餐", id);
//        categoryService.removeById(id);
        categoryService.remove(id);
        return R.success("该分类删除成功");
    }

    /**
     * 根据id修改分类信息
     * @param category
     * @return
     */
    @PutMapping("/")
    @ApiOperation(value = "修改分类信息")
    public R<String> updateCategory(@RequestBody Category category){
        log.info("修改分类信息：{}", category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    @GetMapping("/list")
    @ApiOperation("获取菜品分类或者套餐分类的list")
    public R<List<Category>> getCategoryList(Category category){

        log.info("获取菜品分类或者套餐分类的list");
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(category.getType()!=null, Category::getType, category.getType());
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        return R.success(list);
    }
}
