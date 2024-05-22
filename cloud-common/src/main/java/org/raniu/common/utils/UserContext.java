package org.raniu.common.utils;

import lombok.Data;

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
    public static Long getUser() {return threadLocal.get().getUser();}
    public static Integer getPermissions() {return threadLocal.get().getPermissions();}
    public static void setUserId(Long user, Integer permissions) {
        tv t = new tv();
        t.setUser(user);
        t.setPermissions(permissions);
        threadLocal.set(t);}
    public static void removeUserId() {threadLocal.remove();}

    @Data
    private static class tv{
        Long user;
        Integer permissions;

    }
}
