package com.liy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.liy.entity.ImRoom;
import com.liy.vo.message.ImRoomListVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * </p>
 *
 * @author blue
 * @since 2021-08-18
 */
@Repository
public interface ImRoomMapper extends BaseMapper<ImRoom> {


    List<ImRoomListVO> selectListByUserId(String userId);
}
