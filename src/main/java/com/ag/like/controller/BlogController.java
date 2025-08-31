package com.ag.like.controller;

import com.ag.like.common.BaseResponse;
import com.ag.like.common.ResultUtils;
import com.ag.like.entity.po.Blog;
import com.ag.like.entity.vo.BlogVO;
import com.ag.like.service.BlogService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    @RequestMapping("/getBlogVOById")
    public BlogVO getBlogVOById(long blogId, HttpServletRequest request) {
        return blogService.getBlogVOById(blogId, request);
    }

    @GetMapping("/list")
    public BaseResponse<List<BlogVO>> list(HttpServletRequest request) {
        List<Blog> blogList = blogService.list();
        List<BlogVO> blogVOList = blogService.getBlogVOList(blogList, request);
        return ResultUtils.success(blogVOList);
    }

}
