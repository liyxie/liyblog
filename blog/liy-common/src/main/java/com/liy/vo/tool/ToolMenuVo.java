package com.liy.vo.tool;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.liy.entity.Tool;
import lombok.Data;

import java.util.List;

/*
 * @Description: 工具链接表
 * @date: 2024-10-25
 */
@Data
public class ToolMenuVo implements ToolVo<ToolMenuVo>{
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
    private List<ToolMenuVo> childToolList;

    /**
     * 描述
     *
     */
    private String describe;

    /**
     * 图标
     */
    private String icon;

    @Override
    public void setChildToolList(List<ToolMenuVo> children) { this.childToolList = children; }

    @Override
    public void setChildToolList(ToolMenuVo childTool) {
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

    public static ToolMenuVo PoToVo(Tool tool){
        ToolMenuVo toolMenuVo = new ToolMenuVo();
        toolMenuVo.id = tool.getId();
        toolMenuVo.icon = tool.getIcon();
        toolMenuVo.describe = tool.getDescribe();
        toolMenuVo.sort = tool.getSort();
        toolMenuVo.name = tool.getName();
        toolMenuVo.parentId = tool.getParentId();
        toolMenuVo.type = tool.getType();
        toolMenuVo.url = tool.getUrl();
        return toolMenuVo;
    }

}