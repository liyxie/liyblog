package com.liy.strategy.imp.fileUpload;

import com.liy.common.model.QiNiuPutRet;
import com.liy.config.FileConfig;
import com.liy.entity.SystemFileConfig;
import com.liy.enums.file.FileUpdateModelEnum;
import com.liy.enums.file.QiNiuAreaEnum;
import com.liy.strategy.FileUploadStrategy;
import com.liy.entity.SystemConfig;
import com.liy.service.SystemConfigService;
import com.liy.utils.DateUtil;
import com.liy.utils.FileUtils;
import com.liy.utils.StringUtils;
import com.liy.utils.UUIDUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.util.Auth;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.FileStorageServiceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;


@Service("qiNiuUploadStrategyImpl")
@RequiredArgsConstructor
public class QiNiuUploadStrategyImpl implements FileUploadStrategy {

    private final Logger logger = LoggerFactory.getLogger(QiNiuUploadStrategyImpl.class);

    private final FileConfig fileConfig;

    private SystemFileConfig systemFileConfig;

    private UploadManager uploadManager;
    private String upToken;
    private BucketManager bucketManager;

    @Override
    @PostConstruct
    public void init(){
        try {
            this.systemFileConfig = fileConfig.getFileConfigByEnum(FileUpdateModelEnum.QIN);
            Region region = QiNiuAreaEnum.getRegion(systemFileConfig.getArea());
            com.qiniu.storage.Configuration configuration = new com.qiniu.storage.Configuration(region);
            Auth auth = Auth.create(systemFileConfig.getAccessKey(), systemFileConfig.getSecretKey());
            this.upToken = auth.uploadToken(systemFileConfig.getBucket());
            this.bucketManager = new BucketManager(auth,configuration);
            this.uploadManager = new UploadManager(configuration);
        }catch (Exception e){
            logger.warn("七牛云初始化失败");
        }

    }


    @Override
    public String fileUpload(MultipartFile file, String path) {
        String suffix;

        FileInputStream inputStream = null;
        String key = null;
        try {
            suffix = FileUtils.getExtension(file.getInputStream());
            key = StringUtils.splicingUrl('/', systemFileConfig.getPath(), path , UUIDUtils.getUuid() + "." + suffix);
            inputStream = (FileInputStream) file.getInputStream();
            Response response = uploadManager.put(inputStream, key, upToken,null,null);
            //解析上传成功的结果
            QiNiuPutRet putRet = response.jsonToObject(QiNiuPutRet.class);
            key = StringUtils.splicingUrl('/', systemFileConfig.getUrl(), putRet.key);
        } catch (QiniuException ex) {
            Response r = ex.response;
            logger.info("QiniuException:{}",r.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null){
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
     * 批量删除文件
     * @return
     */
    @Override
    public Boolean deleteFile(String ...keys) {
        try {
            BucketManager.BatchOperations batchOperations = new BucketManager.BatchOperations();
            batchOperations.addDeleteOp(systemFileConfig.getBucket(), keys);
            Response response = bucketManager.batch(batchOperations);
            BatchStatus[] batchStatusList = response.jsonToObject(BatchStatus[].class);
            for (int i = 0; i < keys.length; i++) {
                BatchStatus status = batchStatusList[i];
                String key = keys[i];
                System.out.print(key + "\t");
                if (status.code == 200) {
                    System.out.println("delete success");
                } else {
                    System.out.println(status.data.error);
                }
            }
            return true;
        } catch (QiniuException ex) {
            System.err.println(ex.response.toString());
            return false;
        }
    }
}
