package com.liy.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.liy.vo.system.SystemFileConfigVO;
import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(description = "id")
  private Integer id;
  @Schema(description = "类型名")
  private String typeName;
  @Schema(description = "链接")
  private String url;
  @Schema(description = "地域")
  private String area;
  @Schema(description = "桶/空间")
  private String bucket;
  @Schema(description = "公钥")
  private String accessKey;
  @Schema(description = "私钥")
  private String secretKey;
  @Schema(description = "存储路径")
  private String path;
  @Schema(description = "策略")
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
