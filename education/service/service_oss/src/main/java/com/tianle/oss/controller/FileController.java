package com.tianle.oss.controller;

import com.tianle.commonutils.R;
import com.tianle.oss.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: Tianle
 * @Date: 2021/11/23 16:25
 * @Description:
 */
@RestController
@CrossOrigin
@Api(tags = "文件管理")
@RequestMapping("/ossservice/file")
public class FileController {

    @Autowired
    private FileService service;

    @PostMapping("upload")
    @ApiOperation(value = "文件上传")
    public R upload(
            @ApiParam(name = "file", value ="文件", required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(name = "host", value = "文件上传路径", required = false)
            String host){
        System.out.println(host);
        String uploadUrl = service.upload(file,host);
        return R.ok().message("文件上传成功！").data("url", uploadUrl);
    }
}
