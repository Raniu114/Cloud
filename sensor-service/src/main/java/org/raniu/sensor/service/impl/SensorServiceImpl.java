package org.raniu.sensor.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.raniu.api.dto.DeviceDTO;
import org.raniu.api.dto.SensorDTO;
import org.raniu.api.enums.ResultCode;
import org.raniu.api.vo.Result;
import org.raniu.common.utils.UserContext;
import org.raniu.sensor.domain.po.SensorPo;
import org.raniu.sensor.domain.vo.SensorVo;
import org.raniu.sensor.mapper.SensorMapper;
import org.raniu.sensor.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author raniu
 * @since 2023-12-11
 */
@Service
public class SensorServiceImpl extends ServiceImpl<SensorMapper, SensorPo> implements SensorService {
    @Autowired
    private SensorMapper sensorMapper;

    public Page<SensorPo> list(Integer page, Integer size, String deviceId) {
        Page<SensorPo> sensorPage = new Page<>(page, size);
        QueryWrapper<SensorPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner", deviceId);
        return this.sensorMapper.selectPage(sensorPage, queryWrapper);
    }

    public Page<SensorPo> listAsType(Integer page, Integer size, String deviceId, Integer type) {
        Page<SensorPo> sensorPage = new Page<>(page, size);
        QueryWrapper<SensorPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner", deviceId).eq("type", type);
        return this.sensorMapper.selectPage(sensorPage, queryWrapper);
    }

    public Page<SensorPo> select(String key, String deviceId, Integer page, Integer size) {
        Page<SensorPo> sensorPage = new Page<>(page, size);
        QueryWrapper<SensorPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", key).eq("owner", deviceId);
        return this.sensorMapper.selectPage(sensorPage, queryWrapper);
    }

    @Override
    public Result<String> addSensor(SensorVo sensorVo, HttpServletResponse response) {
        if (sensorVo.getType() != 1 && sensorVo.getType() != 0) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "添加失败，传感器类型不合法");
        }
        SensorPo sensor = new SensorPo();
        sensor.setId(sensorVo.getId());
        sensor.setName(sensorVo.getName());
        sensor.setOwner(sensorVo.getOwner());
        sensor.setType(sensorVo.getType());
        sensor.setUnit(sensorVo.getUnit());
        sensor.setLen(sensorVo.getLen());
        sensor.setCon(sensorVo.getCon());
        sensor.setCoff(sensorVo.getCoff());
        sensor.setFunc(sensorVo.getFunc());
        sensor.setAddr(sensorVo.getAddr());
        sensor.setStart(sensorVo.getStart());
        sensor.setFormula(sensorVo.getFormula());
        if (!this.save(sensor)) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "添加失败，请检查id是否重复");
        }
        return Result.success("添加成功");
    }

    @Override
    public Result<String> updateSensor(SensorVo sensorVo, HttpServletResponse response) {
        if (sensorVo.getType() != 1 && sensorVo.getType() != 0) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "更新失败，传感器类型不合法");
        }
        SensorPo sensor = new SensorPo();
        sensor.setId(sensorVo.getId());
        sensor.setName(sensorVo.getName());
        sensor.setOwner(sensorVo.getOwner());
        sensor.setType(sensorVo.getType());
        sensor.setUnit(sensorVo.getUnit());
        sensor.setLen(sensorVo.getLen());
        sensor.setCon(sensorVo.getCon());
        sensor.setCoff(sensorVo.getCoff());
        sensor.setFunc(sensorVo.getFunc());
        sensor.setAddr(sensorVo.getAddr());
        sensor.setStart(sensorVo.getStart());
        sensor.setFormula(sensorVo.getFormula());
        if (this.updateById(sensor)) {
            return Result.success("更新成功");
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "更新失败");
        }
    }

    @Override
    public Result<String> deleteSensor(String id, String deviceId, HttpServletResponse response) {
        if (id == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("owner", deviceId);
        if (this.removeByMap(map)) {
            return Result.success("删除成功");
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "删除失败");
        }
    }

    @Override
    public Result<List<SensorDTO>> sensorList(Integer page, Integer size, String deviceId, Integer type, HttpServletResponse response) {
        if (page == null || size == null || deviceId == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        Page<SensorPo> devices;
        if (type != null) {
            if (type == 1 || type == 0) {
                devices = this.listAsType(page, size, deviceId, type);
            } else {
                response.setStatus(412);
                return Result.error(ResultCode.ERROR_PARAMETERS, "获取失败，传感器类型不合法");
            }

        } else {
            devices = this.list(page, size, deviceId);
        }
        if (!devices.getRecords().isEmpty()) {
            return Result.success(JSONUtil.toList(JSONUtil.toJsonStr(devices.getRecords()), SensorDTO.class), devices.getPages());
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "获取失败");
        }
    }

    @Override
    public Result<SensorDTO> getSensor(String id, String deviceId, HttpServletResponse response) {
        if (id == null || deviceId == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        QueryWrapper<SensorPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("owner", deviceId).eq("id", id);
        SensorPo sensor = this.getOne(queryWrapper);
        if (sensor == null) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "未查询到传感器");
        }
        return Result.success(JSONUtil.toBean(JSONUtil.toJsonStr(sensor), SensorDTO.class));
    }

    @Override
    public Result<List<SensorDTO>> selectSensor(String key, String deviceId, Integer page, Integer size, HttpServletResponse response) {
        if (key == null || deviceId == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        Page<SensorPo> sensors;
        sensors = this.select(key, deviceId, page, size);
        if (!sensors.getRecords().isEmpty()) {
            return Result.success(JSONUtil.toList(JSONUtil.toJsonStr(sensors.getRecords()), SensorDTO.class), sensors.getPages());
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "获取失败");
        }
    }

}
