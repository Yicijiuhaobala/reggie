package com.web.reggie.controller;

/**
 * @Description
 * @Author ZhangAohui
 * @Date 2023-05-06 15:23
 * @Description:
 */

import com.web.reggie.common.R;
import com.web.reggie.common.VerCodeGenerateUtil;
import com.web.reggie.entity.Email;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * TODO 邮箱验证码
 *
 * @author DB
 * <br>CreateDate 2021/9/13 0:35
 */
@RestController
@RequestMapping("email")
@Api(tags = "发送邮件API")
public class EmailController {
    //	引入邮件接口

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private MailSender mailSender;

    //	获得发件人信息
    @Value("${spring.mail.username}")
    private String from;

    public EmailController() {
    }

    @GetMapping("/sendEmail")
    public R<String> commonEmail(Email Email, HttpServletRequest request) {
//        创建邮件消息
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(from);

        message.setTo(Email.getTos());

        message.setSubject("您本次的验证码是");

        String verCode = VerCodeGenerateUtil.generateVerCode(6);
        message.setText("尊敬的xxx,您好:\n"
                + "\n本次请求的邮件验证码为:" + verCode + ",本验证码 5 分钟内效，请及时输入。（请勿泄露此验证码）\n"
                + "\n如非本人操作，请忽略该邮件。\n(这是一封通过自动发送的邮件，请不要直接回复）");

//        javaMailSender.send(message);
        mailSender.send(message);
        return R.success("发送成功");
    }
}

