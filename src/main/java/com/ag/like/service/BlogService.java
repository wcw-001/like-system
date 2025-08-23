package com.ag.like.service;

import com.ag.like.entity.po.Blog;
import com.ag.like.entity.vo.BlogVO;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author wcw
* @description 针对表【blog】的数据库操作Service
* @createDate 2025-08-23 13:26:46
*/
public interface BlogService extends IService<Blog> {

    /**
     * 根据id获取博客
     * @param blogId
     * @param request
     * @return
     */
    BlogVO getBlogVOById(long blogId, HttpServletRequest request);

    List<BlogVO> getBlogVOList(List<Blog> blogList, HttpServletRequest request);
}
