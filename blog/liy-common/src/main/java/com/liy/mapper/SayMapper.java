package com.liy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liy.vo.say.ApiSayVO;
import com.liy.entity.Say;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Repository
public interface SayMapper extends BaseMapper<Say> {

    Page<ApiSayVO> selectPublicSayList(@Param("page") Page<Object> objectPage, @Param("showPrivate") boolean showPrivate);
}
