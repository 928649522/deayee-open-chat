package com.superhao.part_time_job.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import lombok.extern.slf4j.Slf4j;

/**
 * @Auther: super
 * @Date: 2019/11/4 19:39
 * @email:
 */
@Slf4j
public class SftpUtil {
    /**
     *
     * @param host
     * @param port
     * @param username
     * @param password
     * @return
     * @throws JSchException
     */
    public ChannelSftp connect(String host, int port, String username, String password) throws JSchException {
        // 1.声明连接Sftp的通道
        ChannelSftp nChannelSftp = null;
        // 2.实例化JSch
        JSch nJSch = new JSch();
        // 3.获取session
        Session nSShSession = nJSch.getSession(username, host, port);
       log.info("Session创建成功");
        // 4.设置密码
        nSShSession.setPassword(password);
        // 5.实例化Properties
        Properties nSSHConfig = new Properties();
        // 6.设置配置信息
        nSSHConfig.put("StrictHostKeyChecking", "no");
        // 7.session中设置配置信息
        nSShSession.setConfig(nSSHConfig);
        // 8.session连接
        nSShSession.connect();
        log.info("Session已连接");
        // 9.打开sftp通道
        Channel channel = nSShSession.openChannel("sftp");
        // 10.开始连接
        channel.connect();
        nChannelSftp = (ChannelSftp) channel;
        log.info("连接到主机" + host + ".");
        return nChannelSftp;
    }

    /**
     * 文件重命名
     * @param directory
     * @param oldname
     * @param newname
     * @param sftp
     */
    public void renameFile(String directory, String oldname, String newname,ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rename(oldname, newname);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上传文件
     * @param directory
     * @param uploadFile
     * @param sftp
     */
    public void upload(String directory, String uploadFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(uploadFile);
            if(!file.exists()){
                file.mkdirs();
            }
            sftp.put(new FileInputStream(file), file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     * @param directory
     * @param downloadFile
     * @param saveFile
     * @param sftp
     */
    public void download(String directory, String downloadFile,String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file = new File(saveFile);
            sftp.get(downloadFile, new FileOutputStream(file));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     * @param directory
     * @param deleteFile
     * @param sftp
     */
    public void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
            log.info("删除成功");
        } catch (Exception e) {
            log.info("删除失败");
            e.printStackTrace();
        }
    }

    /**
     * 列出列表下的文件
     * @param directory
     * @param sftp
     * @return
     * @throws SftpException
     */
    public Vector listFiles(String directory, ChannelSftp sftp)throws SftpException {
        return sftp.ls(directory);
    }

    /**
     * 下载文件夹下面的所有文件
     *
     * @param viDirectory
     * @param viHost
     * @param viPort
     * @param viUserName
     * @param viPassWord
     * @param viSaveDir
     * @return
     */
    public List<String> downloadDirFile(String viDirectory, String viHost, int viPort, String viUserName, String viPassWord, String viSaveDir) {
        ChannelSftp nChannelSftp = null;
        List<String> nFileNameList = null;
        try {
            // 1.实例化nSftpUtil工具类
            SftpUtil nSftpUtil = new SftpUtil();
            // 2.建立Sftp通道
            nChannelSftp = nSftpUtil.connect(viHost, 22, viUserName, viPassWord);
            // 3.获取目录下面所有文件
            Vector nVector = nChannelSftp.ls(viDirectory);
            // 4.循环遍历文件
            for (int i = 0; i < nVector.size(); i++) {
                // 5.进入服务器文件夹
                nChannelSftp.cd(viDirectory);
                // 6.实例化文件对象
                String nFileName = nVector.get(i).toString().substring(56, nVector.get(i).toString().length());
                if (!nFileName.contains("csv")) {
                    continue;
                }
                File nFile = new File(viSaveDir + File.separator + nFileName);
                // 7.下载文件
                nChannelSftp.get(nFileName, new FileOutputStream(nFile));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            nChannelSftp.disconnect();
        }
        return nFileNameList;
    }

    public static void main(String[] args) throws JSchException {
        SftpUtil sf = new SftpUtil();


    }

}