package com.superhao.app.handle;

import com.superhao.app.entity.dto.ChatData;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Auther: super
 * @Date: 2019/11/19 17:16
 * @email:
 */
@Component
public class RoomMessageHelper {

    private static ConcurrentHashMap ROOM_MANAGER =new ConcurrentHashMap();

    public static void main(String[] args) {
        BlockingQueue<ChatData> blockingQueue = new LinkedBlockingQueue();
        ChatData chatData = new ChatData("1");
        blockingQueue.add(chatData);
        chatData.setType("2");
        blockingQueue.add(chatData);
        System.out.println(blockingQueue.poll().getType());
        System.out.println(blockingQueue.poll().getType());
        System.out.println(blockingQueue.poll());
    }
}
