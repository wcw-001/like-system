package com.ag.like.service;

import com.ag.like.entity.po.User;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author wcw
* @description 针对表【user】的数据库操作Service
* @createDate 2025-08-23 13:26:47
*/
public interface UserService extends IService<User> {

    User getLoginUser(HttpServletRequest request);
}
