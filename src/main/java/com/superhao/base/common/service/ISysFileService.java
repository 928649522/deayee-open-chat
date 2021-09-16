package com.superhao.base.common.service;

import com.superhao.base.common.entity.SysFile;
import com.superhao.base.common.service.IBaseService;
import com.superhao.base.entity.HttpRequestData;

/**
 * 
 *
 * @author superhao
 * @email 928649522@qq.com
 * @date 2019-05-05 12:52:03
 */
public interface ISysFileService extends IBaseService<SysFile> {

    void singleUpload(HttpRequestData requestData);

    void singleUpdateFile(HttpRequestData requestData);
}

