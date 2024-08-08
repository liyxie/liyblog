package com.liy.vo.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RouterVO {

    private Integer id;
    private String component;
    private String path;
    private String name;

    private String redirect;

    private Integer sort;
    private MetaVO meta;

    private List<RouterVO> children;

    @Data
public static class MetaVO{
        private String title;
        private String icon;
        private Boolean hidden;

        public MetaVO(String title, String icon, Integer hidden) {
            this.title = title;
            this.icon = icon;
            this.hidden = hidden == 0;
        }
    }
}
