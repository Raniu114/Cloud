package org.raniu.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.raniu.api.enums.ResultCode;
import org.raniu.api.vo.Result;
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

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        if (isExclude(request.getPath().toString()) || "OPTIONS".equals(request.getMethod())) {
            return chain.filter(exchange);
        }
        Map<String, Object> data = new HashMap<>();
        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        ServerHttpResponse response = exchange.getResponse();
        if (cookies.isEmpty() || cookies.get("AccessToken") == null) {
            return jsonResponse(response, HttpStatus.UNAUTHORIZED, Result.error(ResultCode.MISSING, "token不能为空"));
        }
        String accessToken = cookies.get("AccessToken").get(0).getValue();
        Map<String, String> user = TokenUtil.verify(accessToken);
        String id = user.get("user");
        if ("-1".equals(id)) {
            return jsonResponse(response, HttpStatus.UNAUTHORIZED, Result.error(ResultCode.TOKEN_TIMEOUT, "token过期"));
        } else if ("-2".equals(id) || "-3".equals(id) || "-4".equals(id)) {
            return jsonResponse(response, HttpStatus.UNAUTHORIZED, Result.error(ResultCode.TOKEN_ERROR, "token错误"));
        }
        exchange.mutate()
                .request(builder -> builder.header("user", id).header("permission", user.get("permission")).header("auth", user.get("auth"))).build();
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

    private static Mono<Void> jsonResponse(ServerHttpResponse resp, HttpStatus status, Result<String> data) throws JsonProcessingException {
        resp.setStatusCode(status);
        resp.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper objectMapper = new ObjectMapper();
        DataBuffer buffer = resp.bufferFactory().wrap(objectMapper.writeValueAsBytes(data));
        return resp.writeWith(Flux.just(buffer));
    }
}
