package com.ag.like.controller;

import com.ag.like.common.BaseResponse;
import com.ag.like.common.ResultUtils;
import com.ag.like.entity.po.Blog;
import com.ag.like.entity.vo.BlogVO;
import com.ag.like.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "博客功能")
@RestController
@RequestMapping("blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    @Operation(summary = "根据id获取博客详情")
    @PostMapping("/getBlogVOById")
    public BlogVO getBlogVOById(long blogId, HttpServletRequest request) {
        return blogService.getBlogVOById(blogId, request);
    }

    @Operation(summary = "获取博客列表")
    @PostMapping("/list")
    public BaseResponse<List<BlogVO>> list(HttpServletRequest request) {
        List<Blog> blogList = blogService.list();
        List<BlogVO> blogVOList = blogService.getBlogVOList(blogList, request);
        return ResultUtils.success(blogVOList);
    }

}
