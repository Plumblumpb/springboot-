package com.plumblum.controller;


import com.alibaba.fastjson.JSONObject;
import com.plumblum.message.email.EmailService;
import com.plumblum.utils.MessageUtils;
import entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: cpb
 * @Date: 2019/1/3 10:20
 * @Description:
 */
@RestController
public class MessageController {
    @Autowired
    private EmailService emailService;

    @RequestMapping("/messageTest")
    public void test(@RequestBody MessageUtils messageUtils){
        String  message = JSONObject.toJSONString(messageUtils);
        emailService.sendMessage(message);
    }
}
