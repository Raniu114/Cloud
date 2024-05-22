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
 * @since 2024-01-10
 */
@Data
@ApiModel(description = "策略历史记录")
public class StrategyHistoryDTO {

    @ApiModelProperty("记录id")
    private Long id;

    @ApiModelProperty("设备id")
    private String deviceId;

    @ApiModelProperty("策略id")
    private Long strategyId;

    @ApiModelProperty("执行时间")
    private Long time;


}
