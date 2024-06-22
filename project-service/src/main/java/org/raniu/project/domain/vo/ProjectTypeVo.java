package org.raniu.project.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.project.domain.vo
 * @className: ProjectTypeVo
 * @author: Raniu
 * @description: TODO
 * @date: 2024/6/17 17:02
 * @version: 1.0
 */

@Data
@Schema(title = "项目请求参数", description = "项目请求参数")
public class ProjectTypeVo {

    @Schema(description = "项目类型", name = "type", required = true)
    private Long type;

    @Schema(description = "项目类型名称", name = "typeName", required = true)
    private String typeName;
}
