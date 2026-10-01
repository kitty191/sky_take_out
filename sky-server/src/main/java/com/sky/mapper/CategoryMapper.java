package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {
    /**
     * 新增分类
     *
     * @param category
     */
    @AutoFill(value = OperationType.INSERT)
    void add(Category category);

    /**
     * 启用/禁用分类
     *
     * @param category
     */
    @AutoFill(value = OperationType.UPDATE)
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
    List<Category> getByType(Integer type);
}
