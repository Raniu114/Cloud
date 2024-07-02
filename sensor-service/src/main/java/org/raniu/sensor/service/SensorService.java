package org.raniu.sensor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.raniu.api.dto.SensorDTO;
import org.raniu.api.vo.Result;
import org.raniu.sensor.domain.po.SensorPo;
import org.raniu.sensor.domain.vo.SensorVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author raniu
 * @since 2023-12-11
 */
public interface SensorService extends IService<SensorPo> {


    Result<String> addSensor(SensorVo sensorVo, HttpServletResponse response);

    Result<String> updateSensor(SensorVo sensorVo, HttpServletResponse response);

    Result<String> deleteSensor(String id, String deviceId, HttpServletResponse response);

    Result<List<SensorDTO>> sensorList(Integer page, Integer size, String deviceId, Integer type, HttpServletResponse response);

    Result<SensorDTO> getSensor(String id, String deviceId, HttpServletResponse response);

    Result<List<SensorDTO>> selectSensor(String key, String deviceId, Integer page, Integer size, HttpServletResponse response);

}
