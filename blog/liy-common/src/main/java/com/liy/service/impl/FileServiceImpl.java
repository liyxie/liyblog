package com.liy.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.liy.common.FileConstants;
import com.liy.config.FileConfig;
import com.liy.service.FileService;
import com.liy.service.SystemConfigService;
import com.liy.common.Constants;
import com.liy.common.ResponseResult;
import com.liy.entity.Resource;
import com.liy.enums.DataEventEnum;
import com.liy.enums.file.FileUpdateModelEnum;
import com.liy.event.DataEventPublisherService;
import com.liy.exception.BusinessException;
import com.liy.strategy.context.FileUploadStrategyContext;
import com.liy.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;


@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileUploadStrategyContext fileUploadStrategyContext;

    private final DataEventPublisherService dataEventPublisherService;

    private final FileConfig fileConfig;

    /**
     * 上传文件
     * @param file
     * @return
     */
    @Override
    public ResponseResult upload(MultipartFile file) {

        if (file.getSize() > 1024 * 1024 * 10) {
            return ResponseResult.error("文件大小不能大于10M");
        }
        //获取文件后缀
        String suffix;
        try {
            suffix = FileUtils.getExtension(file.getInputStream());
            if (!Constants.FIELD_SUFFIX.toUpperCase().contains(suffix.toUpperCase())) {
                return ResponseResult.error("请选择jpg,jpeg,gif,png,mp4格式的图片");
            }
        }catch (IOException e){
            e.printStackTrace();
            throw new BusinessException("文件类型识别错误");
        }
        String key = fileUploadStrategyContext.executeFileUploadStrategy(file, FileConstants.Common);

        Resource resource = Resource.builder().url(key).type(suffix).platform(fileConfig.getNowFileConfig().getTypeName()).userId(StpUtil.getLoginIdAsString()).build();
        dataEventPublisherService.publishData(DataEventEnum.RESOURCE_ADD,resource);
        return ResponseResult.success(key);
    }


    /**
     * 删除文件
     * @param key
     * @return
     */
    @Override
    public ResponseResult delBatchFile(String ...key) {
        Boolean isSuccess = fileUploadStrategyContext.executeDeleteFileStrategy(key);
        if (!isSuccess) {
            throw new BusinessException("删除文件失败");
        }
        return ResponseResult.success();
    }

}
