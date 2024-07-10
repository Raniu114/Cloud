package org.raniu.common.configuration;

import org.raniu.common.properties.CorsProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @projectName: cloud
 * @package: org.raniu.configuration
 * @className: CorsConfig
 * @author: Raniu
 * @description: TODO
 * @date: 2023/12/22 14:11
 * @version: 1.0
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Autowired
    private CorsProperties corsProperties;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        CorsRegistration corsRegistration = registry.addMapping(corsProperties.getMapping());
        corsRegistration.allowCredentials(corsProperties.isAllowCredentials());
        corsRegistration.maxAge(corsProperties.getMaxAge() > 0 ? corsProperties.getMaxAge() : 0);
        corsRegistration.allowedHeaders(corsProperties.getAllowedHeaders());
        corsRegistration.allowedMethods(corsProperties.getAllowedMethods());
        if (corsProperties.getExposedHeaders() != null) {
            corsRegistration.exposedHeaders(corsProperties.getExposedHeaders());
        }
        if (corsProperties.getAllowedOriginPatterns() != null) {
            corsRegistration.allowedOriginPatterns(corsProperties.getAllowedOriginPatterns());
        }else if (corsProperties.getAllowedOrigins() != null) {
            corsRegistration.allowedOrigins(corsProperties.getAllowedOrigins());
        }
    }
}