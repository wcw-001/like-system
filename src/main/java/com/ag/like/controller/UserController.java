package com.ag.like.controller;

import com.ag.like.common.BaseResponse;
import com.ag.like.common.ResultUtils;
import com.ag.like.constant.UserConstant;
import com.ag.like.entity.po.User;
import com.ag.like.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "用户功能")
@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;

    @Operation(summary = "用户登录")
    @GetMapping("/login")
    public BaseResponse<User> login(long userId, HttpServletRequest request) {
        User user = userService.getById(userId);
        request.getSession().setAttribute(UserConstant.LOGIN_USER, user);
        return ResultUtils.success(user);
    }

    @Operation(summary = "获取登录用户信息")
    @GetMapping("/get/login")
    public BaseResponse<User> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(loginUser);
    }

}
