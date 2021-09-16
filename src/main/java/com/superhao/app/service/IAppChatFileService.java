package com.superhao.app.service;

import com.superhao.app.entity.dto.ChatData;
import com.superhao.base.common.entity.SysFile;
import com.superhao.base.common.mapper.SysFileMapper;
import com.superhao.base.common.service.IBaseService;
import com.superhao.base.entity.HttpRequestData;

/**
 * @Auther: super
 * @Date: 2019/10/23 13:13
 * @email:
 */
public interface IAppChatFileService extends IBaseService<SysFile> {
    void temporarySave(HttpRequestData requestData);

    String temporarySaveImage(HttpRequestData requestData, ChatData chatData);


}
