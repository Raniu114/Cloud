package org.raniu.device.domain.vo;

import io.swagger.annotations.ApiModel;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * @projectName: cloud
 * @package: org.raniu.vo
 * @className: ControlVo
 * @author: Raniu
 * @description: TODO
 * @date: 2023/12/29 16:29
 * @version: 1.0
 */
@Data
@ApiModel(value = "控制参数", description = "执行器控制参数")
public class ControlVo {

    private String tag;

    @NotNull(message = "值不能为空")
    private String value;


}
