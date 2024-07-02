package org.raniu.api.client;

import org.raniu.api.dto.SensorDTO;
import org.raniu.api.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.api.client
 * @className: SensorClient
 * @author: Raniu
 * @description: TODO
 * @date: 2024/5/17 15:12
 * @version: 1.0
 */

@FeignClient("/sensor-service")
public interface SensorClient {

    @GetMapping("/sensor/get")
    Result<SensorDTO> getSensor(@RequestParam("id") String id, @RequestParam("deviceId") String deviceId);

}
