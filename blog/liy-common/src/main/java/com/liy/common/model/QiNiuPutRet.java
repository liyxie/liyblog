package com.liy.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author LiY
 *
 * 七牛云响应实体类
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QiNiuPutRet {
    public String key; // 文件保存的 key
    public String hash; // 文件保存的 Etag
    public String bucket; // 文件保存的 bucket
    public long fsize; // 文件的大小，单位：B

}
