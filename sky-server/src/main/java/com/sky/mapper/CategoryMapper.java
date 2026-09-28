package com.sky.mapper;

import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {
    /**
     * 新增分类
     *
     * @param category
     */
    void add(Category category);

    /**
     * 启用/禁用分类
     *
     * @param category
     */
    void update(Category category);
}
