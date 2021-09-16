package com.superhao.app.entity.token;

import com.superhao.app.entity.AppRoom;
import com.superhao.app.entity.AppUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @Auther: super
 * @Date: 2019/10/16 17:27
 * @email:
 */
@Getter
@Setter
@ToString
public class AppUserToken extends AppUser implements Serializable {

    private static final long serialVersionUID = 1249653241391587245L;
    public  int timeoutSeconds; //超时时间:单位秒
    private String tokenCode;
    private long createTime;
    private long refreshTime;
    private Map<String,Integer> sayNumbers;//每个房间的发言次数
    private Set<String> permissionSet;

    public AppUserToken() {
        this.timeoutSeconds = 60000;
        this.createTime = System.currentTimeMillis();
        this.refreshTime = this.createTime;
        this.sayNumbers = new HashMap<>();
    }
    public AppUserToken(String tokenCode) {
        this.timeoutSeconds = 60000;
        this.createTime = System.currentTimeMillis();
        this.refreshTime = this.createTime;
        this.tokenCode = tokenCode;
        this.sayNumbers = new HashMap<>();
    }


    public static void main(String[] args) {
        UUID.randomUUID().toString().replace("-","");
    }





    public static AppUserToken create(AppUser appUser, String tokenCode){
        AppUserToken target = new AppUserToken(tokenCode);
        BeanUtils.copyProperties(appUser,target);
        return target;
    }
    public static AppUserToken create(AppUser appUser){
        String tokenCode = UUID.randomUUID().toString().replace("-","");
        AppUserToken target = new AppUserToken(tokenCode);
        BeanUtils.copyProperties(appUser,target);
        return target;
    }

    public void plusOneSayNumber(String key){
        Integer sayNumber = this.getSayNumbers().get(key);
        if(sayNumber==null){
            this.getSayNumbers().put(key,new Integer(1));
        }else{
            ++sayNumber;
            this.getSayNumbers().put(key,sayNumber);
        }
    }

    public int getSayNumber(AppRoom appRoom) {
        Object sourceId = appRoom.getRoomId();
        if(sourceId==null){
            sourceId = appRoom.getRoomCode();
        }
        Integer res = this.getSayNumbers().get(sourceId.toString());
        return res==null?0:res;
    }
}
