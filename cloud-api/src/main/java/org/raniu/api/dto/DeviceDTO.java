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
@ApiModel(description = "设备实体类")
public class DeviceDTO {

    @ApiModelProperty("设备id")
    private String id;

    @ApiModelProperty("设备名称")
    private String name;

    @ApiModelProperty("所属项目")
    private String owner;

    @ApiModelProperty("协议")
    private String protocol;

    @ApiModelProperty("连接密钥")
    private String transferKey;

    @ApiModelProperty("获取间隔")
    private Float collectionTime;

    @ApiModelProperty("创建时间")
    private Long createTime;

    @ApiModelProperty("创建人")
    private Long createUser;

}
