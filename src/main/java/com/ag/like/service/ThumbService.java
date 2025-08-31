package com.ag.like.service;

import com.ag.like.entity.dto.DoThumbRequest;
import com.ag.like.entity.po.Thumb;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author wcw
* @description 针对表【thumb】的数据库操作Service
* @createDate 2025-08-23 13:26:46
*/
public interface ThumbService extends IService<Thumb> {

    Boolean doThumb(DoThumbRequest doThumbRequest, HttpServletRequest request);

    Boolean undoThumb(DoThumbRequest doThumbRequest, HttpServletRequest request);
}
