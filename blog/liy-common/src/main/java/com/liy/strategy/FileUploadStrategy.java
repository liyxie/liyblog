package com.liy.strategy;

import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传策略
 */
public interface FileUploadStrategy {

    /**
     * 上传文件
     * @param file
     * @param suffix
     * @return
     */
    String fileUpload(MultipartFile file,String suffix);


    /**
     * 删除文件
     * @param fileName
     * @return
     */
    Boolean deleteFile(String ...fileName);
}
