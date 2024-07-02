package org.raniu.device.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.raniu.api.dto.DeviceDTO;
import org.raniu.api.vo.Result;
import org.raniu.device.domain.po.DevicePo;
import org.raniu.device.domain.vo.ControlVo;
import org.raniu.device.domain.vo.DeviceVo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author raniu
 * @since 2023-12-11
 */
public interface DeviceService extends IService<DevicePo> {

    Result<DeviceDTO> addDevice(DeviceVo deviceVo, HttpServletResponse response);

    Result<DeviceDTO> updateDevice(DeviceVo deviceVo, HttpServletResponse response);

    Result<DeviceDTO> deleteDevice(String id, HttpServletResponse response);

    Result<List<DeviceDTO>> deviceList(Integer page, Integer size, HttpServletResponse response);

    Result<DeviceDTO> getDevice(String id, HttpServletResponse response);

    Result<List<DeviceDTO>> selectDevice(List<String> keys, List<String> values, Integer page, Integer size, HttpServletResponse response);

    Result<String> control(String device, ControlVo controlVo, HttpServletResponse response);
}
