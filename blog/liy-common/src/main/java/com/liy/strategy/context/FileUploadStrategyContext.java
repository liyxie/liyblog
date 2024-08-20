package com.liy.strategy.context;

import com.liy.config.FileConfig;
import com.liy.enums.file.FileUpdateModelEnum;
import com.liy.strategy.FileUploadStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Objects;


/**
 * @apiNote 文件上传策略上下文
 */
@Service
@RequiredArgsConstructor
public class FileUploadStrategyContext {

    private final Map<String, FileUploadStrategy> fileUploadStrategyMap;

    private final FileConfig fileConfig;

    /**
     * 指定文件上传类型
     */
    private FileUpdateModelEnum tempFileUpdateModelEnum;

    public void init(){
        fileUploadStrategyMap.entrySet().stream()
                .filter(Objects::nonNull)
                .forEach(e -> {
                    e.getValue().init();
                });
    }

    public FileUploadStrategyContext setFileUpdateModel(FileUpdateModelEnum fileUpdateModelEnum){
        this.tempFileUpdateModelEnum = fileUpdateModelEnum;
        return this;
    }

    public String executeFileUploadStrategy(MultipartFile file, String path) {
        return executeFileUploadStrategy(FileUpdateModelEnum.getEnumByType(fileConfig.getNowFileConfigId()), file, path);
    }
    /**
     * 执行文件上传策略
     *
     * @param file 文件对象
     * @return {@link String} 文件名
     */
    public String executeFileUploadStrategy(FileUpdateModelEnum fileUpdateModelEnum, MultipartFile file, String path) {
         return fileUploadStrategyMap.get(fileUpdateModelEnum.getStrategy()).fileUpload(file, path);
    }

    /**
     * 删除文件策略
     * @param key
     * @return
     */
    public Boolean executeDeleteFileStrategy(String ...key) {
        return fileUploadStrategyMap.get(FileUpdateModelEnum.getStrategyByType(fileConfig.getNowFileConfigId())).deleteFile(key);
    }
    /**
     * 删除文件策略
     * @param fileUploadMode
     * @param key
     * @return
     */
    public Boolean executeDeleteFileStrategy(String fileUploadMode,String ...key) {
         return fileUploadStrategyMap.get(fileUploadMode).deleteFile(key);
    }
}
