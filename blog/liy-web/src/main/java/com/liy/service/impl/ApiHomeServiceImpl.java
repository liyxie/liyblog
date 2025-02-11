package com.liy.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.liy.mapper.ArticleMapper;
import com.liy.mapper.TagsMapper;
import com.liy.mapper.WebConfigMapper;
import com.liy.service.ApiHomeService;
import com.liy.service.RedisService;
import com.liy.common.RedisConstants;
import com.liy.common.ResponseResult;
import com.liy.entity.Tags;
import com.liy.entity.WebConfig;
import com.liy.utils.IpUtil;
import com.liy.utils.CustomHttpUtil;
import com.liy.vo.article.ApiArticleListVO;
import com.liy.vo.article.SystemArticleListVO;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class ApiHomeServiceImpl implements ApiHomeService {

    private final RedisService redisService;

    private final WebConfigMapper webConfigMapper;

    private final ArticleMapper articleMapper;

    private final TagsMapper tagsMapper;



    /**
     * 添加访问量
     * @return
     */

    public ResponseResult report() {
        // 获取ip
        String ipAddress = IpUtil.getIp();
        // 通过浏览器解析工具类UserAgent获取访问设备信息
        UserAgent userAgent = IpUtil.getUserAgent(IpUtil.getRequest());
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        // 生成唯一用户标识
        String uuid = ipAddress + browser.getName() + operatingSystem.getName();
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        // 判断是否访问
        if (!redisService.sIsMember(RedisConstants.UNIQUE_VISITOR, md5)) {
            // 访客量+1
            redisService.incr(RedisConstants.UNIQUE_VISITOR_COUNT, 1);
            // 保存唯一标识
            redisService.sAdd(RedisConstants.UNIQUE_VISITOR, md5);
        }
        // 访问量+1
        redisService.incr(RedisConstants.BLOG_VIEWS_COUNT, 1);

        return ResponseResult.success(IpUtil.getIp2region(ipAddress));
    }

    /**
     * 获取首页数据
     * @return
     */
    public ResponseResult getHomeData() {

        //获取首页轮播
        List<SystemArticleListVO> articles = articleMapper.selectListByBanner();

        //获取标签云
        List<Tags> tags = tagsMapper.selectList(null);
        //推荐文章
        List<ApiArticleListVO> apiArticleListVOS = articleMapper.selectRecommendArticle();

        return ResponseResult.success().putExtra("articles",articles).putExtra("newArticleList",apiArticleListVOS).putExtra("tagCloud",tags);

    }

    /**
     * 获取网站相关信息
     * @return
     */
    public ResponseResult getWebSiteInfo() {
        //网站信息
        WebConfig webConfig = webConfigMapper.selectOne(null);

        //获取访问量
        Long count = Optional.ofNullable(redisService.getCacheObject(RedisConstants.BLOG_VIEWS_COUNT))
                .map(Object::toString)
                .map(Long::valueOf)
                .orElse(0L);

        //获取访客量
        Long visitorAccess = redisService.getCacheSetKeyNumber(RedisConstants.UNIQUE_VISITOR);

        return ResponseResult.success(webConfig).putExtra("siteAccess", count)
                .putExtra("visitorAccess",visitorAccess);
    }

    /**
     * 获取各大平台的热搜
     * @param type 平台
     * @return
     */
    @Override
    public ResponseResult hot(String type) {
        String url = "https://www.coderutil.com/api/resou/v1/" + type;
        JSONObject jsonObject = CustomHttpUtil.sendCuApiHttpUrl(url);
        return ResponseResult.success(jsonObject);
    }

    /**
     * 线报榜单 http://new.ixbk.net
     * 6小时榜单，最快300秒请求一次
     * @param type 平台
     * @return 榜单
     */
    @Override
    public ResponseResult hotXianBao(String type) {
        JSONArray jsonArray;
        List<Object> list;
        if(redisService.hasKey("xianbao")){
            list = JSON.parseArray( (String) redisService.getCacheObject("XianBao"));
        }
        else {
            String url = "http://new.ixbk.net/plus/json/rank/sanxiaoshi.json";
            jsonArray = CustomHttpUtil.sendApiHttpUrl(url);
            list = jsonArray.subList(0, Math.min(15, jsonArray.size()));
            // 缓存，10分钟更新
            redisService.setCacheObject("XianBao", list.toString(),600, TimeUnit.SECONDS);
        }
        return ResponseResult.success(list);
    }

    @Override
    public ResponseResult hotDoYing(String type) {
        String url = "https://dsp.lenovo.com.cn/lenovo/bid?positionId=50411";
        JSONObject jsonArray = CustomHttpUtil.sendApiHttpUrlByO(url);
        return ResponseResult.success(jsonArray);
    }
}
