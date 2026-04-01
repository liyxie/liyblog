package com.liy.service;

import com.liy.common.ResponseResult;

/**
 * 文件URL迁移服务
 * 将数据库中存储的完整URL（含域名）剥离域名，只保留路径部分
 */
public interface FileMigrationService {

    /**
     * 将所有表中已存储的完整文件URL剥离域名，只保留相对路径
     * 适用场景：首次部署本次改造后，对存量数据进行一次性迁移
     * @return 迁移结果摘要
     */
    ResponseResult migrateFileUrls();
}
