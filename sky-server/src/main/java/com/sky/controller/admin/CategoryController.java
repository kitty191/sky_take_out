package com.sky.controller.admin;

import com.sky.entity.Category;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/category")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 新增菜品/套餐分类
     *
     * @param category
     * @return
     */
    @PostMapping
    @ApiOperation("新增分类")
    public Result addCategory(@RequestBody Category category) {
        log.info("新增分类：{}", category);
        categoryService.addCategory(category);
        return Result.success();
    }

    /**
     * 启用/禁用分类
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用/禁用分类")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("启用/禁用分类:{},{}", status, id);
        categoryService.startOrStop(status, id);
        return Result.success();
    }

}
