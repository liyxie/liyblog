package com.liy.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liy.common.ResponseResult;
import com.liy.utils.PageUtil;
import com.liy.vo.cache.CacheVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/system/cache")
@RequiredArgsConstructor
@Tag(name = "后台缓存监控")
public class CacheController {

    private final RedisTemplate<String,String> redisTemplate;

    @GetMapping(value = "/getCacheInfo")
    @Operation(summary = "缓存信息")
    public ResponseResult getCacheInfo() {
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::info);
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) RedisServerCommands::dbSize);

        Map<String, Object> result = new HashMap<>(3);
        result.put("info", info);
        result.put("dbSize", dbSize);
        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            Map<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("commandStats", pieList);
        return ResponseResult.success(result);
    }

    @GetMapping(value = "/list")
    @Operation(summary = "缓存key列表")
    public ResponseResult selectCacheKeysPage() {
        Page<CacheVO> page = new Page<CacheVO>();

        Set<String> keys = redisTemplate.keys("*");
        if (keys == null || keys.size() == 0) {
            return ResponseResult.success(page);
        }
        //分页获取
        int startIndex = (int) ((PageUtil.getPageNo() - 1) * PageUtil.getPageSize());
        int endIndex = (int) Math.min(startIndex + PageUtil.getPageSize(), keys.size());
        List<String> list = new ArrayList<>(keys).subList(startIndex, endIndex);

        List<CacheVO> cacheVOS = new ArrayList<>();
        for (String key : list) {
            CacheVO cacheVO = new CacheVO();
            cacheVO.setKey(key);
            cacheVOS.add(cacheVO);
        }
        page.setRecords(cacheVOS);
        page.setTotal(keys.size());

        return ResponseResult.success(page);
    }

    @GetMapping(value = "/getValue/{key}")
    @Operation(summary = "根据键获取值")
    public ResponseResult getValue(@PathVariable String key) {
        String type = (String) redisTemplate.execute(
                (RedisCallback<String>) connection -> String.valueOf(connection.type(key.getBytes()))
        );

        Object data = null;
        switch (Objects.requireNonNull(type)) {
            case "STRING":
                // key的类型是字符串
                data = redisTemplate.opsForValue().get(key);
                break;
            case "LIST":
                // key的类型是列表
                data = redisTemplate.opsForList().range(key, 0, -1);
                break;
            case "SET":
                // key的类型是集合
                data = redisTemplate.boundSetOps(key).members();
                break;
            case "ZSET":
                // key的类型是有序集合
                data = redisTemplate.opsForZSet().range(key,0,-1);
                break;
            case "HASH":
                // key的类型是哈希表
                data = redisTemplate.opsForHash().entries(key);
                break;
            default:
                break;
        }
        return ResponseResult.success(data);
    }

    @SaCheckPermission("system:cache:delete")
    @DeleteMapping(value = "/delete/{key}")
    @Operation(summary = "根据键删除缓存")
    public ResponseResult deleteCache(@PathVariable String key) {
        return ResponseResult.success(redisTemplate.delete(key));
    }

}
