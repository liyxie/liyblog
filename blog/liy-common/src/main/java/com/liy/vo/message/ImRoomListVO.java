package com.liy.vo.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImRoomListVO {
    @ApiModelProperty(value = "主键id")
    private String id;

    @ApiModelProperty(value = "接收用户id")
    private String receiveId;

    @ApiModelProperty(value = "接收用户昵称")
    private String nickname;

    @ApiModelProperty(value = "接收用户头像")
    private String avatar;

    @ApiModelProperty(value = "未读消息条数")
    private int readNum;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "创建时间")
    private String createTimeStr;
}
