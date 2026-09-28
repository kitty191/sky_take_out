package com.sky.service;

import com.sky.entity.Category;

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
}
