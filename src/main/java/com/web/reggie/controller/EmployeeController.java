package com.web.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.web.reggie.common.R;
import com.web.reggie.entity.Employee;
import com.web.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @Description
 * @Author ZhangAohui
 * @Date 2023-04-26 20:02
 * @Description:
 */
@Slf4j
@Controller
@RestController("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登陆
     * @param httpServletRequest
     * @param employee  传入的参数 实体类
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest httpServletRequest, @RequestBody Employee employee){
//        1.首先对页面提交的密码进行MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
//        2.验证数据库是否存在该用户
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
//        如果查询为空，返回登陆失败
        if(emp==null){
            return R.error("登陆失败,账户不存在");
        }
        if(!emp.getPassword().equals(password)){
            return R.error("登陆失败，密码错误");
        }
        if(emp.getStatus()==0){
            return R.error("账号已经被禁用");
        }
        httpServletRequest.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest httpServletRequest){
//        溢出session
        httpServletRequest.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
}
