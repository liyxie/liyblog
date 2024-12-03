package com.liy.dto.tool;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/*
 * @Description: 工具链接表
 * @date: 2024-10-25
 */
@Data
public class ToolDto {
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

    /**
     * 描述
     *
     */
    private String describe;

    public boolean isNull() {
        return this.id == null && this.name == null && this.parentId == null && this.describe == null;
    }
}