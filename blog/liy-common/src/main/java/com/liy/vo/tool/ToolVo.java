package com.liy.vo.tool;

import java.util.List;

/**
 * @Author LiY
 */
public interface ToolVo<V> {

    void setChildToolList(List<V> children);

    void setChildToolList(V childTool);

    int getType();

    Integer getId();
}
