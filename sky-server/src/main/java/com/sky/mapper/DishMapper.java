package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     *
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);


    /**
     * 新增菜品
     *
     * @param dish
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);


    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);


    /**
     * 获得停售的菜品
     *
     * @param ids
     * @return
     */
    List<Long> getValidStatusDishId(List<Long> ids);


    /**
     * 根据有效ID删除菜品
     *
     * @param validId
     */
    void deleteById(List<Long> validId);


    /**
     * 根据ID查询菜品
     *
     * @param id
     * @return
     */
    DishVO getById(Long id);


    /**
     * 修改菜品信息
     * 传实体 Dish 而不是 DishDTO：切面需要调用 setUpdateTime/setUpdateUser，
     * DishDTO 没有这两个字段，反射会失败
     *
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);
}
