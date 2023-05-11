package com.web.reggie.controller;

import com.web.reggie.common.BaseContext;
import com.web.reggie.common.R;
import com.web.reggie.entity.AddressBook;
import com.web.reggie.service.AddressBookService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author ZhangAohui
 * @Date 2023-05-06 16:39
 * @Description:
 */
@Slf4j
@RestController
@RequestMapping("/addressbook")
@Api(tags = "地址簿管理API接口")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增地址簿
     * @param addressBook
     * @return
     */
    @PostMapping("/")
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        addressBook.setId(BaseContext.getCurrentId());
        log.info("新增地址簿");
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }
}
