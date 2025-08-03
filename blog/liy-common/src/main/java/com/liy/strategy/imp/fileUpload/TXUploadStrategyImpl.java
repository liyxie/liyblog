package com.liy.strategy.imp.fileUpload;

import com.liy.config.FileConfig;
import com.liy.entity.SystemFileConfig;
import com.liy.enums.file.FileUpdateModelEnum;
import com.liy.enums.file.TXAreaEnum;
import com.liy.exception.BusinessException;
import com.liy.strategy.FileUploadStrategy;
import com.liy.utils.FileUtils;
import com.liy.utils.StringUtils;
import com.liy.utils.UUIDUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author LiY
 *
 * 腾讯云文件存储
 */

@Service("tXUploadStrategyImpl")
@RequiredArgsConstructor
@Slf4j
public class TXUploadStrategyImpl implements FileUploadStrategy {

    private final FileConfig fileConfig;

    private SystemFileConfig systemFileConfig;

    private COSClient cosClient;

    @Override
    @PostConstruct
    public void init() {
        try {
            this.systemFileConfig = fileConfig.getFileConfigByEnum(FileUpdateModelEnum.TX);
            COSCredentials cred = new BasicCOSCredentials(systemFileConfig.getAccessKey(), systemFileConfig.getSecretKey());
            Region region = new Region(TXAreaEnum.getRegion(systemFileConfig.getArea()));
            ClientConfig clientConfig = new ClientConfig(region);
            clientConfig.setHttpProtocol(HttpProtocol.https);
            this.cosClient = new COSClient(cred, clientConfig);
        }catch (Exception e){
            log.warn("腾讯云存储初始化失败");
        }
    }

    private ObjectMetadata getObjectMetadata(InputStream inputStream, String fileType) throws IOException {
        // 创建上传Object的Metadata
        ObjectMetadata objectMetadata = new ObjectMetadata();
        // - 使用输入流存储，需要设置请求长度
        objectMetadata.setContentLength(inputStream.available());
        // - 设置缓存
        objectMetadata.setCacheControl("no-cache");
        // - 设置Content-Type
        objectMetadata.setContentType(fileType);
        return objectMetadata;
    }

    @Override
    public String fileUpload(MultipartFile file, String path) {
        String suffix = null;
        // 指定文件将要存放的存储桶
        String bucketName = systemFileConfig.getBucket();
        // 指定文件上传到 COS 上的路径，即对象键。例如对象键为 folder/picture.jpg，则表示将文件 picture.jpg 上传到 folder 路径下
        String key = null;
        InputStream inputStream = null;
        ObjectMetadata objectMetadata = null;
        try {
            suffix = FileUtils.getExtension(file.getInputStream());
            key = UUIDUtils.getUuid() + "." + suffix;
            key = StringUtils.splicingUrl('/', systemFileConfig.getPath(), path, key);
            inputStream = file.getInputStream();
            objectMetadata = getObjectMetadata(inputStream, suffix);
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            key = StringUtils.splicingUrl('/', systemFileConfig.getUrl(), key);
        }catch (IOException e){
            e.printStackTrace();
            throw new BusinessException("tx文件上传错误");
        }catch (Exception e){
            throw new BusinessException("腾讯云文件存储错误: " + e.getMessage());
        }
        return key;
    }

    @Override
    public Boolean deleteFile(String... fileName) {
        return null;
    }
}
