package com.sky.controller.admin;

import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.UUID;


/**
 * 通用接口
 * 目前提供文件上传功能：接收前端上传的图片，转存到阿里云 OSS，并把可访问的 URL 返回给前端
 */
@RestController
@Slf4j
@RequestMapping("admin/common")
@Api(tags = "通用接口")
public class CommonController {

    private final AliOssUtil aliOssUtil;

    public CommonController(AliOssUtil aliOssUtil) {
        this.aliOssUtil = aliOssUtil;
    }


    /**
     * 文件上传
     * 前端以 multipart/form-data 形式提交文件，本方法把文件转存到 OSS，返回其访问地址
     *
     * @param file 前端提交过来的文件，参数名必须与前端约定的字段名保持一致
     * @return 上传成功返回 OSS 上的文件访问地址；失败返回统一错误提示
     */
    @ApiOperation("文件上传")
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传:{}", file);

        try {
            // 1、取得原始文件名，例如 cat.jpg
            String originalFilename = file.getOriginalFilename();

            // 2、截取扩展名（含点），例如 .jpg，用来保证上传后的文件类型不变
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // 3、用 UUID 生成不重复的新文件名，避免同名文件互相覆盖
            String ObjectName = UUID.randomUUID() + extension;

            // 4、把文件字节流交给 OSS 工具类上传，返回文件访问路径
            String filePath = aliOssUtil.upload(file.getBytes(), ObjectName);

            return Result.success(filePath);
        } catch (IOException e) {
            log.info("文件上传失败:{}",e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
