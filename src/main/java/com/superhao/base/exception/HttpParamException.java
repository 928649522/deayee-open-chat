package com.superhao.base.exception;

import com.superhao.base.common.dto.BaseJsonResult;

/**
 * @Auther: zehao
 * @Date: 2019/4/25 12:42
 * @email: 928649522@qq.com
 * @Description:
 */
public class HttpParamException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private BaseJsonResult errorResponse;


    public HttpParamException(String message,BaseJsonResult errorResponse) {
        super(message);
        this.errorResponse = errorResponse;
    }

    public HttpParamException(BaseJsonResult errorResponse) {
        super(errorResponse.getMsg());
        this.errorResponse = errorResponse;
    }

    public HttpParamException(String message) {
        super(message);
    }

    public BaseJsonResult getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(BaseJsonResult errorResponse) {
        this.errorResponse = errorResponse;
    }
}
