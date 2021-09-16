package com.superhao.base.entity;

import com.alibaba.fastjson.JSONObject;
import com.superhao.base.cache.util.SysAuthzUtil;
import com.superhao.base.common.dto.BaseJsonResult;
import com.superhao.base.common.dto.SysTips;
import com.superhao.base.entity.token.SysUserToken;
import com.superhao.base.util.SpringContexUtil;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Auther:
 * @Date: 2019/4/25 11:55
 * @email:
 * @Description:
 */
public class HttpRequestData {
    /**
     * request所有参数集二次封装
     */
    private Map<String, String[]> paramData = null;

    MultiValueMap<String, MultipartFile> fileMap = null;
    /**
     * response响应数据集
     */
    private BaseJsonResult responseData = null;
    /**
     * 当前系统用户的Token对象
     */
    private SysUserToken currentUserToken = null;

    private Map responseMap = null;

    public HttpRequestData() {
        paramData = new HashMap<>();
        responseData =new BaseJsonResult(true);
       currentUserToken = SysAuthzUtil.currentSysUser();
        paramData= SpringContexUtil.getServletRequest().getParameterMap();

    }

    /**
     * 媒体流时初始化用
     * @param request
     */
    public HttpRequestData(HttpServletRequest request) {
        paramData = new HashMap<>();
        responseData =new BaseJsonResult(true);
        currentUserToken = SysAuthzUtil.currentSysUser();
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        paramData = multipartRequest.getParameterMap();
        fileMap = multipartRequest.getMultiFileMap();
    }


    public boolean hasPageParam(){
        return this.getInteger("page")!=null;
    }
    public boolean hasParam(String key){
       String []params = (String[]) this.paramData.get(key);
        return !(params==null||params.length==0||StringUtils.isEmpty(params[0]));
    }

    /**
     *
     * @param keys
     * @return
     * true exists
     * fals not exists
     */
    public boolean hasMustParam(String keys){
        String []paramKeys = keys.split(",");
        if(paramKeys==null||paramKeys.length==0){
            return false;
        }
        for(String key:paramKeys){
            String []params = (String[]) this.paramData.get(key);
            if(params==null||params.length==0|| StringUtils.isEmpty(params[0])){
                return false;
            }
        }
        return true;
    }


    /**
     * 获取String 类型的参数值
     * @param key
     * @return
     */
    public String getString(String key){
        Object res = paramData.get(key);
        return res==null? null :((String[])res)[0];
    }
    /**
     * 获取Integer 类型的参数值
     * @param key
     * @return
     */
    public Integer getInteger(String key){
        Object val = paramData.get(key);
        String []array = (String[])val;
        if(array==null||StringUtils.isEmpty(array[0])){
            return null;
        }
        return new Integer(array[0]);
    }
    /**
     * 获取Integer 类型的参数值
     * @param key
     * @return
     */
    public Long getLong(String key){
        Object val = paramData.get(key);
        String []array = (String[])val;
        if(array==null||StringUtils.isEmpty(array[0])){
            return null;
        }
        return new Long(array[0]);
    }

    public void generatePageData(List<?> dataList,int count){
        this.responseData.setCode(0);
        this.responseData.setData(dataList);
        this.responseData.put("count",count);
    }
    public void fillResponseData(Object data){
        this.responseData.setData(data);
    }
    public HttpRequestData fillResponseMapData(String key, Object data){
        if(responseMap ==null){
            responseMap = new HashMap<String,Object>();
        }
        responseMap.put(key,data);
        return this;
    }
    public void fillClose(){
        this.responseData.setData(responseMap);
    }

    public void createErrorResponse(String msg,SysTips sysTips){
        this.responseData = new BaseJsonResult(false,msg, sysTips);
    }
    public void createErrorResponse(SysTips sysTips){
        this.responseData = new BaseJsonResult(false, sysTips);
    }
    public void createErrorResponse(String msg) {
        this.responseData = new BaseJsonResult(false,msg);
    }

    public Long getUserId(){
        return currentUserToken.getUserId();
    }


    /**
     *
     * MultipartHttpServletRequest 处理方式
     *
     */
    public MultiValueMap<String, MultipartFile> getFileMap(){
        return  this.fileMap;
    }
    public MultipartFile getMultipartFile(String attributeName){
        return  this.fileMap.get(attributeName).get(0);
    }
    public boolean hasMustUploadParam(){
        if(StringUtils.isEmpty(this.paramData.get("module"))){
            createErrorResponse("缺少模块参数", SysTips.PARAM_ERROR);
            return false;
        }
        if(this.fileMap.size()==0){
            createErrorResponse("上传的文件丢失", SysTips.PARAM_ERROR);
            return false;
        }
        return  true;
    }



    public HashMap<String, Object> response(){
        return  this.responseData;
    }

    public static HttpRequestData create(){
        return new HttpRequestData();
    }


    public void responseToJson(HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(JSONObject.toJSONString(this.responseData));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }
}
