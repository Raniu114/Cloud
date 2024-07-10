package org.raniu.common.interceptors;

import cn.hutool.json.JSONUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.raniu.common.enums.AuthEnum;
import org.raniu.common.utils.UserContext;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.common.interceptors
 * @className: AuthInterceptor
 * @author: Raniu
 * @description: TODO
 * @date: 2024/5/30 17:38
 * @version: 1.0
 */
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String[] uri = request.getRequestURI().split("/");
        cn.hutool.json.JSONObject authJson = JSONUtil.parseObj(UserContext.getAuth());
        if ("POST".equals(request.getMethod())) {
            if ((int) authJson.get(uri[1]) < AuthEnum.READ_WRITE.ordinal()) {
                JSONObject jsonObject = new JSONObject();
                response.setStatus(403);
                jsonObject.put("status", -1);
                jsonObject.put("msg", "权限不足");
                response.getWriter().write(jsonObject.toString());
                return false;
            }
            return true;
        }
        if ("GET".equals(request.getMethod())) {
            if ((int) authJson.get(uri[1]) == AuthEnum.NONE.ordinal()) {
                JSONObject jsonObject = new JSONObject();
                response.setStatus(403);
                jsonObject.put("status", -1);
                jsonObject.put("msg", "权限不足");
                response.getWriter().write(jsonObject.toString());
                return false;
            }
            return true;
        }
        if ("OPTIONS".equals(request.getMethod())) {
            return true;
        }
        return false;
    }
}
