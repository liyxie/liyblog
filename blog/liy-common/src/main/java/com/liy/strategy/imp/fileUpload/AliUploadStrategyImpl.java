package com.liy.strategy.imp.fileUpload;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.liy.config.FileConfig;
import com.liy.entity.SystemConfig;
import com.liy.entity.SystemFileConfig;
import com.liy.enums.file.FileUpdateModelEnum;
import com.liy.service.SystemConfigService;
import com.liy.strategy.FileUploadStrategy;
import com.liy.utils.DateUtil;
import com.liy.utils.FileUtils;
import com.liy.utils.UUIDUtils;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.FileStorageServiceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

@Service("aliUploadStrategyImpl")
@RequiredArgsConstructor
public class AliUploadStrategyImpl implements FileUploadStrategy {

    private final Logger logger = LoggerFactory.getLogger(AliUploadStrategyImpl.class);

    private final FileConfig fileConfig;

    private SystemFileConfig systemFileConfig;
    private OSS ossClient;

    @Override
    @PostConstruct
    public void init(){
        try {
            this.systemFileConfig = fileConfig.getFileConfigByEnum(FileUpdateModelEnum.ALI);
            ossClient = new OSSClientBuilder().build(
                    systemFileConfig.getUrl(),
                    systemFileConfig.getAccessKey(),
                    systemFileConfig.getSecretKey(),
                    getClientBuilderConfiguration()
            );
        }catch (Exception e){
            logger.warn("阿里云初始化失败");
        }
    }

    private ClientBuilderConfiguration getClientBuilderConfiguration() {
        // 创建ClientConfiguration。ClientConfiguration是OSSClient的配置类，可配置代理、连接超时、最大连接数等参数。
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        // 设置OSSClient允许打开的最大HTTP连接数，默认为1024个。
        conf.setMaxConnections(200);
        // 设置Socket层传输数据的超时时间，默认为50000毫秒。
        conf.setSocketTimeout(10000);
        // 设置建立连接的超时时间，默认为50000毫秒。
        conf.setConnectionTimeout(10000);
        // 设置从连接池中获取连接的超时时间（单位：毫秒），默认不超时。
        conf.setConnectionRequestTimeout(1000);
        // 设置连接空闲超时时间。超时则关闭连接，默认为60000毫秒。
        conf.setIdleConnectionTime(60000);
        // 设置失败请求重试次数，默认为3次。
        conf.setMaxErrorRetry(3);
        // 设置是否支持将自定义域名作为Endpoint，默认支持。
        conf.setSupportCname(true);
        // 设置是否开启HTTP重定向，默认开启。
        conf.setRedirectEnable(true);
        // 设置是否开启SSL证书校验，默认开启。
        conf.setVerifySSLEnable(false);
        return conf;
    }


    @Override
    public String fileUpload(MultipartFile file, String path) {
        String suffix = null;
        String key = null;

        //获取上传文件输入流
        InputStream inputStream = null;
        try {
            suffix = FileUtils.getExtension(file.getInputStream());
            inputStream = file.getInputStream();
            //在文件名称里面添加随机唯一值（因为如果上传文件名称相同的话，后面的文件会将前面的文件给覆盖了）
            String newFileName = systemFileConfig.getPath() + path + UUIDUtils.getUuid() + "." + suffix;

            // 调用oss方法实现上传
            // 参数一：Bucket名称  参数二：上传到oss文件路径和文件名称  比如 /aa/bb/1.jpg 或者直接使用文件名称  参数三：上传文件的流
            ossClient.putObject(systemFileConfig.getBucket(), newFileName, inputStream);

            //把上传之后的文件路径返回，需要把上传到阿里云路径返回    https://edu-guli-eric.oss-cn-beijing.aliyuncs.com/1.jpg
            key = FileUtils.splicingUrl('/', systemFileConfig.getUrl(), path, newFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return key;
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
