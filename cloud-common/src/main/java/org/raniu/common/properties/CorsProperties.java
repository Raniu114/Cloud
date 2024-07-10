package org.raniu.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.common.properties
 * @className: CorsProperties
 * @author: Raniu
 * @description: TODO
 * @date: 2024/7/4 18:14
 * @version: 1.0
 */

@Data
@Component
@ConfigurationProperties(value = "cors")
public class CorsProperties {

    private String mapping = "/**";
    private Long maxAge = 0L;
    private boolean allowCredentials = false;
    private String[] allowedOrigins;
    private String[] allowedHeaders = {"*"};
    private String[] exposedHeaders;
    private String[] allowedMethods = { "GET", "POST"};
    private String[] allowedOriginPatterns;


}
