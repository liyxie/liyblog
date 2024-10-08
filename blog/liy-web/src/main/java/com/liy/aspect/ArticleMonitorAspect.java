package com.liy.aspect;

import com.liy.exception.BusinessException;
import com.liy.service.RedisService;
import com.liy.utils.IpUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import static com.liy.common.RedisConstants.ARTICLE_READING;
import static com.liy.common.ResultCode.PARAMS_ILLEGAL;

/**
 * @Author LiY
 * 文章监听，处理阅读量
 */

@Aspect
@Component
@RequiredArgsConstructor
public class ArticleMonitorAspect {
    private static final Logger logger = LoggerFactory.getLogger(ArticleMonitorAspect.class);

    private final RedisService redisService;


    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.liy.annotation.ArticleMoniter)")
    public void questionViewPointCut(){}

    /**
     * 记录用户id到redis阅读量，定时统计到数据库；
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("questionViewPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object obj = joinPoint.proceed();
        try {
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0 && args[0] instanceof Integer) {
                Integer id = (Integer) args[0];
                //增加文章阅读量
                redisService.incrArticle(id.longValue(),ARTICLE_READING, IpUtil.getIp());
            }
            else {
                throw new BusinessException(PARAMS_ILLEGAL);
            }
        }catch (Exception e){
            e.fillInStackTrace();
        }
        return obj;
    }




}
