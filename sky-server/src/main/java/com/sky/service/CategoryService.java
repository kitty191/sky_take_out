package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

public interface CategoryService {

    /**
     * 新增菜品/套餐分类
     *
     * @param category
     */
    void addCategory(Category category);

    /**
     * 启用/禁用分类
     *
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);


    /**
     * 分类分页查询
     *
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 修改分类
     */
    void update(CategoryDTO categoryDTO);

}
