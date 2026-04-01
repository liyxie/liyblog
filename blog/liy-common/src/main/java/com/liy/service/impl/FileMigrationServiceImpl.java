package com.liy.service.impl;

import com.liy.common.ResponseResult;
import com.liy.entity.SystemFileConfig;
import com.liy.mapper.SystemFileConfigMapper;
import com.liy.service.FileMigrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 文件URL迁移服务实现
 * 将数据库中各表的完整文件URL（含域名前缀）替换为相对路径
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileMigrationServiceImpl implements FileMigrationService {

    private final SystemFileConfigMapper systemFileConfigMapper;

    private final JdbcTemplate jdbcTemplate;

    /**
     * 各表需要迁移的字段配置：[表名, 字段名]
     */
    private static final String[][] MIGRATION_TARGETS = {
            {"b_resource",   "url"},
            {"b_article",    "avatar"},
            {"b_user",       "avatar"},
            {"b_user",       "bj_cover"},
            {"b_say",        "img_url"},
            {"b_feed_back",  "img_url"},
            {"b_web_config", "logo"},
            {"b_web_config", "author_avatar"},
            {"b_web_config", "tourist_avatar"},
            {"b_web_config", "ali_pay"},
            {"b_web_config", "weixin_pay"},
    };

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult migrateFileUrls() {
        List<SystemFileConfig> configs = systemFileConfigMapper.selectList(null);
        if (configs == null || configs.isEmpty()) {
            return ResponseResult.error("未找到任何文件存储配置，迁移终止");
        }

        int totalUpdated = 0;
        List<String> details = new ArrayList<>();

        for (SystemFileConfig config : configs) {
            String domain = config.getUrl();
            if (domain == null || domain.isEmpty()) {
                continue;
            }
            String normalizedDomain = domain.replaceAll("/$", "");

            for (String[] target : MIGRATION_TARGETS) {
                String table = target[0];
                String column = target[1];
                try {
                    String sql = "UPDATE " + table + " SET " + column
                            + " = SUBSTRING(" + column + ", LENGTH(?) + 2)"
                            + " WHERE " + column + " LIKE ?";
                    String prefix = normalizedDomain + "/";
                    int rows = jdbcTemplate.update(sql, prefix, prefix + "%");
                    if (rows > 0) {
                        String msg = table + "." + column + ": " + rows + " 条记录（域名=" + normalizedDomain + "）";
                        details.add(msg);
                        totalUpdated += rows;
                        log.info("[FileMigration] {}", msg);
                    }
                } catch (Exception e) {
                    log.warn("[FileMigration] 迁移 {}.{} 时出错: {}", table, column, e.getMessage());
                    details.add("⚠ " + table + "." + column + " 迁移失败: " + e.getMessage());
                }
            }
        }

        String summary = "迁移完成，共更新 " + totalUpdated + " 条记录";
        log.info("[FileMigration] {}", summary);
        return ResponseResult.success(summary).putExtra("details", details);
    }
}
