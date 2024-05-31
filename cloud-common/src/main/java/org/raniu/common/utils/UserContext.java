package org.raniu.common.utils;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.common.utils
 * @className: UserContext
 * @author: Raniu
 * @description: TODO
 * @date: 2024/4/26 10:42
 * @version: 1.0
 */

public class UserContext {
    private static final ThreadLocal<tv> threadLocal = new ThreadLocal<>();

    public static Long getUser() {
        return threadLocal.get().getUser();
    }

    public static Integer getPermissions() {
        return threadLocal.get().getPermissions();
    }

    public static String getAuth() {
        return threadLocal.get().getAuth();
    }

    public static void setUser(Long user, Integer permissions, String auth) {
        tv t = new tv();
        t.setUser(user);
        t.setPermissions(permissions);
        t.setAuth(auth);
        threadLocal.set(t);
    }

    public static void removeUser() {
        threadLocal.remove();
    }

    @Data
    private static class tv {
        Long user;
        Integer permissions;
        String auth;
    }
}
