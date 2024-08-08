package com.liy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liy.entity.Tags;
import com.liy.vo.tag.ApiTagListVO;
import com.liy.vo.tag.SystemTagListVo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 博客标签表 Mapper 接口
 * </p>
 *
 * @author blue
 * @since 2021-07-19
 */
@Repository
public interface TagsMapper extends BaseMapper<Tags> {

    /**
     * 添加标签
     * @param articleId
     * @param tags
     */
    void saveArticleTags(@Param("articleId") Long articleId, @Param("tags")List<Long> tags);

    /**
     * 根据id删除文章对应中间表数据
     * @param ids
     */
    void deleteByArticleIds(@Param("ids") List<Long> ids);

    List<String> selectByArticleId(Long articleId);


    /**
     * 分页获取标签
     * @param objectPage
     * @param name
     * @return
     */
    Page<SystemTagListVo> selectPageRecord(@Param("page") Page<Tags> objectPage, @Param("name") String name);

    /**
     * 标签统计
     * @return
     */
    @MapKey("id")
    List<Map<String, Object>> getTagKeyValuePairs();


    /**
     * 根据文章id获取标签
     * @param articleId
     * @return
     */
    List<Tags> selectTagByArticleId(Long articleId);

    /**
     * 校验此标签是否存在文章关联
     * @param id
     * @return
     */
    int validateTagIdIsExistArticle(Long id);

    List<ApiTagListVO> selectTagListApi();

}
