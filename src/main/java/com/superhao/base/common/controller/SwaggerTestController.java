package com.superhao.base.common.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Auther: zehao
 * @Date: 2019/4/21 00:55
 * @email: 928649522@qq.com
 * @Description:
 */
@RequestMapping
@RestController
@Api(tags = "Swagger Test Api",description="Swagger测试接口")
public class SwaggerTestController {

    @RequestMapping("updateUser")
    @ResponseBody
    @ApiOperation(value = "修改用户" , notes="更新用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "手机号码", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "nickName", value = "用户昵称", required = true, paramType = "query", dataType = "String")
    })
    public String update(HttpServletRequest request, String phone, String nickName){
        return "heheh:see";
    }


}
