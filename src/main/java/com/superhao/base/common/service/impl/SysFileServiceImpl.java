package com.superhao.base.common.service.impl;

import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.common.entity.SysFile;
import com.superhao.base.common.mapper.SysFileMapper;
import com.superhao.base.common.service.ISysFileService;
import com.superhao.base.entity.HttpRequestData;
import com.superhao.base.exception.SystemException;
import com.superhao.base.remote.service.IRemoteFileService;
import com.superhao.base.remote.service.impl.RemoteFileSFTPImpl;
import com.superhao.base.util.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;


@Service("sysFileService")
public class SysFileServiceImpl extends BaseServiceImpl<SysFileMapper, SysFile> implements ISysFileService {

    @Autowired
    private SysFileMapper sysFileMapper;

    @Autowired
    private SysServiceConfigSet configSet;

    @Resource(name = "fileSftp")
    IRemoteFileService remoteFileService;


    @Override
    public void singleUpload(HttpRequestData requestData) {


        try {
            SysFile target =new SysFile();
            this.insertDefaultVal(target);
            MultipartFile clientFile = requestData.getMultipartFile("file");
            String module = requestData.getString("module");
            String fileName = clientFile.getOriginalFilename();

            String fileType = requestData.getString("fileType");
            if(fileType==null) {
                fileType = fileName.substring(fileName.lastIndexOf('.'));
            }
            String newFileName = createUUID()+fileType;



            target.setFileSize(new Long(clientFile.getSize()).intValue());

            //拼装添加模块的路径
            String savePath = configSet.getFileBasePath()
                    + RemoteFileSFTPImpl.LINUX_FILE_SEPARATOR+module;

                String fileMd5 = Md5Util.getFileMD5(clientFile.getInputStream());

            File file =createFilePath(savePath,newFileName);
            file.setExecutable(true);
            file.setReadable(true);//设置可读权限
            file.setWritable(true);//设置可写权限
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(clientFile.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();

           // clientFile.transferTo(file);
            /**
             * TODO 远程共享文件
             */
            if(new Boolean(configSet.getOpenRemoteFile()).booleanValue()){
                String remoteSavePath = configSet.getRemoteFileBasePath()+RemoteFileSFTPImpl.LINUX_FILE_SEPARATOR+module;
                remoteFileService.upload(remoteSavePath,file);
            }


            target.setFileMd5(fileMd5);
            target.setModule(module);
            target.setFileName(fileName);
            target.setFileType(fileType);
            target.setFilePath(newFileName);
            sysFileMapper.insert(target);
            requestData.fillResponseData(target);
        } catch (IOException e) {
            e.printStackTrace();
            throw new SystemException("传输复制文件出错");
        }

    }

    @Override
    public void singleUpdateFile(HttpRequestData requestData) {
        try {
            SysFile target =sysFileMapper.selectById(requestData.getLong("fileId"));
            this.updateDefaultVal(target);
            MultipartFile clientFile = requestData.getMultipartFile("file");
            String module = requestData.getString("module");
            String fileName = clientFile.getOriginalFilename();
            String fileType = requestData.getString("fileType");
            if(fileType==null) {
                fileType = fileName.substring(fileName.lastIndexOf('.'));
            }
            String newFileName = createUUID()+fileType;
            target.setFileSize(new Long(clientFile.getSize()).intValue());

            //拼装添加模块的路径
            String savePath = configSet.getFileBasePath()
                    + RemoteFileSFTPImpl.LINUX_FILE_SEPARATOR+module;


            String oldFilePath = savePath +RemoteFileSFTPImpl.LINUX_FILE_SEPARATOR+target.getFilePath();

            String fileMd5 = Md5Util.getFileMD5(clientFile.getInputStream());
            File file =createFilePath(savePath,newFileName);
            file.setExecutable(true);
            file.setReadable(true);//设置可读权限
            file.setWritable(true);//设置可写权限
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(clientFile.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();

            deleteFile(oldFilePath);

            /**
             * TODO 远程共享文件
             */
            if(new Boolean(configSet.getOpenRemoteFile()).booleanValue()){
                String remoteSavePath = configSet.getRemoteFileBasePath()+RemoteFileSFTPImpl.LINUX_FILE_SEPARATOR+module;
                remoteFileService.upload(remoteSavePath,file);
                remoteFileService.delete(remoteSavePath,target.getFilePath());
            }

            target.setFileMd5(fileMd5);
            target.setModule(module);
            target.setFileName(fileName);
            target.setFileType(fileType);
            target.setFilePath(newFileName);
            sysFileMapper.updateById(target);
            requestData.fillResponseData(target);
        } catch (IOException e) {
            e.printStackTrace();
            throw new SystemException("传输复制文件出错");
        }
    }
    private  void deleteFile(String path){
        File target = new File(path);
        if(target.exists()){
            target.delete();
        }
    }



    public static void main(String[] args) throws FileNotFoundException {
        InputStream fi = new FileInputStream("D://sod.txt");
        FileInputStream i = (FileInputStream) fi;
System.out.println("");
    }

}