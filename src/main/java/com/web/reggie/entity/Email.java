package com.web.reggie.entity;

/**
 * @Description
 * @Author ZhangAohui
 * @Date 2023-05-06 15:20
 * @Description:
 */
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * TODO 邮箱验证码实体类
 *
 * @author DB
 * <br>CreateDate 2021/9/13 0:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email implements Serializable {

    //    邮件接收方
    private String[] tos;
    //    邮件主题
    private String subject;
    //    邮件内容
    private String content;
}

