package com.superhao.app.controller;

import com.superhao.app.entity.dto.ChatData;
import com.superhao.app.service.IAppChatFileService;
import com.superhao.app.service.IAppChatMessageService;
import com.superhao.base.common.service.ISysFileService;
import com.superhao.base.entity.HttpRequestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther: super
 * @Date: 2019/10/23 13:04
 * @email:
 */
@RestController
@RequestMapping("app/file")
public class AppChatFileController {
                                                                                                    @Resource(name = "appChatFileService")
    private IAppChatFileService appChatFileService;

    @Autowired
    private IAppChatMessageService appChatMessageService;

    @Resource(name = "sysFileService")
    private ISysFileService sysFileService;


    @RequestMapping(value = "/singleUpload",method = RequestMethod.POST)
    public Map singleUpload(ChatData chatData,HttpServletRequest request){
        HttpRequestData requestData =new HttpRequestData(request);
            appChatFileService.temporarySave(requestData);
            Map result = requestData.response();
            List<String> httpPaths = (List<String>) result.get("data");
            chatData.setCreationTime(new Date());
            chatData.setContent(httpPaths.get(0));
            appChatMessageService.sendDataToRoom(chatData,requestData);
        return requestData.response();
    }
    @RequestMapping(value = "/uploadImage",method = RequestMethod.POST)
    public Map uploadImage(ChatData chatData,HttpServletRequest request){
        HttpRequestData requestData =new HttpRequestData();
        String content = appChatFileService.temporarySaveImage(requestData,chatData);
        if(content!=null){
            chatData.setCreationTime(new Date());
            chatData.setContent(content);
            appChatMessageService.sendDataToRoom(chatData,requestData);
        }

        return requestData.response();
    }
    @RequestMapping(value = "/icoUpload",method = RequestMethod.POST)
    public Map icoUpload(HttpServletRequest request){
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        System.out.println(multipartRequest.getMultiFileMap());
        HttpRequestData requestData =new HttpRequestData(request);
        if(requestData.hasMustUploadParam()){
            if(requestData.hasParam("fileId")){//修改更新文件记录
                sysFileService.singleUpdateFile(requestData);
            }else{//新增上传文件记录
                sysFileService.singleUpload(requestData);
            }
        }

        return requestData.response();
    }


}
