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
 * @since 2023-12-14
 */
@Data
@ApiModel(description = "历史记录实体类")
public class HistoryDTO {

    @ApiModelProperty("记录id")
    private Long id;

    @ApiModelProperty("设备id")
    private String deviceId;

    @ApiModelProperty("传感器id")
    private String sensorId;

    @ApiModelProperty("时间")
    private Long time;

    @ApiModelProperty("记录值")
    private String value;

}
