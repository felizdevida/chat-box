package com.box.implatform.controller;


import com.box.implatform.result.Result;
import com.box.implatform.result.ResultUtils;
import com.box.implatform.service.IUserService;
import com.box.implatform.dto.LoginDTO;
import com.box.implatform.dto.RegisterDTO;
import com.box.implatform.vo.LoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;



@Api(tags = "用户登录和注册")
@RestController
public class LoginController {

    @Autowired
    private IUserService userService;

    @PostMapping("/login")
    @ApiOperation(value = "用户注册",notes="用户注册")
    public Result register(@Valid @RequestBody LoginDTO dto){
        LoginVO vo = userService.login(dto);
        return ResultUtils.success(vo);
    }


    @PutMapping("/refreshToken")
    @ApiOperation(value = "刷新token",notes="用refreshtoken换取新的token")
    public Result refreshToken(@RequestHeader("refreshToken")String refreshToken){
        LoginVO vo = userService.refreshToken(refreshToken);
        return ResultUtils.success(vo);
    }


    @PostMapping("/register")
    @ApiOperation(value = "用户注册",notes="用户注册")
    public Result register(@Valid @RequestBody RegisterDTO dto){
        userService.register(dto);
        return ResultUtils.success();
    }
}
