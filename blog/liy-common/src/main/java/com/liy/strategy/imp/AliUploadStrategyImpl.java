package com.liy.strategy.imp;

import com.liy.entity.SystemConfig;
import com.liy.service.SystemConfigService;
import com.liy.strategy.FileUploadStrategy;
import com.liy.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.FileStorageServiceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.util.Collections;

@Service("aliUploadStrategyImpl")
@RequiredArgsConstructor
public class AliUploadStrategyImpl implements FileUploadStrategy {

    private final Logger logger = LoggerFactory.getLogger(AliUploadStrategyImpl.class);

    private final SystemConfigService systemConfigService;

    private final FileStorageService service;
    private final String platform = "ali-1";


    @PostConstruct
    private void init(){
        SystemConfig systemConfig = systemConfigService.getCustomizeOne();

        FileStorageProperties.AliyunOssConfig config = new FileStorageProperties.AliyunOssConfig();
        config.setPlatform(platform);
        config.setAccessKey(systemConfig.getQiNiuAccessKey());
        config.setSecretKey(systemConfig.getQiNiuSecretKey());
        config.setDomain(systemConfig.getQiNiuPictureBaseUrl());
        config.setBucketName(systemConfig.getQiNiuBucket());

        service.getFileStorageList().addAll(FileStorageServiceBuilder.buildAliyunOssFileStorage(Collections.singletonList(config),null));

        logger.info("------初始化阿里云oss上传配置文件成功-----");
    }


    @Override
    public String fileUpload(MultipartFile file,String suffix) {
        String path = DateUtil.dateTimeToStr(DateUtil.getNowDate(), DateUtil.YYYYMMDD)  + "/";
        return service.of(file).setPath(path).setPlatform(platform).setSaveFilename(file.getOriginalFilename()).upload().getUrl();
    }
    /**
     * 删除文件 -- 阿里云OSS
     *
     * @param key   文件url
     * @return      ResponseResult
     */
    @Override
    public Boolean deleteFile(String ...key) {
      return false;
    }
}
