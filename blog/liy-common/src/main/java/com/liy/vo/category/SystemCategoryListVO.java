package com.liy.vo.category;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SystemCategoryListVO {
    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "点击量")
    private Integer clickVolume;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "文章量")
    private int articleCount;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "最后更新时间")
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date updateTime;

}
