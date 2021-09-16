package com.superhao.app.controller;

import com.superhao.app.entity.dto.ChatData;
import com.superhao.app.handle.listener.RoomMessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

/**
 * @Auther: super
 * @Date: 2019/11/7 11:12
 * @email:
 */
@Controller
@RequestMapping("/redis")
public class Test {

    @Resource
    private RedisMessageListenerContainer redisMessageListenerContainer;

    @Resource
    private RedisTemplate redisTemplate;

    @RequestMapping("/add")
    public void add(@RequestParam String id){
        RoomMessageListener subscribeListener = new RoomMessageListener();

        //设置订阅topic
        redisMessageListenerContainer.addMessageListener(subscribeListener, new ChannelTopic(id));
    }

    @RequestMapping("/put")
    public void put(@RequestParam String id,@RequestParam String msg){
        redisTemplate.opsForHash();
        ChatData cd = new ChatData("sender","reser","电脑房分内事念佛");
        redisTemplate.convertAndSend(id,cd);
    }


}
