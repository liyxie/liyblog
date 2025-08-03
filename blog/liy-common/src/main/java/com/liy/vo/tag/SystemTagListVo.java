package com.liy.vo.tag;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SystemTagListVo {
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "标签名称")
    private String name;

    @Schema(description = "封面图")
    private String avatar;

    @Schema(description = "排序")
    private int sort;

    @Schema(description = "点击量")
    private int clickVolume;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @Schema(description = "最后更新时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date updateTime;

    @Schema(description = "文章量")
    private int articleCount;
}
