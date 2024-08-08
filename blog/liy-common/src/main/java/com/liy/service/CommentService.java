package com.liy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liy.common.ResponseResult;
import com.liy.entity.Comment;

import java.util.List;

/**
 * <p>
 * 博客文章表 服务类
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
public interface CommentService extends IService<Comment> {

    /**
     * 分页
     * @param keywords
     * @return
     */
    ResponseResult selectCommentPage(String keywords);

    /**
     * 删除
     * @param ids
     * @return
     */
    ResponseResult deleteComment(List<Integer> ids);

}
