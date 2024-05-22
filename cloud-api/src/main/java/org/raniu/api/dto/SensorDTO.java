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
 * @since 2023-12-11
 */
@Data
@ApiModel(description = "传感器实体类")
public class SensorDTO {

    @ApiModelProperty("传感器id")
    private String id;

    @ApiModelProperty("传感器名称")
    private String name;

    @ApiModelProperty("传感器所属设备")
    private String owner;

    @ApiModelProperty("传感器类型")
    private Integer type;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("设备从机地址")
    private String addr;

    @ApiModelProperty("设备起始地址")
    private String start;

    @ApiModelProperty("数据长度")
    private String len;

    @ApiModelProperty("开启值")
    private String con;

    @ApiModelProperty("关闭值")
    private String coff;

    @ApiModelProperty("寄存器类型")
    private String func;

    @ApiModelProperty("换算公式")
    private String formula;
}
