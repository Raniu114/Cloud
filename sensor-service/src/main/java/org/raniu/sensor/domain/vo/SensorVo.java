package org.raniu.sensor.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * @projectName: cloud
 * @package: org.raniu.vo
 * @className: SensorVo
 * @author: Raniu
 * @description: TODO
 * @date: 2023/12/13 17:22
 * @version: 1.0
 */
@Data
@ApiModel(value = "传感器请求参数",description = "传感器请求参数")
public class SensorVo {
    @ApiModelProperty(value = "传感器id",name = "id",required = false)
    @NotNull(message = "传感器id不能为空")
    private String id;

    @ApiModelProperty(value = "传感器名称",name = "name", required = false)
    @NotNull(message = "传感器名称不能为空")
    private String name;

    @ApiModelProperty(value = "所属设备", name = "owner",required = false)
    @NotNull(message = "传感器所属设备不能为空")
    private String owner;

    @ApiModelProperty(value = "类型", name = "type", required = true)
    @NotNull(message = "类型不能为空")
    private Integer type;

    private String unit;

    private String addr;

    private String start;

    private String len;

    private String con;

    private String coff;

    private String func;

    private String formula;
}
