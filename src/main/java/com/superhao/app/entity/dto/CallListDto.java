package com.superhao.app.entity.dto;

import com.superhao.app.util.Cn2Spell;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @Auther: super
 * @Date: 2019/11/9 19:31
 * @email:
 */
@Getter
@Setter
public class CallListDto {

    String roomId;
    String filePath;
    String nickName;
    String remark;
    String receiver;
    String nameLetter;
    String type;//联系人的类型
    int age;
    String sex;
    String account;
    List<ChatData> chatRecords;

    public CallListDto() {

    }

    public CallListDto(String remark,String nickname, String filePath, String roomId,String receiver, List<ChatData> chatRecords) {
        this.remark = remark;
        this.roomId = roomId;
        this.filePath = filePath;
        this.nickName = nickname;
        this.receiver = receiver;
        this.chatRecords = chatRecords;
        this.nameLetter =  Cn2Spell.converterToFirstSpell(this.nickName).toUpperCase();
    }
}
