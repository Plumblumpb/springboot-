package com.plumblum.mq;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class RegisterMailboxProducer {
    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    //单点发送queue
    public void sendMessage(String destintion,String value){
        ActiveMQQueue connection = new ActiveMQQueue(destintion);
        jmsMessagingTemplate.convertAndSend(connection,value);

    }
    //topic发送
    public void sendMessageTopic(String destintion,String value){
        ActiveMQTopic activeMQTopic = new ActiveMQTopic(destintion);
        jmsMessagingTemplate.convertAndSend(activeMQTopic,value);
    }
}
