package org.raniu.common.interceptors;

import org.json.JSONObject;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @projectName: cloud
 * @package: org.raniu.interceptor
 * @className: GetControllerInterceptor
 * @author: Raniu
 * @description: TODO
 * @date: 2023/12/23 14:43
 * @version: 1.0
 */

@Component
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        if (Long.parseLong(request.getHeader("permissions")) < 2){
            JSONObject jsonObject = new JSONObject();
            response.setStatus(403);
            jsonObject.put("status",0);
            jsonObject.put("msg","权限不足");
            response.getWriter().print(jsonObject.toString());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {

    }
}
