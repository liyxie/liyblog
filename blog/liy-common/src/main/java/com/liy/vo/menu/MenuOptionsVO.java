package com.liy.vo.menu;

import lombok.Data;

import java.util.List;

@Data
public class MenuOptionsVO {
    private Integer value;

    private String label;

    private List<MenuOptionsVO> children;

    public MenuOptionsVO(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
