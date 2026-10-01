package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavourMapper {

    /**
     * 添加菜品口味
     * 不加 @AutoFill：dish_flavor 表没有公共字段，且切面只处理第 1 个参数，对 List 无法生效
     *
     * @param flavors
     */
    void insert(List<DishFlavor> flavors);


    /**
     * 根据有效ID删除菜品对应口味
     *
     * @param validId
     */
    void deleteById(List<Long> validId);


    /**
     * 根据菜品ID删除对应口味（修改时操作）
     *
     * @param dishId
     */
    void delete(Long dishId);


    /**
     * 根据菜品查口味
     *
     * @param id
     * @return
     */
    List<DishFlavor> getDishFlavourById(Long id);
}
