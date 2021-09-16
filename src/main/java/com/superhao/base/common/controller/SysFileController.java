package com.superhao.base.common.controller;

import java.util.Arrays;
import java.util.Map;


import com.superhao.base.common.dto.SysTips;
import com.superhao.base.common.service.ISysFileService;
import com.superhao.base.entity.HttpRequestData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;


/**
 *
 *
 *
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-05-05 12:58:07
 *
 */
@RestController
@RequestMapping("/sys/sysFile")
public class SysFileController extends BaseController{
    @Resource(name = "sysFileService")
    private ISysFileService sysFileService;

    @RequestMapping(value = "singleUpload",method = RequestMethod.POST)
    public Map singleUpload(HttpServletRequest request){
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
