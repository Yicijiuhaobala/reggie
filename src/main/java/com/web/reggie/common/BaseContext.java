package com.web.reggie.common;

/**
 * @Description  基于ThreadLocal封装工具类，用来保存和获取当前登陆用户id
 * @Author ZhangAohui
 * @Date 2023-04-27 21:54
 * @Description:
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
