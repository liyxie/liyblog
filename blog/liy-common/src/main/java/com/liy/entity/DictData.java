package com.liy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 字典数据表
 * </p>
 *
 * @author blue
 * @since 2021-11-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(title="DictData对象", description="字典数据表")
@TableName("b_dict_data")
public class DictData implements Serializable {

    private static final long serialVersionUID=1L;

    @Schema(description = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "字典类型id")
    private Long dictId;

    @Schema(description = "字典标签")
    private String label;

    @Schema(description = "字典键值")
    private String value;

    @Schema(description = "回显样式")
    private String style;

    @Schema(description = "是否默认（1是 0否）")
    private String isDefault;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "是否发布(1:正常，0:停用)")
    private Integer status;

    @Schema(description = "备注")
    private String remark;

    @TableField(exist = false)
    private Dict dict;
}
