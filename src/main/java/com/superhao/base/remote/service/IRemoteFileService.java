package com.superhao.base.remote.service;

import com.jcraft.jsch.ChannelSftp;

import java.io.File;

/**
 * @Auther: super
 * @Date: 2019/11/5 00:13
 * @email:
 */
public interface IRemoteFileService {
    void renameFile(String directory, String oldname, String newname);
    boolean upload(String directory, String uploadFile);
    boolean upload(String directory, File uploadFile);
    boolean delete(String directory, String uploadFile);
    boolean mkdirs(String directory, ChannelSftp sftp);
}
