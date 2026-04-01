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
import jakarta.annotation.PostConstruct;
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

    /**
     * 将完整URL剥离已知域名前缀，只保留相对路径，用于保存到数据库前处理
     * 若传入的已是相对路径或外部链接（不在配置域名内），则直接返回
     * @param url 可能包含域名的完整URL
     * @return 剥离域名后的路径
     */
    public String stripDomain(String url) {
        if (url == null || url.isEmpty()) return url;
        if (!url.startsWith("http://") && !url.startsWith("https://")) return url;
        for (SystemFileConfig config : systemFileConfigMap.values()) {
            if (config.getUrl() == null || config.getUrl().isEmpty()) continue;
            String prefix = config.getUrl().replaceAll("/$", "") + "/";
            if (url.startsWith(prefix)) {
                return url.substring(prefix.length());
            }
        }
        return url;
    }

    /**
     * 对逗号分隔的多个URL批量剥离域名（用于 imgUrl 等字段）
     */
    public String stripDomainMulti(String urls) {
        if (urls == null || urls.isEmpty()) return urls;
        String[] parts = urls.split(",");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(stripDomain(parts[i].trim()));
        }
        return sb.toString();
    }

    /**
     * 使用当前激活的文件存储配置，将路径拼接为完整URL
     * 若传入的已是完整URL（http/https 开头），则直接返回（兼容外部链接）
     * @param path 文件相对路径
     * @return 完整访问URL
     */
    public String buildUrl(String path) {
        if (path == null || path.isEmpty()) return path;
        if (path.startsWith("http://") || path.startsWith("https://")) return path;
        SystemFileConfig config = getNowFileConfig();
        if (config == null || config.getUrl() == null || config.getUrl().isEmpty()) return path;
        String domain = config.getUrl().replaceAll("/$", "");
        String cleanPath = path.replaceAll("^/", "");
        return domain + "/" + cleanPath;
    }

    /**
     * 按 b_system_file_config.typeName 精确匹配配置，将路径拼接为完整URL
     * 主要用于 b_resource 查询时，通过 platform 字段准确对应各平台域名
     * @param typeName 平台类型名（对应 b_system_file_config.type_name）
     * @param path     文件相对路径
     * @return 完整访问URL
     */
    public String buildUrlByTypeName(String typeName, String path) {
        if (path == null || path.isEmpty()) return path;
        if (path.startsWith("http://") || path.startsWith("https://")) return path;
        SystemFileConfig config = systemFileConfigMap.values().stream()
                .filter(c -> typeName != null && typeName.equals(c.getTypeName()))
                .findFirst()
                .orElse(getNowFileConfig());
        if (config == null || config.getUrl() == null || config.getUrl().isEmpty()) return path;
        String domain = config.getUrl().replaceAll("/$", "");
        String cleanPath = path.replaceAll("^/", "");
        return domain + "/" + cleanPath;
    }
}
