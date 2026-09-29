package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
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

    Page<Category> query(CategoryPageQueryDTO categoryPageQueryDTO);


    /**
     * 根据id删除分类
     *
     * @param id
     */
    void deleteById(Long id);


    /**
     * 根据类型查询分类
     *
     * @param type
     * @return
     */
    Category getByType(Integer type);
}
