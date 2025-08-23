package com.ag.like.service.impl;

import com.ag.like.entity.po.Blog;
import com.ag.like.entity.po.Thumb;
import com.ag.like.entity.po.User;
import com.ag.like.entity.vo.BlogVO;
import com.ag.like.mapper.BlogMapper;
import com.ag.like.service.BlogService;
import com.ag.like.service.ThumbService;
import com.ag.like.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author wcw
* @description 针对表【blog】的数据库操作Service实现
* @createDate 2025-08-23 13:26:46
*/
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog>
    implements BlogService {

    @Resource
    private UserService userService;

    @Resource
    private ThumbService thumbService;
    @Override
    public BlogVO getBlogVOById(long blogId, HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        Blog blog = this.getById(blogId);
        BlogVO blogVO = hasThumb(blog, user);
        return blogVO;
    }

    @Override
    public List<BlogVO> getBlogVOList(List<Blog> blogList, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Map<Long,Boolean> hasThumbMap = new HashMap<>();
        if (loginUser != null){
            List<Thumb> thumbList = thumbService.lambdaQuery()
                    .eq(Thumb::getUserid, loginUser.getId())
                    .in(Thumb::getBlogid, blogList)
                    .list();
            thumbList.forEach(thumb -> {
                hasThumbMap.put(thumb.getBlogid(), true);
            });
        }
        return blogList.stream().map(blog -> {
            BlogVO blogVO = new BlogVO();
            BeanUtils.copyProperties(blogVO, blogVO);
            blogVO.setHasThumb(hasThumbMap.get(blog.getId()));
            return blogVO;
        }).toList();
    }

    //是否已经点赞
    private BlogVO hasThumb(Blog blog, User loginUser) {
        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(blogVO, blogVO);
        if (loginUser == null) {
            return blogVO;
        }
        Thumb one = thumbService.lambdaQuery()
                .eq(Thumb::getUserid, loginUser.getId())
                .eq(Thumb::getBlogid, blog.getId())
                .last("limit 1")
                .one();
        blogVO.setHasThumb(one != null);
        return blogVO;
    }
}




