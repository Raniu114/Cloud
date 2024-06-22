package org.raniu.device.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


/**
 * @projectName: cloud
 * @package: org.raniu.vo
 * @className: DeviceVo
 * @author: Raniu
 * @description: TODO
 * @date: 2023/12/13 16:06
 * @version: 1.0
 */
@Data
@ApiModel(value = "设备请求参数",description = "设备请求参数")
public class DeviceVo {

    @ApiModelProperty(value = "设备id",name = "id",required = true)
    @NotNull(message = "设备id不能为空")
    private String id;

    @ApiModelProperty(value = "设备名称",name = "name", required = true)
    @NotNull(message = "设备名称不能为空")
    private String name;

    @ApiModelProperty(value = "设备所属项目", name = "owner",required = false)
    private String owner;

    @ApiModelProperty(value = "协议", name = "protocol", required = true)
    @NotNull(message = "设备通信协议不能为空")
    private String protocol;

    @ApiModelProperty(value = "采集间隔", name = "collectionTime", required = false)
    private Float collectionTime;



}
