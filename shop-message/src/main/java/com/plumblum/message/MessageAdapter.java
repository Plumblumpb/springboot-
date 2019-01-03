package com.plumblum.message;

import com.alibaba.fastjson.JSONObject;

//消息发送接口
public interface MessageAdapter {

    public void sendMessage(JSONObject body );
}
