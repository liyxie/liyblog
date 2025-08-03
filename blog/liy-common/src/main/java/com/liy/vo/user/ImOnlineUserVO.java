package com.liy.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 聊天室在线用户列表
 */
@Data
public class ImOnlineUserVO {
    @Schema(description = "用户id")
    private String id;
    @Schema(description = "用户昵称")
    private String nickname;
    @Schema(description = "用户头像")
    private String avatar;
}
