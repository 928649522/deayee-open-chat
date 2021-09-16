package com.superhao.part_time_job.file;


import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * @Auther: super
 * @Date: 2019/11/3 15:012
 * @email:
 */
public class RemoteFile {

    static ChannelSftp sftp;
    public static void main(String[] args) throws JSchException, SftpException {
        SftpUtil util = new SftpUtil();
        sftp= util.connect("119.29.100.144",22,"root","haogezaiCI123");

        long start = System.currentTimeMillis();



        //util.delete("/baseproject/","zehao.txt",chan);


        //chan.mkdir("/baseproject/base2");
       // chan.chmod(0777,"/baseproject/base");
        mkdirs("/baseproject1/hehehe/hehehe");

       // util.upload("/baseproject/","D:/base",chan);

       /* while(true){
            util.upload("/baseproject","D:/zehao.txt",chan);
            System.out.println("用时："+(System.currentTimeMillis()-start)+"ms");
            start = System.currentTimeMillis();
        }*/
        sftp.disconnect();

    }


    public static boolean mkdirs(String directory) {
        boolean exits = true;

        if(exits){
            //TODO 需要加分布式锁
                String[] dirs = directory.split("/");
                String deep = File.separator;
                for(String fileName:dirs){
                    try {
                        if(StringUtils.isEmpty(fileName)){
                            continue;
                        }
                        if(!exits(deep+fileName)){
                            sftp.mkdir(deep+fileName);
                            sftp.chmod(0777,deep+fileName);
                            deep+=(fileName+File.separator);
                           // System.out.println(deep);
                        }
                    } catch (SftpException e) {
                        return false;
                    }
            }
        }
        return true;
    }
    public static boolean exits(String fileName){
        try{
            sftp.cd(fileName);
        }catch (Exception e){
            return false;
        }
        return true;
    }


}
