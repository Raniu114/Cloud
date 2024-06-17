package org.raniu.api.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.raniu.api.enums.ResultCode;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.api.vo
 * @className: Result
 * @author: Raniu
 * @description: TODO
 * @date: 2024/6/17 10:57
 * @version: 1.0
 */

@Data
@ApiModel("result")
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    @ApiModelProperty("状态码")
    private Integer status;

    @ApiModelProperty("信息")
    private String msg;

    @ApiModelProperty("数据")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @ApiModelProperty("页码")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long page;

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<T>();
        result.setStatus(ResultCode.SUCCESS.getCode());
        result.setData(data);
        result.setMsg("获取成功");
        return result;
    }

    public static <T> Result<T> success(T data, Long page) {
        Result<T> result = new Result<T>();
        result.setStatus(ResultCode.SUCCESS.getCode());
        result.setData(data);
        result.setMsg("获取成功");
        result.setPage(page);
        return result;
    }


    public static <T> Result<T> success(String message, T data) {
        Result<T> result = new Result<T>();
        result.setStatus(ResultCode.SUCCESS.getCode());
        result.setMsg(message);
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(String message) {
        Result<T> result = new Result<T>();
        result.setStatus(ResultCode.SUCCESS.getCode());
        result.setMsg(message);
        return result;
    }

    public static <T> Result<T> error(ResultCode code, String message) {
        Result<T> result = new Result<T>();
        result.setStatus(code.getCode());
        result.setMsg(message);
        return result;
    }

}
