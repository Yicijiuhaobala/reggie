package com.web.reggie.common;

/**
 * @Description
 * @Author ZhangAohui
 * @Date 2023-05-04 15:23
 * @Description:    自定义异常
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }
}
