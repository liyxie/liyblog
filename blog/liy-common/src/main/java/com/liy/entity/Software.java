package com.liy.entity;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

/*
 * @Description:
 * @date: 2024-03-19
 */
@Data
@TableName(value = "b_software")
public class Software {
    /*
     *主键
     */
    @TableId
    private Integer id;
    /*
     *软件名称
     */
    private String name;
    /*
     *描述
     */
    private String info;
    /*
     *源码地址
     */
    private String codeUrl;
    /*
     *轮播图片,逗号分隔
     */
    private String coverImg;
    /*
     *演示地址
     */
    private String demoUrl;
    /*
     *详细介绍地址
     */
    private String detailUrl;
    /*
     *创建时间
     */
    private Date createTime;
}