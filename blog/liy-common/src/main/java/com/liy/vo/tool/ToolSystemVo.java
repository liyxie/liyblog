package com.liy.vo.tool;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.liy.entity.Tool;
import com.liy.utils.DateUtil;
import lombok.Data;

import java.util.Date;
import java.util.List;

/*
 * @Description: 工具链接表
 * @date: 2024-10-25
 */
@Data
public class ToolSystemVo implements ToolVo<ToolSystemVo> {
    /*
    *id
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /*
    *名称
    */
    private String name;
    /*
    *父工具分类id
    */
    private Integer parentId;
    /*
    *排序号
    */
    private Integer sort;
    /*
    *1:工具链接实体;2:工具分类
    */
    private Integer type;
    /*
    *链接
    */
    private String url;

    /**
     * 子菜单
     */
    private List<ToolSystemVo> childToolList;

    /**
     * 描述
     *
     */
    private String describe;


    /**
     * 状态
     */
    private Integer state;

    /**
     * 创建者
     */
    private Integer createBy;

    private String userName;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.FORMAT_STRING,timezone="GMT+8")
    private Date createTime;

    /**
     * 图标
     */
    private String icon;

    @Override
    public void setChildToolList(List<ToolSystemVo> children) { this.childToolList = children; }

    @Override
    public void setChildToolList(ToolSystemVo childTool) {
        this.childToolList.add(childTool);
    }

    @Override
    public Integer getId(){
        return id;
    }
    @Override
    public int getType(){
        return type;
    }


    public static ToolSystemVo PoToVo(Tool tool) {
        ToolSystemVo toolSystemVo = new ToolSystemVo();
        toolSystemVo.id = tool.getId();
        toolSystemVo.icon = tool.getIcon();
        toolSystemVo.describe = tool.getDescribe();
        toolSystemVo.sort = tool.getSort();
        toolSystemVo.name = tool.getName();
        toolSystemVo.parentId = tool.getParentId();
        toolSystemVo.type = tool.getType();
        toolSystemVo.url = tool.getUrl();
        toolSystemVo.createTime = tool.getCreateTime();
        toolSystemVo.createBy = tool.getCreateBy();
        toolSystemVo.state = tool.getState();

        return toolSystemVo;
    }

}