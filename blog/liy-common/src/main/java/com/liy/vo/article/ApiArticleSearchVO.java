package com.liy.vo.article;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @author blue
 * @date 2021/12/30
 * @apiNote
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiArticleSearchVO {

    @Schema(description = "id")
    private Long id;


    @Schema(description = "文章标题")
    private String title;


    @Schema(description = "文章内容")
    private String summary;
}
