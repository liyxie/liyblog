package com.liy.vo.friendLink;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ApiFriendLinkVO {

    @Schema(description = "网站名称")
    private String name;

    @Schema(description = "网站地址")
    private String url;

    @Schema(description = "网站头像地址")
    private String avatar;

    @Schema(description = "网站描述")
    private String info;

}
