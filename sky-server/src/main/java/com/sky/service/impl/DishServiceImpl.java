package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavourMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    private final DishMapper dishMapper;

    private final DishFlavourMapper dishFlavourMapper;

    private final SetmealDishMapper setmealDishMapper;


    public DishServiceImpl(DishMapper dishMapper, DishFlavourMapper dishFlavourMapper, SetmealDishMapper setmealDishMapper) {
        this.dishMapper = dishMapper;
        this.dishFlavourMapper = dishFlavourMapper;
        this.setmealDishMapper = setmealDishMapper;
    }

    /**
     * 新增菜品
     */
    @Override
    @Transactional
    public void saveWithFlavour(DishDTO dishDTO) {

        /*
        插入菜品数据
         */
        Dish dish = new Dish();

        BeanUtils.copyProperties(dishDTO, dish);

        dishMapper.insert(dish);

        /*
        插入口味数据
         */
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();

        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });

            dishFlavourMapper.insert(flavors);
        }
    }


    /**
     * 菜品分页查询
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage()
                , dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);

        return new PageResult(page.getTotal(), page.getResult());
    }


    /**
     * 批量删除菜品
     *
     * @param ids
     */
    @Override
    @Transactional
    public void delete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        /*
        排除在售的菜品
         */
        List<Long> validStatusDish = dishMapper.getValidStatusDishId(ids);
        if (validStatusDish.size() < ids.size()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }

        /*
        排除套餐内的菜品
         */
        List<Long> validId = setmealDishMapper.getValidId(validStatusDish);
        if (validId.size() < validStatusDish.size()) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

        /*
        删除合法的菜品和对应的口味
         */
        dishMapper.deleteById(validId);
        dishFlavourMapper.deleteById(validId);
    }


    /**
     * 根据ID查询菜品
     *
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {
        DishVO dishVO = dishMapper.getById(id);

        if (dishVO == null) {
            return null;
        }

        dishVO.setFlavors(dishFlavourMapper.getDishFlavourById(id));

        return dishVO;
    }
}

















