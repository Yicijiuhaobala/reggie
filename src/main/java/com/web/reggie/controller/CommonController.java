package com.web.reggie.controller;

import com.web.reggie.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @Description
 * @Author ZhangAohui
 * @Date 2023-05-04 16:19
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/common")
@Api(tags = "文件上传下载API接口")
public class CommonController {
    @Resource
    private RedisTemplate redisTemplate;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    @ApiOperation(value = "文件上传")
    public R<String> upload(MultipartFile file){
        log.info("上传文件，{}", file.toString());
//        获取文件名
        String fileName  = file.getOriginalFilename();
//        获取文件后缀名
        String suffix = fileName.substring(fileName.lastIndexOf('.'));
        fileName = UUID.randomUUID() + suffix;
        File dir = new File(basePath);
        if(!dir.exists()){
//            如果目录不存在，新建目录
            dir.mkdirs();
        }
        try {
//            将文件中转存到临时文件中
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param httpServletResponse
     * @return
     */
    @GetMapping("/download")
    @ApiOperation(value = "文件下载")
    public void download(String name, HttpServletResponse httpServletResponse){
        FileInputStream fileInputStream = null;
        ServletOutputStream servletOutputStream = null;
        try {
            fileInputStream = new FileInputStream(new File(basePath+name));
            servletOutputStream = httpServletResponse.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            httpServletResponse.setContentType("image/jpeg");
            while ((len = fileInputStream.read(bytes))!=-1){
                servletOutputStream.write(bytes, 0, len);
                servletOutputStream.flush();
            }
            fileInputStream.close();
            servletOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
//            return R.error("文件下载出错");
        }
//        return R.success(name + "文件下载成功");
    }

    @GetMapping("/saveStu")
    public void setStudent(){
        redisTemplate.opsForValue().set("st2","6663");
        System.out.println(redisTemplate.opsForValue().get("name"));
    }
}
