package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavourMapper {

    /**
     * 添加菜品口味
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
     * 根据菜品查口味
     *
     * @param id
     * @return
     */
    List<DishFlavor> getDishFlavourById(Long id);
}
