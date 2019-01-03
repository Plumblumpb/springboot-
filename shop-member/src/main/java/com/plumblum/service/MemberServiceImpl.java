package com.plumblum.service;

import com.alibaba.fastjson.JSONObject;
import com.plumblum.base.BaseApiService;
import com.plumblum.base.BaseRedisService;
import com.plumblum.base.ResponseBase;
import com.plumblum.constants.Constants;
import com.plumblum.utils.MD5Util;
import utils.MessageUtils;
import com.plumblum.utils.TokenUtils;
import com.plumblum.dao.MemberDao;
import com.plumblum.mq.RegisterMailboxProducer;
import entity.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import service.MemberService;

import java.util.Date;
import java.util.UUID;

/**
 * Created with IDEA
 * author:plumblum
 * Date:2018/12/23
 * Time:22:38
 */
@Slf4j
@RestController
public class MemberServiceImpl extends BaseApiService implements MemberService{
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private RegisterMailboxProducer registerMailboxProducer;

    @Value("${messages.queue}")
    private String MESSAGESQUEUE;

    @Autowired
    private BaseRedisService baseRedisService;
    @Override
    public ResponseBase findUserById(Long userId) {
        UserEntity user = memberDao.findByID(userId);
        if (user == null) {
            return setResultError("未查找到用户信息.");
        }
        return setResultSuccess(user);
    }

    @Override
    public ResponseBase regUser(@RequestBody UserEntity user) {
        // 参数验证
        String password = user.getPassword();
        if (StringUtils.isEmpty(password)) {
            return setResultError("密码不能为空.");
        }
        String newPassword = MD5Util.MD5(password);
        user.setPassword(newPassword);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        Date  date = new Date();
        user.setId(uuid);
        user.setCreated(date);
        user.setUpdated(date);
        Integer result = memberDao.insertUser(user);
        if (result <= 0) {
            return setResultError("注册用户信息失败.");
        }
        // 采用异步方式发送消息
        MessageUtils messageUtils = new MessageUtils();
        messageUtils.setType("email");
        String json = JSONObject.toJSONString(messageUtils);
        log.info("####会员服务推送消息到消息服务平台####json:{}", json);
        registerMailboxProducer.sendMessage(MESSAGESQUEUE, json);
        return setResultSuccess("用户注册成功.");
    }




    @Override
    public ResponseBase login(@RequestBody UserEntity user) {
        // 1.验证参数
        String username = user.getUsername();
        if (StringUtils.isEmpty(username)) {
            return setResultError("用戶名称不能为空!");
        }
        String password = user.getPassword();
        if (StringUtils.isEmpty(password)) {
            return setResultError("密码不能为空!");
        }
        // 2.数据库查找账号密码是否正确
        String newPassWrod = MD5Util.MD5(password);
        UserEntity userEntity = memberDao.login(username, newPassWrod);
        if (userEntity == null) {
            return setResultError("账号或者密码不能正确");
        }
        // 3.如果账号密码正确，对应生成token
        String memberToken = TokenUtils.getMemberToken();
        // 4.存放在redis中，key为token value 为 userid
        String userId = userEntity.getId();
        log.info("####用户信息token存放在redis中... key为:{},value", memberToken, userId);
        baseRedisService.setString(memberToken, userId + "", Constants.TOKEN_MEMBER_TIME);
        // 5.直接返回token
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("memberToken", memberToken);
        return setResultSuccess(jsonObject);
    }

    @Override
    public ResponseBase findByTokenUser(String token) {
        // 1.验证参数
        if (StringUtils.isEmpty(token)) {
            return setResultError("token不能为空!");
        }
        // 2.从redis中 使用token 查找对应 userid
        String strUserId = (String) baseRedisService.getString(token);
        if (StringUtils.isEmpty(strUserId)) {
            return setResultError("token无效或者已经过期!");
        }
        // 3.使用userid数据库查询用户信息返回给客户端
        Long userId = Long.parseLong(strUserId);
        UserEntity userEntity = memberDao.findByID(userId);
        if (userEntity == null) {
            return setResultError("为查找到该用户信息");
        }
        userEntity.setPassword(null);
        return setResultSuccess(userEntity);
    }

}
