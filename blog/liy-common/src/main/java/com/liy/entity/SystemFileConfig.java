package com.liy.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.liy.vo.system.SystemFileConfigVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * liy
 * 文件存储配置
 */

@Data
@ToString
@TableName("b_system_file_config")
public class SystemFileConfig {

  @ApiModelProperty(value = "id")
  private Integer id;
  @ApiModelProperty(value = "类型名")
  private String typeName;
  @ApiModelProperty(value = "链接")
  private String url;
  @ApiModelProperty(value = "地域")
  private String area;
  @ApiModelProperty(value = "桶/空间")
  private String bucket;
  @ApiModelProperty(value = "公钥")
  private String accessKey;
  @ApiModelProperty(value = "私钥")
  private String secretKey;
  @ApiModelProperty(value = "存储路径")
  private String path;
  @ApiModelProperty(value = "策略")
  private String strategy;

  public static SystemFileConfig voToPo(SystemFileConfigVO systemFileConfigVO) {
    if (systemFileConfigVO == null) {
      return null;
    }
    SystemFileConfig systemFileConfig = new SystemFileConfig();
    systemFileConfig.setId(systemFileConfigVO.getId());
    systemFileConfig.setTypeName(systemFileConfigVO.getTypeName());
    systemFileConfig.setUrl(systemFileConfigVO.getUrl());
    systemFileConfig.setArea(systemFileConfigVO.getArea());
    systemFileConfig.setBucket(systemFileConfigVO.getBucket());
    systemFileConfig.setAccessKey(systemFileConfigVO.getAccessKey());
    systemFileConfig.setSecretKey(systemFileConfigVO.getSecretKey());
    systemFileConfig.setPath(systemFileConfigVO.getPath());
    return systemFileConfig;
  }
}
