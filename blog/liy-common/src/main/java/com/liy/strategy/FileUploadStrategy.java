package com.liy.strategy;

import com.liy.entity.SystemFileConfig;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传策略
 */
public interface FileUploadStrategy {

    /**
     * 初始化
     */
    void init();

    /**
     * 上传文件
     * @param file
     * @return
     */
    String fileUpload(MultipartFile file, String path);


    /**
     * 删除文件
     * @param fileName
     * @return
     */
    Boolean deleteFile(String ...fileName);
}
