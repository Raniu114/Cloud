package org.raniu.api.enums;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.api.enums
 * @className: ResultCode
 * @author: Raniu
 * @description: TODO
 * @date: 2024/6/17 11:04
 * @version: 1.0
 */
public enum ResultCode {

    TOKEN_ERROR(-4), TOKEN_TIMEOUT(-3), PERMISSIONS_ERROR(-2), ERROR_PARAMETERS(-1), MISSING(0), SUCCESS(1), SYSTEM_ERROR(2),
    CONNECTION_REJECTED(3);

    private Integer code;

    private ResultCode(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
