package com.ag.like.service.impl;

import com.ag.like.common.ErrorCode;
import com.ag.like.constant.ThumbConstant;
import com.ag.like.entity.dto.DoThumbRequest;
import com.ag.like.entity.dto.ThumbInfo;
import com.ag.like.entity.po.Blog;
import com.ag.like.entity.po.Thumb;
import com.ag.like.entity.po.User;
import com.ag.like.exceotion.BusinessException;
import com.ag.like.mapper.ThumbMapper;
import com.ag.like.service.BlogService;
import com.ag.like.service.ThumbService;
import com.ag.like.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class ThumbServiceImpl extends ServiceImpl<ThumbMapper, Thumb> implements ThumbService {

    private final UserService userService;

    private final BlogService blogService;

    private final TransactionTemplate transactionTemplate;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Boolean doThumb(DoThumbRequest doThumbRequest, HttpServletRequest request) {
        if (doThumbRequest == null || doThumbRequest.getBlogId() == null) {
            throw new RuntimeException("参数错误");
        }
        User loginUser = userService.getLoginUser(request);
        // 加锁
        synchronized (loginUser.getId().toString().intern()) {

            // 编程式事务
            return transactionTemplate.execute(status -> {
                Long blogId = doThumbRequest.getBlogId();
//                boolean exists = this.lambdaQuery()
//                        .eq(Thumb::getUserid, loginUser.getId())
//                        .eq(Thumb::getBlogid, blogId)
//                        .exists();
                Boolean exists = this.hasThumb(blogId, loginUser.getId());
                if (exists) {
                    throw new RuntimeException("用户已点赞");
                }

                boolean update = blogService.lambdaUpdate()
                        .eq(Blog::getId, blogId)
                        .setSql("thumbCount = thumbCount + 1")
                        .update();

                Thumb thumb = new Thumb();
                thumb.setUserid(loginUser.getId());
                thumb.setBlogid(blogId);
                boolean success = update && this.save(thumb);
                if (success){
                    ThumbInfo thumbInfo = new ThumbInfo();
                    thumbInfo.setThumbId(thumb.getId());
                    thumbInfo.setExpireTime(Instant.now().plusSeconds(30L * 24 * 60 * 60).toEpochMilli());
                    redisTemplate.opsForHash().put(ThumbConstant.USER_THUMB_KEY_PREFIX + loginUser.getId(), blogId.toString(), thumbInfo);
                }
                // 更新成功才执行
                return success;
            });
        }
    }

    @Override
    public Boolean undoThumb(DoThumbRequest doThumbRequest, HttpServletRequest request) {
        if (doThumbRequest == null || doThumbRequest.getBlogId() == null) {
            throw new RuntimeException("参数错误");
        }
        User loginUser = userService.getLoginUser(request);
        // 加锁
        synchronized (loginUser.getId().toString().intern()) {

            // 编程式事务
            return transactionTemplate.execute(status -> {
                Long blogId = doThumbRequest.getBlogId();
//                Thumb thumb = this.lambdaQuery()
//                        .eq(Thumb::getUserid, loginUser.getId())
//                        .eq(Thumb::getBlogid, blogId)
//                        .one();
                ThumbInfo thumbInfo = (ThumbInfo) redisTemplate.opsForHash().get(ThumbConstant.USER_THUMB_KEY_PREFIX + loginUser.getId(), blogId.toString());
//                if (thumb == null) {
//                    throw new RuntimeException("用户未点赞");
//                }
                if (thumbInfo == null  || thumbInfo.getExpireTime() < System.currentTimeMillis()){
                    Thumb one = this.lambdaQuery()
                            .eq(Thumb::getUserid, loginUser.getId())
                            .eq(Thumb::getBlogid, blogId)
                            .one();
                    if (one == null){
                        throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户未点赞");
                    }
                }
                boolean update = blogService.lambdaUpdate()
                        .eq(Blog::getId, blogId)
                        .setSql("thumbCount = thumbCount - 1")
                        .update();
                boolean result = update && this.removeById(thumbInfo.getThumbId());
                if (result){
                    redisTemplate.opsForHash().delete(ThumbConstant.USER_THUMB_KEY_PREFIX + loginUser.getId(), blogId.toString());
                }
                return result;
            });
        }
    }



    @Override
    public Boolean hasThumb(Long blogId, Long userId) {
        String key = ThumbConstant.USER_THUMB_KEY_PREFIX + userId;
        ThumbInfo thumbInfo = (ThumbInfo) redisTemplate.opsForHash().get(key, blogId.toString());
        if (thumbInfo == null){
            Thumb one = this.lambdaQuery()
                    .eq(Thumb::getUserid, userId)
                    .eq(Thumb::getBlogid, blogId)
                    .one();
            if (one != null){
                return true;
            }
            return false;
        }
        if (thumbInfo.getExpireTime() < System.currentTimeMillis()){
            redisTemplate.opsForHash().delete(key, blogId.toString());
            return false;
        }
        return true;
    }


}




