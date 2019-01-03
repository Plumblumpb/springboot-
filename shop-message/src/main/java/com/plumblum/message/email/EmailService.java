package com.plumblum.message.email;

import com.alibaba.fastjson.JSONObject;
import com.plumblum.message.MessageAdapter;
import utils.MessageUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService implements MessageAdapter {
    @Value("${msg.subject}")
    private String subject;
    @Value("${msg.text}")
    private String text;

    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public void sendMessage(String message) {
        MessageUtils messageUtils = JSONObject.parseObject(message,MessageUtils.class);
        // 处理发送邮件
        //1.获取邮箱账号
        String email=messageUtils.getType();
        if(StringUtils.isEmpty(email) || !email.equals("email")){
            return ;
        }
        log.info("消息服务平台发送邮件:{}");
        //初始化
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        //设置发送内容
        // 来自账号 自己发自己
        simpleMailMessage.setFrom(messageUtils.getUserEntity().getEmail());
        // 发送账号
        simpleMailMessage.setTo(messageUtils.getUserEntity().getEmail());
        // 标题
        simpleMailMessage.setSubject(subject);
        // 内容
        simpleMailMessage.setText(text.replace("{}", email));
        // 发送邮件
        javaMailSender.send(simpleMailMessage);
        log.info("消息服务平台发送邮件:{}完成", email);
        //发送信息
    }
}
