package org.raniu.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.raniu.common.utils.TokenUtil;
import org.raniu.gateway.config.TokenProperties;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.common.filter
 * @className: AuthGlobalFilter
 * @author: Raniu
 * @description: TODO
 * @date: 2024/5/17 16:15
 * @version: 1.0
 */

@Component
@RequiredArgsConstructor
public class TokenGlobalFilter implements GlobalFilter, Ordered {

    private final TokenProperties tokenProperties;
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (isExclude(request.getPath().toString()) || "OPTIONS".equals(request.getMethod())) {
            return chain.filter(exchange);
        }
        Map<String, Object> data = new HashMap<>();
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        ServerHttpResponse response = exchange.getResponse();
        if (cookies.isEmpty() || cookies.get("AccessToken").isEmpty()) {
            data.put("msg", "token不能为空");
            data.put("status", -1);
            return jsonResponse(response, HttpStatus.UNAUTHORIZED, data);
        }
        String accessToken = cookies.get("AccessToken").get(0).getValue();
        Map<String, String> user = TokenUtil.verify(accessToken);
        String id = user.get("user");
        if ("-1".equals(id)) {
            data.put("msg", "token过期");
            data.put("status", -2);
            return jsonResponse(response, HttpStatus.UNAUTHORIZED, data);
        } else if ("-2".equals(id)) {
            data.put("msg", "token格式错误");
            data.put("status", -3);
            return jsonResponse(response, HttpStatus.UNAUTHORIZED, data);
        } else if ("-3".equals(id)) {
            data.put("msg", "token签名错误");
            data.put("status", -4);
            return jsonResponse(response, HttpStatus.UNAUTHORIZED, data);
        } else if ("-4".equals(id)) {
            data.put("msg", "token异常");
            data.put("status", -5);
            return jsonResponse(response, HttpStatus.UNAUTHORIZED, data);
        }
        exchange.mutate()
                .request(builder -> builder.header("user", id).header("permissions", user.get("permissions")).header("auth", user.get("auth"))).build();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private boolean isExclude(String path) {
        for (String excludePath : tokenProperties.getExcludePaths()) {
            if (antPathMatcher.match(excludePath, path)) {
                return true;
            }
        }
        return false;
    }

    private static Mono<Void> jsonResponse(ServerHttpResponse resp, HttpStatus status, Object data) {
        String body;
        try {
            body = new ObjectMapper().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
        resp.setStatusCode(status);
        resp.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        DataBuffer buffer = resp.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return resp.writeWith(Flux.just(buffer));
    }
}
