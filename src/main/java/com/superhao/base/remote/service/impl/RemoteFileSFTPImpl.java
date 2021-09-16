package com.superhao.base.remote.service.impl;

import com.jcraft.jsch.*;
import com.superhao.base.common.constant.SysServiceConfigSet;
import com.superhao.base.log.util.SysLogRecordUtil;
import com.superhao.base.remote.service.IRemoteFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Auther: super
 * @Date: 2019/11/4 20:04
 * @email:
 */
@Service("fileSftp")
@Slf4j
public class RemoteFileSFTPImpl implements IRemoteFileService {

    @Autowired
    private SysServiceConfigSet serviceConfigSet;

    public static final String LINUX_FILE_SEPARATOR="/";

    public RemoteFileSFTPImpl(){

    }

 /*   private void lazyConnect(){
        try {
            if(sftp == null){
                synchronized (this){
                    if(sftp==null){
                        sftp = this.connect(serviceConfigSet.getShareFileHost(),serviceConfigSet.getShareFilePort(),serviceConfigSet.getShareFileUser(),serviceConfigSet.getShareFilePwd());
                    }
                }
            }
        } catch (JSchException e) {
            SysLogRecordUtil.record("SFTP连接错误",e);
            log.warn("SFTP连接错误===>"+SysLogRecordUtil.getStackTrace(e));
        }
    }*/




    @Override
    public void renameFile(String directory, String oldname, String newname) {

    }

    @Override
    public boolean upload(String directory, String uploadFile) {
        ChannelSftp sftp = null;
        Session session = null;
        try {
            Map cons = this.connect();
            sftp = (ChannelSftp) cons.get("channel");
            session = (Session) cons.get("session");
            sftp.cd(directory);
            File file = new File(uploadFile);
            file.setExecutable(true);
            file.setReadable(true);
            file.setWritable(true);
            sftp.put(new FileInputStream(file), file.getName());
            sftp.chmod(0777,directory+LINUX_FILE_SEPARATOR+file.getName());
            return true;
        } catch (Exception e) {
            SysLogRecordUtil.record("SFTP传输错误",e);
            log.warn("SFTP传输错误===>"+SysLogRecordUtil.getStackTrace(e));
        } finally {
            this.logout(sftp,session);
        }

        return false;
    }

    @Override
    public boolean upload(String directory, File file) {
        ChannelSftp sftp = null;
        Session session = null;
        try {
            Map cons = this.connect();
            sftp = (ChannelSftp) cons.get("channel");
            session = (Session) cons.get("session");
            if (!this.exits(directory,sftp)){
                this.mkdirs(directory,sftp);
            }
            sftp.cd(directory);
            file.setExecutable(true);
            file.setReadable(true);
            file.setWritable(true);
            sftp.put(new FileInputStream(file), file.getName());
            sftp.chmod(0777,directory+LINUX_FILE_SEPARATOR+file.getName());
            return true;
        } catch (Exception e) {
            SysLogRecordUtil.record("SFTP传输错误",e);
            log.info("SFTP传输错误===>"+SysLogRecordUtil.getStackTrace(e));
        }finally {
            this.logout(sftp,session);
        }
        return false;
    }

    @Override
    public boolean delete(String directory, String deleteFile) {
        ChannelSftp sftp = null;
        Session session = null;
        try {
              Map cons = this.connect();
             sftp = (ChannelSftp) cons.get("channel");
             session = (Session) cons.get("session");
            sftp.cd(directory);
            sftp.rm(deleteFile);
            return true;
        } catch (Exception e) {
            SysLogRecordUtil.record("SFTP删除出错",e);
            log.warn("SFTP删除出错===>"+SysLogRecordUtil.getStackTrace(e));
        }finally {
            this.logout(sftp,session);
        }
        return false;
    }

    private void logout(ChannelSftp sftp,Session session){
        if(sftp!=null){
            sftp.disconnect();
        }
        if(session!=null){
            session.disconnect();
        }
    }

    @Override
    public boolean mkdirs(String directory, ChannelSftp sftp) {

            //TODO 需要加分布式锁
                String[] dirs = directory.split(LINUX_FILE_SEPARATOR);
                String deep = LINUX_FILE_SEPARATOR;
                for(String fileName:dirs){
                    try {
                        if(StringUtils.isEmpty(fileName)){
                            continue;
                        }
                        if(!exits(deep+fileName,sftp)){
                            sftp.mkdir(deep+fileName);
                            sftp.chmod(0777,deep+fileName);

                        }
                        deep+=(fileName+LINUX_FILE_SEPARATOR);
                    } catch (SftpException e) {
                        return false;
                    }
        }
        return true;
    }
    public boolean exits(String fileName,ChannelSftp sftp){
        try{
            sftp.cd(fileName);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    private Map connect() throws JSchException {
        // 1.声明连接Sftp的通道
        ChannelSftp nChannelSftp = null;
        // 2.实例化JSch
        JSch nJSch = new JSch();
        // 3.获取session
        Session nSShSession = nJSch.getSession(serviceConfigSet.getShareFileUser(), serviceConfigSet.getShareFileHost(), serviceConfigSet.getShareFilePort());
        // 4.设置密码
        nSShSession.setPassword(serviceConfigSet.getShareFilePwd());
        // 5.实例化Properties
        Properties nSSHConfig = new Properties();
        // 6.设置配置信息
        nSSHConfig.put("StrictHostKeyChecking", "no");
        // 7.session中设置配置信息
        nSShSession.setConfig(nSSHConfig);
        // 8.session连接
        nSShSession.connect();
        log.warn("Session已连接");
        // 9.打开sftp通道
        Channel channel = nSShSession.openChannel("sftp");
        channel.connect();
       // nChannelSftp = (ChannelSftp) channel;
        Map map = new HashMap();
        map.put("session",nSShSession);
        map.put("channel",channel);
        return map;
    }

}
