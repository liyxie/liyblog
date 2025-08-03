package com.liy.quartz;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.liy.common.RedisConstants;
import com.liy.entity.Article;
import com.liy.entity.Tags;
import com.liy.service.ArticleService;
import com.liy.service.RedisService;
import com.liy.service.TagsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.liy.common.RedisConstants.ARTICLE_READING;
import static com.liy.common.RedisConstants.TAG_CLICK_VOLUME;

/**
 * @author blue
 * @date 2021/12/8
 * @apiNote 定时任务调度
 */
@Component("blogQuartz")
@RequiredArgsConstructor
public class BlogQuartz {
    private final RedisService redisService;

    private final ArticleService articleService;

    private final TagsService tagsService;



    public void ryParams(String params) {
        System.out.println("执行有参方法：" + params);
    }

    public void ryNoParams() {
        System.out.println("执行无参方法");
    }

    /**
     *  redis心跳,解决总是存在Command timed out after 3 second(s)的问题
     * @author blue
     * @date: 2021/8/18 17:58
     */
    public void redisTimer(){
        redisService.redisTimer();
    }

    /**
     *  自动更新阅读数
     * @author blue
     * @date: 2021/8/18 17:58
     */
    public void updateReadQuantity(){
        // 获取带阅读量的前缀key集合
        List<Article> articles = new ArrayList<>();
        Map<String, Object> map = redisService.getCacheMap(ARTICLE_READING);
        // 取出所有数据更新到数据库
        if(map.size() > 1){
            for (Map.Entry<String, Object> stringEntry : map.entrySet()) {
                String id = stringEntry.getKey();
                List<String> list = (List<String>) stringEntry.getValue();
                Article article = Article.builder()
                        .id(Long.parseLong(id))
                        .quantity(list.size())
                        .build();
                articles.add(article);
            }

            articleService.updateQuantityAdd(articles);
            redisService.deleteObject(ARTICLE_READING);
        }

    }

    /**
     * 自动增加阅读量
     */
    public void addReadQuantity(){
        List<Article> articles = articleService.list();

        articles.stream().filter(Objects::nonNull).forEach(a -> {
            a.setQuantity(a.getQuantity() + new Random().nextInt(10));
        });
        articleService.updateBatchById(articles);
    }

//    public void add


    /**
     * 删除redis当天验证码通过的ip
     *
     */
    public void removeCodePassInIp(){
        redisService.deleteObject(RedisConstants.CHECK_CODE_IP);
    }

    /**
     * 每天定时修改标签的点击量
     *
     */
    public void updateTagsClickVolume(){
        Map<String, Object> map = redisService.getCacheMap(TAG_CLICK_VOLUME);
        List<Tags> tagsList = new ArrayList<>();
        for (Map.Entry<String, Object> stringEntry : map.entrySet()) {
            String id = stringEntry.getKey();
            Integer value = (Integer) stringEntry.getValue() + new Random().nextInt(10);
            Tags tags = new Tags(Long.parseLong(id),value);
            tagsList.add(tags);
        }
        tagsService.updateBatchById(tagsList);
    }
}
