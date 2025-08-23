package com.ag.like.service.impl;

import com.ag.like.constant.UserConstant;
import com.ag.like.entity.po.User;
import com.ag.like.mapper.UserMapper;
import com.ag.like.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

/**
* @author wcw
* @description 针对表【user】的数据库操作Service实现
* @createDate 2025-08-23 13:26:47
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Override
    public User getLoginUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute(UserConstant.LOGIN_USER);
    }

}




