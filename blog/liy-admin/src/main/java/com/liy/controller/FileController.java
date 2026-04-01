package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.liy.annotation.OperationLogger;
import com.liy.common.ResponseResult;
import com.liy.service.FileMigrationService;
import com.liy.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("system/file")
@Tag(name = "图片上传-接口")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    private final FileMigrationService fileMigrationService;

    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    @Operation(summary = "上传文件")
    public ResponseResult upload(MultipartFile multipartFile){
        return fileService.upload(multipartFile);
    }

    @OperationLogger("删除文件")
    @SaCheckPermission("system:fileUpload:delete")
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    @Operation(summary = "批量删除文件")
    public ResponseResult delBatchFile(String key){
        return fileService.delBatchFile(key);
    }

    @OperationLogger("迁移文件URL（剥离域名）")
    @RequestMapping(value = "/migrate", method = RequestMethod.POST)
    @Operation(summary = "【一次性】迁移存量文件URL：将各表已存的完整URL剥离域名，只保留相对路径")
    public ResponseResult migrateFileUrls() {
        return fileMigrationService.migrateFileUrls();
    }
}
