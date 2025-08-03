package com.liy.vo.system;


import com.liy.entity.SystemFileConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title = "SystemFileConfigVO对象", description = "系统文件存储配置")
public class SystemFileConfigVO implements Serializable {

  @Schema(description = "id")
  private Integer id;
  @Schema(description = "存储类型名称")
  private String typeName;
  @Schema(description = "存储链接")
  private String url;
  @Schema(description = "存储区域")
  private String area;
  @Schema(description = "存储空间/桶")
  private String bucket;
  @Schema(description = "公钥/腾讯secretId")
  private String accessKey;
  @Schema(description = "私钥")
  private String secretKey;
  @Schema(description = "存储主路径")
  private String path;

  public static SystemFileConfigVO poToVo(SystemFileConfig systemFileConfig) {
    if (systemFileConfig == null) {
      return null;
    }
    SystemFileConfigVO systemFileConfigVO = new SystemFileConfigVO();
    systemFileConfigVO.setId(systemFileConfig.getId());
    systemFileConfigVO.setTypeName(systemFileConfig.getTypeName());
    systemFileConfigVO.setUrl(systemFileConfig.getUrl());
    systemFileConfigVO.setArea(systemFileConfig.getArea());
    systemFileConfigVO.setBucket(systemFileConfig.getBucket());
    systemFileConfigVO.setAccessKey(systemFileConfig.getAccessKey());
    systemFileConfigVO.setSecretKey(systemFileConfig.getSecretKey());
    systemFileConfigVO.setPath(systemFileConfig.getPath());
    return systemFileConfigVO;
  }


}
