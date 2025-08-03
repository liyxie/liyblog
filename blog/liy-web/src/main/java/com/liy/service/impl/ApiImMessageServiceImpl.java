package com.liy.service.impl;


import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.liy.common.ResponseResult;
import com.liy.entity.ImMessage;
import com.liy.entity.ImRoom;
import com.liy.enums.YesOrNoEnum;
import com.liy.exception.BusinessException;
import com.liy.utils.BeanCopyUtil;
import com.liy.utils.DateUtil;
import com.liy.utils.IpUtil;
import com.liy.utils.PageUtil;
import com.liy.handle.RelativeDateFormat;
import com.liy.im.MessageConstant;
import com.liy.im.WebSocketInfoService;
import com.liy.mapper.ImMessageMapper;
import com.liy.mapper.ImRoomMapper;
import com.liy.mapper.UserMapper;
import com.liy.service.ApiImMessageService;
import com.liy.vo.message.ImMessageVO;
import com.liy.vo.user.ImOnlineUserVO;
import com.liy.vo.message.ImRoomListVO;
import com.liy.vo.user.UserInfoVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author blue
 * @since 2021-11-10
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ApiImMessageServiceImpl implements ApiImMessageService {

    private final ImMessageMapper imMessageMapper;

    private final ImRoomMapper imRoomMapper;

    private final UserMapper userMapper;


    private final WebSocketInfoService webSocketInfoService;

    private Pattern pattern = Pattern.compile("(http|https)://[\\w\\d.-]+(/[\\w\\d./?=#&-]*)?");

    @Override
    public ResponseResult selectHistoryList() {
        Page<ImMessageVO> page = imMessageMapper.selectPublicHistoryList(new Page<>(PageUtil.getPageNo(),
                PageUtil.getPageSize()));
        formatCreateTime(page);
        return ResponseResult.success(page);
    }

    @Override
    public List<ImOnlineUserVO> selectOnlineUserList(Set<String> keys) {
        return imMessageMapper.selectPublicOnlineUserList(keys);
    }

    @Override
    public ResponseResult selectUserImHistoryList(String fromUserId, String toUserId) {
        Page<ImMessageVO> page = imMessageMapper.selectPublicUserImHistoryList(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                fromUserId, toUserId);
        formatCreateTime(page);
        return ResponseResult.success(page);
    }

    /**
     * 获取房间列表
     *
     * @return
     */
    @Override
    public ResponseResult selectRoomList() {
        List<ImRoomListVO> list = new ArrayList<>();
        List<ImRoom> imRooms = imRoomMapper.selectList(new LambdaQueryWrapper<ImRoom>().eq(ImRoom::getFromUserId, StpUtil.getLoginIdAsString())
                .orderByDesc(ImRoom::getCreateTime));
        for (ImRoom imRoom : imRooms) {
            String toUserId = imRoom.getToUserId();
            UserInfoVO userInfoVO = userMapper.selectInfoByUserId(toUserId);
            ImRoomListVO vo = ImRoomListVO.builder().id(imRoom.getId()).receiveId(toUserId).nickname(userInfoVO.getNickname())
                    .avatar(userInfoVO.getAvatar()).createTimeStr(RelativeDateFormat.format(imRoom.getCreateTime())).build();
            int readNum = imMessageMapper.selectListReadByUserId(toUserId, StpUtil.getLoginIdAsString());
            vo.setReadNum(readNum);
            list.add(vo);
        }
        return ResponseResult.success(list);
    }

    /**
     * 创建房间
     *
     * @param toUserId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult addRoom(String toUserId) {
        String fromUserId = StpUtil.getLoginIdAsString();
        if (StringUtils.isBlank(toUserId)) {
            throw new BusinessException("请选择用户！");
        }
        if (toUserId.equals(fromUserId)) {
            throw new BusinessException("不能跟自己聊天！");
        }
        ImRoom imRoom = imRoomMapper.selectOne(new LambdaQueryWrapper<ImRoom>().eq(ImRoom::getFromUserId, fromUserId)
                .eq(ImRoom::getToUserId, toUserId));
        if (imRoom != null) {
            return ResponseResult.success();
        }
        imRoom = ImRoom.builder().type(1).fromUserId(fromUserId).toUserId(toUserId).build();
        imRoomMapper.insert(imRoom);
        UserInfoVO userInfoVO = userMapper.selectInfoByUserId(toUserId);
        ImRoomListVO vo = ImRoomListVO.builder().id(imRoom.getId()).receiveId(toUserId).nickname(userInfoVO.getNickname())
                .avatar(userInfoVO.getAvatar()).createTimeStr(RelativeDateFormat.format(imRoom.getCreateTime())).build();
        return ResponseResult.success(vo);
    }

    /**
     * 已读消息
     *
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult read(String userId) {
        imMessageMapper.updateRead(userId, StpUtil.getLoginIdAsString());
        return ResponseResult.success();
    }

    /**
     * 删除房间
     *
     * @param roomId 房间id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteRoom(String roomId) {
        imRoomMapper.deleteById(roomId);
        return ResponseResult.success();
    }

    /**
     * 发送消息
     *
     * @param obj     消息
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult chat(ImMessageVO obj) {
        Matcher matcher = pattern.matcher(obj.getContent());
        //字符串找出http地址
        String content = obj.getContent();
        String url = null;
        try {
            while (matcher.find()) {
                url = matcher.group();
                if (url.contains("img.liy.com") ||  url.contains("res.wx.qq.com") || url.contains("npm.elemecdn.com")) {
                    continue;
                }
                Document doc = Jsoup.connect(url).get();
                String hrefContent = String.format("<a href=\"%s\" target=\"_blank\" >%s</a>", url, doc.title());
                content = content.replace(url, hrefContent);
            }
        } catch (IOException e) {
            log.error("网址={} 解析失败", url);
        }
        //图片不进行敏感词过滤，会把图片地址部分过滤掉
//        if (obj.getType() != MessageConstant.MESSAGE_TYPE_IMG) {
//            //过滤敏感词
//            String filterContent = SensitiveUtils.filter(content);
//            obj.setContent(filterContent);
//        }

        obj.setIp(IpUtil.getIp());
        obj.setIpSource(IpUtil.getIp2region(obj.getIp()));
        obj.setContent(content);
        ImMessage imMessage = BeanCopyUtil.copyObject(obj, ImMessage.class);
        //保存消息到数据库
        imMessageMapper.insert(imMessage);
        //如果是私聊，则发送聊天的同时给发送人创建房间
        if (obj.getCode() == MessageConstant.PRIVATE_CHAT_CODE) {
            ImRoom imRoom = imRoomMapper.selectOne(new LambdaQueryWrapper<ImRoom>().eq(ImRoom::getFromUserId, obj.getToUserId())
                    .eq(ImRoom::getToUserId, obj.getFromUserId()));
            if (imRoom == null) {
                ImRoom room = ImRoom.builder().fromUserId(obj.getToUserId()).toUserId(obj.getFromUserId()).type(obj.getCode()).build();
                imRoomMapper.insert(room);
            }
        }

        //组装发送消息数据
        obj.setId(imMessage.getId());
        obj.setCreateTimeStr(RelativeDateFormat.format(imMessage.getCreateTime()));
        //发送消息
        webSocketInfoService.chat(obj);
        return ResponseResult.success();
    }

    /**
     * 撤回消息
     *
     * @param message 消息对象
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult withdraw(ImMessageVO message) {
        ImMessage entity = imMessageMapper.selectById(message.getId());
        if (DateUtil.getDiffDateToMinutes(entity.getCreateTime(), DateUtil.getNowDate()) >= 2) {
            throw new BusinessException("撤回失败，只能撤回俩分钟以内的消息！");
        }
        if (!entity.getFromUserId().equals(StpUtil.getLoginIdAsString())) {
            throw new BusinessException("只能撤回自己的消息哦！");
        }
        ImMessage imMessage = BeanCopyUtil.copyObject(message, ImMessage.class);
        imMessage.setIp(IpUtil.getIp());
        imMessage.setIpSource(IpUtil.getIp2region(imMessage.getIp()));

        //修改消息
        imMessageMapper.updateById(imMessage);

        //发送消息
        webSocketInfoService.chat(message);

        return ResponseResult.success();
    }

    /**
     * 获取跟当前用户有关的系统通知
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult getMessageNotice(Integer type) {
        Page<ImMessageVO> page = imMessageMapper.getMessageNotice(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                StpUtil.getLoginIdAsString(), type);
        page.getRecords().forEach(item -> {
            item.setCreateTimeStr(RelativeDateFormat.format(item.getCreateTime()));
        });
        //修改该类型的所有消息为已读
        ImMessage message = ImMessage.builder().isRead(1).build();
        imMessageMapper.update(message, new LambdaQueryWrapper<ImMessage>().eq(ImMessage::getToUserId, StpUtil.getLoginIdAsString())
                .eq(ImMessage::getNoticeType, type));
        return ResponseResult.success(page);
    }

    /**
     * 获取未读的最新系统通知
     *
     * @return
     */
    @Override
    public ResponseResult getNewSystemNotice() {
        Long systemCount = imMessageMapper.selectCount(new LambdaQueryWrapper<ImMessage>()
                .eq(ImMessage::getCode, MessageConstant.SYSTEM_MESSAGE_CODE).eq(ImMessage::getToUserId, StpUtil.getLoginIdAsString())
                .eq(ImMessage::getIsRead, 0).eq(ImMessage::getNoticeType, MessageConstant.MESSAGE_SYSTEM_NOTICE));

        Long commentCount = imMessageMapper.selectCount(new LambdaQueryWrapper<ImMessage>()
                .eq(ImMessage::getCode, MessageConstant.SYSTEM_MESSAGE_CODE).eq(ImMessage::getToUserId, StpUtil.getLoginIdAsString())
                .eq(ImMessage::getIsRead, 0).eq(ImMessage::getNoticeType, MessageConstant.MESSAGE_COMMENT_NOTICE));

        Long privateCount = imMessageMapper.selectCount(new LambdaQueryWrapper<ImMessage>()
                .eq(ImMessage::getCode, MessageConstant.PRIVATE_CHAT_CODE).eq(ImMessage::getToUserId, StpUtil.getLoginIdAsString())
                .eq(ImMessage::getIsRead, 0));

        Map<String, Integer> map = new HashMap<>();
        map.put("system", Math.toIntExact(systemCount));
        map.put("comment", Math.toIntExact(commentCount));
        map.put("private", Math.toIntExact(privateCount));
        return ResponseResult.success(map);
    }

    /**
     * 根绝类型删除消息
     *
     * @param type 通知类型
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult deleteByNoticeType(String id, Integer type) {
        if (StringUtils.isNotBlank(id)) {
            imMessageMapper.deleteById(id);
            return ResponseResult.success();
        }
        imMessageMapper.delete(new LambdaQueryWrapper<ImMessage>().eq(ImMessage::getToUserId, StpUtil.getLoginIdAsString())
                .eq(ImMessage::getNoticeType, type));
        return ResponseResult.success();
    }

    @Override
    public ResponseResult getMessageNoticeApplet(Integer type) {
        Page<ImMessageVO> page = imMessageMapper.getMessageNotice(new Page<>(PageUtil.getPageNo(), PageUtil.getPageSize()),
                StpUtil.getLoginIdAsString(), type);
        page.getRecords().forEach(item -> {
            item.setCreateTimeStr(RelativeDateFormat.format(item.getCreateTime()));
        });
        return ResponseResult.success(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult markReadMessageNoticeApplet(String id) {
        if (StringUtils.isNotBlank(id)) {
            imMessageMapper.updateById(ImMessage.builder().id(id).isRead(YesOrNoEnum.YES.getCode()).build());
            return ResponseResult.success();
        }
        imMessageMapper.update(ImMessage.builder().isRead(YesOrNoEnum.YES.getCode()).build(),new LambdaQueryWrapper<ImMessage>()
                .eq(ImMessage::getToUserId, StpUtil.getLoginIdAsString()));
        return ResponseResult.success();
    }

    private void formatCreateTime(Page<ImMessageVO> page) {
        page.getRecords().forEach(item -> item.setCreateTimeStr(RelativeDateFormat.format(item.getCreateTime())));
    }

}
