package com.superhao.app.simplecalculate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Auther: super
 * @Date: 2019/10/13 14:53
 * @email:
 */

@Controller
@RequestMapping("/message")
public class MessageController {

  /*  //websocket服务层调用类
    @Autowired
    private WSMessageService wsMessageService;

    //请求入口
    @RequestMapping(value="/TestWS",method= RequestMethod.GET)
    @ResponseBody
    public String TestWS(@RequestParam(value="userId",required=true) String userId,
                         @RequestParam(value="message",required=true) String message){
        System.out.println("收到发送请求，向用户{"+userId+"}的消息：{"+message+"}");

        if(wsMessageService.sendToAllTerminal(userId, message)){
            return "发送成功";
        }else{
            return "发送失败";
        }
    }
    @RequestMapping(value="/close",method= RequestMethod.GET)
    @ResponseBody
    public String TestWSClose(@RequestParam(value="userId",required=true) String userId,
                         @RequestParam(value="message",required=true) String message){
        System.out.println("收到发送请求，向用户{"+userId+"}的消息：{"+message+"}");

        if(wsMessageService.sendToAllTerminal(userId, message)){
            return "发送成功";
        }else{
            return "发送失败";
        }
    }*/




}
