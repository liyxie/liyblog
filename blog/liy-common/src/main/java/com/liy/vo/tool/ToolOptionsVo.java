package com.liy.vo.tool;

import com.liy.entity.Tool;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 下拉菜单选项
 * @Author LiY
 */

@Data
public class ToolOptionsVo implements ToolVo<ToolOptionsVo>{

    private Integer value;

    private String label;

    private List<ToolOptionsVo> children;

    private Integer type;

    @Override
    public void setChildToolList(List<ToolOptionsVo> children) {
        this.children = children;
    }

    @Override
    public void setChildToolList(ToolOptionsVo childTool) {
        if(this.children == null) children = new ArrayList<>();
        this.children.add(childTool);
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public Integer getId() {
        return value;
    }

    public static ToolOptionsVo PoToVo(Tool tool){
        ToolOptionsVo toolOptionsVo = new ToolOptionsVo();
        toolOptionsVo.setValue(tool.getId());
        toolOptionsVo.setLabel(tool.getName());
        toolOptionsVo.setType(tool.getType());
        return toolOptionsVo;
    }
}
