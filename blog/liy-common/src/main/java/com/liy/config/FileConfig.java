package com.liy.config;

import cn.hutool.core.util.ObjectUtil;
import com.liy.entity.SystemConfig;
import com.liy.entity.SystemFileConfig;
import com.liy.enums.file.FileUpdateModelEnum;
import com.liy.enums.file.TXAreaEnum;
import com.liy.mapper.SystemConfigMapper;
import com.liy.mapper.SystemFileConfigMapper;
import com.liy.service.SystemConfigService;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.region.Region;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.FileStorageServiceBuilder;
import org.dromara.x.file.storage.core.platform.FileStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author LiY
 *
 * 文件存储配置
 */

@Configuration
@RequiredArgsConstructor
public class FileConfig {

    private final SystemFileConfigMapper systemFileConfigMapper;

    private final SystemConfigMapper systemConfigMapper;

    private Map<Integer, SystemFileConfig> systemFileConfigMap;

    /**
     * 当前文件存储类型
     */
    private Integer code;

    private CopyOnWriteArrayList<FileStorage> fileStorageList;




    // 默认本地文件存储路径
    @Value("${file.upload-folder}")
    private String UPLOAD_FOLDER;

    /**
     * 初始化信息
     */
    @PostConstruct
    public void init(){
        this.systemFileConfigMap = new HashMap<Integer, SystemFileConfig>();
        this.code = null;
        systemFileConfigMapper.selectList(null).forEach(f -> {
            systemFileConfigMap.put(f.getId(), f);
        });
        this.code = systemConfigMapper.selectOne(null).getFileUploadWay();
    }

    /**
     * 获得当前文件操作配置
     * @return
     */
    public SystemFileConfig getNowFileConfig(){
        return systemFileConfigMap.get(code);
    }

    public SystemFileConfig getFileConfigByEnum(FileUpdateModelEnum fileUpdateModelEnum){
        return systemFileConfigMap.get(fileUpdateModelEnum.getType());
    }

    public String getPath(){
        return ObjectUtil.isNull(getNowFileConfig().getPath()) ? this.UPLOAD_FOLDER : getNowFileConfig().getPath();
    }

    /**
     * 当前文件上传策略id
     * @return
     */
    public Integer getNowFileConfigId() {
        return getNowFileConfig().getId();
    }
}
