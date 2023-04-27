package com.web.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.web.reggie.common.R;
import com.web.reggie.entity.Employee;
import com.web.reggie.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @Description
 * @Author ZhangAohui
 * @Date 2023-04-26 20:02
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/employee")
@Api(tags = "Employee员工管理 API 接口")
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
    @ApiOperation(value = "员工登陆", notes = "目前仅仅是作为测试，所以返回用户全列表")
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

    /**
     * 退出系统
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation(value = "员工退出")
    public R<String> logout(HttpServletRequest httpServletRequest){
//        溢出session
        httpServletRequest.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }

    /**
     * 新增员工
     * @param employee
     * @return
     */
    @PostMapping("/")
    @ApiOperation(value = "添加员工账号")
    public R<String> addEmployee(HttpServletRequest httpServletRequest,  @RequestBody Employee employee){
        log.info("新增员工，员工信息：{}"+employee.toString());
//        设置初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        更新字段数据
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        Long empId =  (Long)httpServletRequest.getSession().getAttribute("employee");
        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 获取员工列表
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    @ApiOperation(value = "获取员工列表")
    public R<Page> getMemberList(int page, int pageSize, String name){
        log.info("page={}, pageSize={}, name={}", page, pageSize, name);
//        构造分页构造器
        Page pageInfo = new Page(page, pageSize);
//        构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
//        添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
//        添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
//        执行查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 修改员工信息
     * @param employee
     * @return
     */
    @PutMapping("/")
    @ApiOperation(value = "修改员工信息")
    public R<String>  updateEmployee(HttpServletRequest httpServletRequest, @RequestBody Employee employee){
        log.info("修改信息"+employee.toString());
        Long empID = (Long) httpServletRequest.getSession().getAttribute("employee");
        employee.setUpdateUser(empID);
        employee.setUpdateTime(LocalDateTime.now());
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }

    @PostMapping("/{id}")
    @ApiOperation(value = "根据员工id查询员工信息")
    public R<Employee> getEmployeeById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
        return R.error("查询结果为空");
    }
}
