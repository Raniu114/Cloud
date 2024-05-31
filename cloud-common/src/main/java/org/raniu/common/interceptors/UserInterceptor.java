package org.raniu.common.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.raniu.common.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.common.interceptors
 * @className: UserInterceptor
 * @author: Raniu
 * @description: TODO
 * @date: 2024/5/24 14:34
 * @version: 1.0
 */

public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UserContext.setUser(Long.parseLong(request.getHeader("user")), request.getIntHeader("permission"), request.getHeader("auth"));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.removeUser();
    }
}
