package com.liy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.liy.common.RedisConstants;
import com.liy.entity.Article;
import com.liy.entity.SystemConfig;
import com.liy.mapper.*;
import com.liy.service.RedisService;
import com.liy.service.SystemConfigService;
import com.liy.utils.DateUtil;
import com.liy.vo.article.SystemArticleContributeVO;
import com.liy.vo.category.SystemCategoryCountVO;
import com.liy.vo.system.SystemHomeDataVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.liy.enums.PublishEnum.PUBLISH;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeServiceImpl {

    private final ArticleMapper articleMapper;

    private final MessageMapper messageMapper;

    private final TagsMapper tagsMapper;

    private final CategoryMapper categoryMapper;

    private final UserLogMapper userLogMapper;

    private final SystemConfigService systemConfigService;

    private final RedisService redisService;

    private final UserMapper userMapper;

    private static final String CONTRIBUTE_DATE = "contributeDate";

    private static final String BLOG_CONTRIBUTE_COUNT = "blogContributeCount";

    private static final String RESULT_KEY = "result";

    private static final String CATEGORY_LIST_KEY = "categoryList";



    /**
     * 文章、留言、用户、ip统计
     * @return
     */
    public Map<String,Integer> getDashboardTopStatistics(){
        Map<String,Integer> map = new ConcurrentHashMap<>();
        map.put("article", articleMapper.selectList(null).size());
        map.put("message",messageMapper.selectList(null).size());
        map.put("user",userMapper.selectCount(null));
        map.put("viewsCount",getWebsiteVisitVolume());
        return map;
    }



    public SystemHomeDataVO getDashboardBottomStatistics() {
        SystemHomeDataVO systemHomeDataVO = new SystemHomeDataVO();
        try {
            // 初始化文章排行
            systemHomeDataVO.setArticles(loadTopArticles());
            // 初始化文章贡献度
            systemHomeDataVO.setContribute(getArticleContribution());
            // 初始化分类统计
            systemHomeDataVO.setCategoryList(getArticleCountsByCategory());
            // 初始化用户访问量
            systemHomeDataVO.setUserAccess(getAccessLogsForLastSevenDays());
            // 初始化标签列表
            systemHomeDataVO.setTagsList(tagsMapper.getTagKeyValuePairs());
            // 初始化系统配置
            SystemConfig systemConfig = systemConfigService.getCustomizeOne();
            if (systemConfig != null) {
                systemHomeDataVO.setDashboard(systemConfig.getDashboardNotification());
                systemHomeDataVO.setOpenDashboardNotification(systemConfig.getOpenDashboardNotification());
            } else {
                log.warn("系统配置获取失败或为空");
            }
        } catch (Exception e) {
            log.error("初始化系统首页数据异常：", e);
        }
        return systemHomeDataVO;
    }

    public static List<String> getDatesFromPastYear() {
        List<String> dateList = new ArrayList<>();
        Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, -1);

        do {
            dateList.add(DateUtil.formateDate(calendar.getTime(), DateUtil.YYYY_MM_DD));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } while (currentDate.after(calendar.getTime()));

        return dateList;
    }
    /**
     * 获取文章贡献度
     * @return
     */
    public Map<String, Object> getArticleContribution () {

        Map<String, Object> map = new HashMap<>();
        List<Object> contributes = new ArrayList<>();
        List<List<Object>> result = new ArrayList<>();

        // 异常处理和边界条件
        try {
            List<String> months = getDatesFromPastYear();
            if (months.isEmpty() || months.size() < 2) {
                throw new IllegalArgumentException("Months list must contain at least two elements.");
            }
            String lastTime = months.get(0), nowTime = months.get(months.size() - 1);

            // 性能优化：通过分组避免重复过滤
            Map<String, Integer> articlesCountByMonth = articleMapper.contribute(lastTime, nowTime)
                    .stream()
                    .collect(Collectors.groupingBy(SystemArticleContributeVO::getDate, Collectors.summingInt(SystemArticleContributeVO::getCount)));

            months.forEach(item -> {
                List<Object> list = new ArrayList<>();
                list.add(item);
                // 直接从映射中获取计数，如果不存在则为0
                list.add(articlesCountByMonth.getOrDefault(item, 0));
                result.add(list);
            });

            contributes.add(lastTime);
            contributes.add(nowTime);
            map.put(CONTRIBUTE_DATE, contributes);
            map.put(BLOG_CONTRIBUTE_COUNT, result);
        } catch (Exception e) {
            // 日志记录或其他异常处理方法
            e.printStackTrace();
            // 根据实际情况，可以选择返回空map、错误代码或抛出自定义异常
            return Collections.emptyMap();
        }

        return map;
    }

    /**
     * 分类统计
     * @return
     */
    public Map<String, Object> getArticleCountsByCategory(){
        Map<String, Object> map = new HashMap<>();

        try {
            List<SystemCategoryCountVO> result = categoryMapper.getArticleCountsByCategory();

            // 检查返回的结果是否为null
            if (result != null) {
                List<String> list = result.stream().map(SystemCategoryCountVO::getName).collect(Collectors.toList());
                map.put(RESULT_KEY, result);
                map.put(CATEGORY_LIST_KEY, list);
            } else {
                // 可以选择记录日志，或者添加其他的错误处理机制
                log.info("No article count data available.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 根据需求，可以选择在这里返回null，或者空的Map，或者抛出运行时异常
            return new HashMap<>();
        }

        return map;
    }

    /**
     * 获取一周的用户访问数据
     * @return
     */
    public List<Map<String, Object>> getAccessLogsForLastSevenDays() {
        LocalDate sevenDaysAgo = LocalDate.now().minus(7, ChronoUnit.DAYS);
        String formattedDate = DateUtil.formateDate(Date.from(sevenDaysAgo.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), DateUtil.YYYY_MM_DD);
        return userLogMapper.getAccessLogsForLastSevenDays(formattedDate);
    }

    /**
     * 获取网站访问量
     * @return
     */
    private Integer getWebsiteVisitVolume() {
        try {
            // 使用Optional避免直接null检查，增强代码的可读性
            return Optional.ofNullable(redisService.getCacheObject(RedisConstants.BLOG_VIEWS_COUNT))
                    .filter(count -> count instanceof Integer)
                    .map(Integer.class::cast)
                    .orElse(0);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取阅读量最高的前6条文章
     * @return
     */
    private List<Article> loadTopArticles() {
        // 使用更具描述性的方法名称
        return articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getQuantity, Article::getTitle, Article::getId)
                .eq(Article::getIsPublish, PUBLISH.getCode())
                .orderByDesc(Article::getQuantity).last("limit 6"));
    }

}
