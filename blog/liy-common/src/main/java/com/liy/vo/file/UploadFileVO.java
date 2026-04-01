package com.liy.vo.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件上传响应 VO
 * url  - 含域名的完整访问地址，供前端图片预览
 * path - 不含域名的相对路径，供前端存入表单字段（存库）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "UploadFileVO", description = "文件上传响应")
public class UploadFileVO {

    @Schema(description = "完整访问URL（含域名，供前端图片预览）")
    private String url;

    @Schema(description = "文件路径（不含域名，供前端存入表单字段使用）")
    private String path;
}
