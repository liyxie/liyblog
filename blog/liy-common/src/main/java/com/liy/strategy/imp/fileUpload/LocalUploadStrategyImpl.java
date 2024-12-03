package com.liy.strategy.imp.fileUpload;

import com.liy.config.FileConfig;
import com.liy.entity.SystemFileConfig;
import com.liy.enums.file.FileUpdateModelEnum;
import com.liy.exception.BusinessException;
import com.liy.strategy.FileUploadStrategy;
import com.liy.utils.DateUtil;
import com.liy.utils.FileUtils;
import com.liy.utils.UUIDUtils;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.ArrayList;
import java.util.List;



@Service("localUploadStrategyImpl")
@RequiredArgsConstructor
public class LocalUploadStrategyImpl implements FileUploadStrategy {

    private final Logger logger = LoggerFactory.getLogger(LocalUploadStrategyImpl.class);

    private final FileConfig fileConfig;

    private SystemFileConfig systemFileConfig;

    @Override
    @PostConstruct
    public void init(){
        this.systemFileConfig = fileConfig.getFileConfigByEnum(FileUpdateModelEnum.LOCAL);
    }


    /**
     * 上传文件
     * @param file 文件
     * @return 文件地址
     */
    @Override
    public String fileUpload(MultipartFile file, String path) {
        String suffix = null;

        String savePath = systemFileConfig.getPath();
        File savePathFile = new File(savePath);

        String filename = null;
        try {
            if (!savePathFile.exists()) {
                //若不存在该目录，则创建目录
                savePathFile.mkdir();
            }
            suffix = FileUtils.getExtension(file.getInputStream());
            //通过UUID生成唯一文件名
            filename = FileUtils.splicingUrl('.', UUIDUtils.getUuid(), suffix);
            //将文件保存指定目录
            file.transferTo(new File(savePath + path + filename));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException();
        }
        //返回文件名称
        return FileUtils.splicingUrl('/', systemFileConfig.getPath(), path, filename);
    }

    /**
     * 删除文件
     * @param keys 文件名
     * @return
     */
    @Override
    public Boolean deleteFile(String... keys) {

        return false;
    }


    private static List<String> getFileNames(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        List<String> fileNames = new ArrayList<>();
        return getFileNames(file, fileNames);
    }

    /**
     * 递归查询文件
     * @param file
     * @param fileNames
     * @return
     */
    private static List<String> getFileNames(File file, List<String> fileNames) {
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                getFileNames(f, fileNames);
            } else {
                fileNames.add(f.getName());
            }
        }
        return fileNames;
    }
}
