package com.liy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/*
 * @Description:
 * @date: 2024-04-11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "b_resource")
public class Resource {
    /*
     *序号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /*
     *文件地址
     */
    private String url;
    /*
     *文件类型
     */
    private String type;
    /*
     *存储平台
     */
    private String platform;
    /*
     *上传用户id
     */
    private String userId;
    /*
     *创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;
}