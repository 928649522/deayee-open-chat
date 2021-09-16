package com.superhao.app.service.impl;

import com.superhao.app.entity.dto.ChatData;
import com.superhao.app.service.IAppChatFileService;
import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.common.entity.SysFile;
import com.superhao.base.common.mapper.SysFileMapper;
import com.superhao.base.common.service.ISysFileService;
import com.superhao.base.common.service.impl.BaseServiceImpl;
import com.superhao.base.common.service.impl.SysFileServiceImpl;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.remote.service.IRemoteFileService;
import com.superhao.base.remote.service.impl.RemoteFileSFTPImpl;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: super
 * @Date: 2019/10/23 13:14
 * @email:
 */
@Log4j
@Service("appChatFileService")
public class AppChatFileServiceImpl extends BaseServiceImpl<SysFileMapper, SysFile> implements IAppChatFileService{

    @Autowired
    private SysServiceConfigSet configSet;
    @Autowired
    private IRemoteFileService remoteFileService;

    private static final String APP_PATH = "APP"+RemoteFileSFTPImpl.LINUX_FILE_SEPARATOR;
    private static final long FILE_MAX_SIZI = 1024*1024*2L;//2兆
    private static final int UPLOAD_SIZE = 5;//








    /**
     * 临时存储会话文件
     * 按天数生成一个不同的文件夹
     * @param requestData
     */
    @Override
    public void temporarySave(HttpRequestData requestData) {
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date()).toString();
        String savePath = configSet.getFileBasePath()+RemoteFileSFTPImpl.LINUX_FILE_SEPARATOR+APP_PATH+folderName;

        String basePath = RemoteFileSFTPImpl.LINUX_FILE_SEPARATOR+APP_PATH+folderName+RemoteFileSFTPImpl.LINUX_FILE_SEPARATOR;
        List<String> httpPaths = new ArrayList<>();
        if(requestData.getFileMap().size()>UPLOAD_SIZE){
            requestData.createErrorResponse(SysTips.APP_UPLOAD_FILE_SIZE_MAX);
            return;
        }
        for(Map.Entry<String, List<MultipartFile>> item: requestData.getFileMap().entrySet()){
            for(MultipartFile target:item.getValue()){
                if(target.getSize()>FILE_MAX_SIZI){
                    requestData.createErrorResponse(SysTips.APP_UPLOAD_FILE_SIZE_MAX);
                    return;
                }
                String fileType = "";
                if(target.getContentType().contains("image")){
                     fileType = '.'+target.getContentType().substring(target.getContentType().lastIndexOf('/')+1);
                }
                String fileName =createUUID()+fileType;

                try {
                    File file = super.createFilePath(savePath,fileName);
                    file.setExecutable(true);
                    file.setReadable(true);//设置可读权限
                    file.setWritable(true);//设置可写权限
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    fileOutputStream.write(target.getBytes());
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    /**
                     * TODO 远程共享文件
                     */
                    if(new Boolean(configSet.getOpenRemoteFile()).booleanValue()){
                        String remoteSavePath = configSet.getRemoteFileBasePath()+RemoteFileSFTPImpl.LINUX_FILE_SEPARATOR+APP_PATH+folderName;
                        remoteFileService.upload(remoteSavePath,file);
                    }
                } catch (IOException e) {
                    log.error("APP上传文件错误");
                    e.printStackTrace();
                }
                httpPaths.add(basePath+fileName);
            }
        }
        requestData.fillResponseData(httpPaths);
    }



    @Override
    public String temporarySaveImage(HttpRequestData requestData, ChatData chatData) {
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date()).toString();
        String savePath = configSet.getFileBasePath()+RemoteFileSFTPImpl.LINUX_FILE_SEPARATOR+APP_PATH+folderName;
        String basePath = File.separator+APP_PATH+folderName+File.separator;

        String fileName =createUUID()+".jpg";
        super.createFilePath(savePath,fileName);
        if(base64StrToImage(chatData.getContent().toString(),savePath+File.separator+fileName)){
            return basePath+fileName;
        }
        requestData.createErrorResponse(SysTips.PARAM_ERROR);
        return null;
    }
    public  boolean base64StrToImage(String imgStr, String path) {
        if (imgStr == null)
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // 解密
            byte[] b = decoder.decodeBuffer(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            File tempFile = new File(path);

            OutputStream out = new FileOutputStream(tempFile);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
