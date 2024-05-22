package org.raniu.gateway.route;

import cn.hutool.json.JSONUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.gateway.route
 * @className: DynamicRouteLoader
 * @author: Raniu
 * @description: TODO
 * @date: 2024/4/26 16:09
 * @version: 1.0
 */

@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicRouteLoader {

    private final NacosConfigManager nacosConfigManager;
    private final RouteDefinitionWriter writer;
    private final Set<String> ids = new HashSet<>();

    private static final String DATA_ID = "iot-gateway.json";
    private static final String GROUP = "DEFAULT_GROUP";

    @PostConstruct
    public void initRouteConfigListener() throws NacosException {
        String config = nacosConfigManager.getConfigService().getConfigAndSignListener(DATA_ID, GROUP, 5000, new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String s) {
                updateRoute(s);
            }
        });
        updateRoute(config);
    }

    private void updateRoute(String config) {
        List<RouteDefinition> routeDefinitions = JSONUtil.toList(config, RouteDefinition.class);

        ids.forEach(id -> writer.delete(Mono.just(id)));
        ids.clear();

        routeDefinitions.forEach(routeDefinition -> {
            writer.save(Mono.just(routeDefinition));

            ids.add(routeDefinition.getId());
        });
    }

}
