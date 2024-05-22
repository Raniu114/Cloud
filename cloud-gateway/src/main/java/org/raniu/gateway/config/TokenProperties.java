package org.raniu.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.gateway.config
 * @className: TokenProperties
 * @author: Raniu
 * @description: TODO
 * @date: 2024/5/20 15:07
 * @version: 1.0
 */

@Data
@Component
@ConfigurationProperties(prefix = "iot.auth")
public class TokenProperties {
    private List<String> includePaths;
    private List<String> excludePaths;
}
