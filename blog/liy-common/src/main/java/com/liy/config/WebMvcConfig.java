package com.liy.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import com.liy.config.intercept.PageableInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @author blue
 * @date 2022/3/10
 * @apiNote
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer  {

    @Value("${file.upload-folder}")
    private String UPLOAD_FOLDER;


    /**
     * 注册sa-token的拦截器，打开注解式鉴权功能 (如果您不需要此功能，可以删除此类)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //分页拦截器
        registry.addInterceptor(new PageableInterceptor());

        // 注册Sa-Token的路由拦截器
        registry.addInterceptor(new SaRouteInterceptor()).addPathPatterns("/system/**").excludePathPatterns("/login","/logout","/verify");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

            registry.addResourceHandler("/webjars/**").addResourceLocations(
                    "classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/img/**").addResourceLocations("fileUpload:" + UPLOAD_FOLDER);
    }

    /**
     * 注册跨域信息
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*") // 允许所有跨域地址
                .allowedHeaders("*")
                .allowedMethods("*")
                .maxAge(3600);
    }

}
