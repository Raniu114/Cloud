package org.raniu.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *
 * </p>
 *
 * @author raniu
 * @since 2023-12-15
 */
@Data
@ApiModel(description = "策略实体类")
public class StrategyDTO {

    @ApiModelProperty("策略id")
    private Long id;

    @ApiModelProperty("设备id")
    private String deviceId;

    @ApiModelProperty("触发条件")
    private String logic;

    @ApiModelProperty("执行命令")
    private String execute;

    @ApiModelProperty("执行时间")
    private String time;

}
