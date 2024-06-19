package org.raniu.api.configuration;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.api.configuration
 * @className: FeignConfig
 * @author: Raniu
 * @description: TODO
 * @date: 2024/6/19 14:16
 * @version: 1.0
 */

@Configuration
public class FeignConfig {

    @Bean("requestInterceptor")
    public RequestInterceptor requestInterceptor() {

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (requestAttributes != null) {
                    HttpServletRequest request = requestAttributes.getRequest();
                    if (request != null) {
                        template.header("Cookie", request.getHeader("Cookie"));
                        template.header("user", request.getHeader("user"));
                        template.header("permission", request.getHeader("permission"));
                        template.header("auth", request.getHeader("auth"));
                    }
                }
            }
        };

        return requestInterceptor;
    }

}
