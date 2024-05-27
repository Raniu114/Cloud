package org.raniu.common.configuration;

import org.raniu.common.interceptors.AdminInterceptor;
import org.raniu.common.interceptors.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.common.configuration
 * @className: MvcConfiguration
 * @author: Raniu
 * @description: TODO
 * @date: 2024/5/24 14:51
 * @version: 1.0
 */

@Configuration
@ConditionalOnClass(DispatcherServlet.class)
public class MvcConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new UserInterceptor())// 指定拦截器
                .addPathPatterns("/**")
                .excludePathPatterns("/**/login/**"
                        ,"/**/token"
                        ,"/doc.html/**"
                        ,"/RSA/**"
                        ,"/favicon.ico"
                        ,"/error"
                        ,"/swagger-ui/**"
                        ,"/swagger-resources/**"
                        ,"/webjars/**"
                        ,"/v3/**"
                        ,"/api/**"
                        ,"/swagger-ui.html/**");
        registry.addInterceptor(new AdminInterceptor())
                .addPathPatterns("/**/add","/**/delete","/**/update","/user/list","/user/select")
                .excludePathPatterns(
                        "/strategy/**"
                        ,"/doc.html/**"
                        ,"/swagger-resources/**"
                        ,"/swagger-ui/**"
                        ,"/favicon.ico"
                        ,"/error"
                        ,"/webjars/**"
                        ,"/v3/**"
                        ,"/api/**"
                        ,"/swagger-ui.html/**");
    }
}
