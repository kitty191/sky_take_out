package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    /**
     * 排除在套餐内的菜品
     *
     * @param validStatusDish
     * @return
     */
    List<Long> getValidId(List<Long> validStatusDish);
}
