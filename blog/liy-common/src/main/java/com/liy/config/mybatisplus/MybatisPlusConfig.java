package com.liy.config.mybatisplus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.baomidou.mybatisplus.extension.plugins.*;

/**
 * @author blue
 * @description: mybatis-plus分页功能
 * @date 2021/7/19 14:41
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 开启分页插件
     * @date: 2021/7/19 14:52
     */
    @Bean
    public MybatisPlusInterceptor paginationInterceptor() {
//        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
//        // 设置最大分页100条
//        paginationInterceptor.setLimit(100);
//        paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
//        paginationInterceptor.setDbType(DbType.MYSQL);

        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor();
        innerInterceptor.setMaxLimit(100L);
        innerInterceptor.setDbType(DbType.MYSQL);
        innerInterceptor.setOptimizeJoin(true);
        interceptor.addInnerInterceptor(innerInterceptor);

        return interceptor;
    }

    /**
     *  设置自动注入规则
     * @date: 2021/7/19 14:52
     */
    @Bean
    public MetaObjectHandler metaObjectHandler(){
        return new MyMetaObjectHandler();
    }

//    @Bean
//    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
//        return new OptimisticLockerInterceptor();
//    }

    @Bean
    public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor(){
        return new OptimisticLockerInnerInterceptor();
    }
}
