package org.raniu.device.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.raniu.api.client.ProjectClient;
import org.raniu.api.client.SensorClient;
import org.raniu.api.dto.DeviceDTO;
import org.raniu.api.dto.ProjectDTO;
import org.raniu.api.dto.SensorDTO;
import org.raniu.api.enums.ResultCode;
import org.raniu.api.vo.Result;
import org.raniu.common.utils.RedisUtil;
import org.raniu.common.utils.UserContext;
import org.raniu.device.domain.po.DevicePo;
import org.raniu.device.domain.vo.ControlVo;
import org.raniu.device.domain.vo.DeviceVo;
import org.raniu.device.mapper.DeviceMapper;
import org.raniu.device.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author raniu
 * @since 2023-12-11
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, DevicePo> implements DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;

    @Resource
    private ProjectClient projectClient;

    @Autowired
    private SensorClient sensorClient;

    @Autowired
    private RedisUtil redisUtil;

    protected Page<DevicePo> list(Integer page, Integer size) {
        Page<DevicePo> devicePage = new Page<>(page, size);
        QueryWrapper<DevicePo> queryWrapper = new QueryWrapper<>();
        if (UserContext.getPermissions() == 1) {
            queryWrapper.eq("create_user", UserContext.getUser());
        } else if (UserContext.getPermissions() == 0) {
            try {
                Result<List<ProjectDTO>> listResult = projectClient.list(1, -1);
                List<String> projects = new ArrayList<>();
                for (ProjectDTO projectDTO : listResult.getData()) {
                    projects.add(projectDTO.getId());
                }
                queryWrapper.in("owner", projects);
            }catch (Exception e){
                return null;
            }
        }
        return this.deviceMapper.selectPage(devicePage, queryWrapper);
    }


    protected Page<DevicePo> select(List<String> keys, List<String> values, Integer size, Integer page) {
        Page<DevicePo> devicePage = new Page<>(page, size);
        QueryWrapper<DevicePo> queryWrapper = new QueryWrapper<>();
        if (UserContext.getPermissions() == 1) {
            queryWrapper.eq("create_user", UserContext.getUser());
        }
        Iterator<String> keysIterator = keys.iterator();
        Iterator<String> valueIterator = values.iterator();
        Map<String,Object> map = new HashMap<>();
        while (keysIterator.hasNext() && valueIterator.hasNext()) {
            map.put(keysIterator.next(), valueIterator.next());
        }
        queryWrapper.allEq(map);
        return this.deviceMapper.selectPage(devicePage, queryWrapper);
    }

    protected String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            stringBuilder.append(str.charAt(number));
        }
        return stringBuilder.toString();
    }

    @Override
    public Result<DeviceDTO> addDevice(DeviceVo deviceVo, HttpServletResponse response) {
        DevicePo device = new DevicePo();
        device.setId(deviceVo.getId());
        device.setName(deviceVo.getName());
        device.setOwner(deviceVo.getOwner());
        device.setProtocol(deviceVo.getProtocol());
        device.setCollectionTime(deviceVo.getCollectionTime());
        device.setTransferKey(getRandomString(25));
        if (!this.save(device)) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "添加失败，请检查id是否重复");
        }
        return Result.success("添加成功");
    }

    @Override
    public Result<DeviceDTO> updateDevice(DeviceVo deviceVo, HttpServletResponse response) {
        DevicePo device = new DevicePo();
        device.setOwner(deviceVo.getOwner());
        device.setName(deviceVo.getName());
        device.setId(deviceVo.getId());
        device.setProtocol(deviceVo.getProtocol());
        device.setCollectionTime(deviceVo.getCollectionTime());
        if (this.updateById(device)) {
            return Result.success("更新成功");
        }
        response.setStatus(412);
        return Result.error(ResultCode.ERROR_PARAMETERS, "更新失败");
    }

    @Override
    public Result<DeviceDTO> deleteDevice(String id, HttpServletResponse response) {
        if (id == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        if (this.removeById(id)) {
            return Result.success("删除成功");
        }
        response.setStatus(412);
        return Result.error(ResultCode.ERROR_PARAMETERS, "未找到设备");
    }

    @Override
    public Result<List<DeviceDTO>> deviceList(Integer page, Integer size, HttpServletResponse response) {
        if (page == null || size == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        Page<DevicePo> devices = list(page, size);
        if (devices == null){
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "获取失败");
        }
        if (!devices.getRecords().isEmpty()) {
            return Result.success(JSONUtil.toList(JSONUtil.toJsonStr(devices.getRecords()), DeviceDTO.class), devices.getPages());
        }
        response.setStatus(412);
        return Result.error(ResultCode.ERROR_PARAMETERS, "获取失败");
    }

    @Override
    public Result<DeviceDTO> getDevice(String id, HttpServletResponse response) {
        if (id == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        DevicePo device = this.getById(id);
        if (device == null) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "未查询到设备");
        }
        if (UserContext.getPermissions() == 0){
            if (!this.projectClient.getProject(device.getOwner()).getData().getOwner().equals(UserContext.getUser())) {
                response.setStatus(403);
                return Result.error(ResultCode.PERMISSIONS_ERROR, "权限不足");
            }
        }else if (UserContext.getPermissions() == 1){
            if (!device.getCreateUser().equals(UserContext.getUser())) {
                response.setStatus(403);
                return Result.error(ResultCode.PERMISSIONS_ERROR, "权限不足");
            }
        }
        return Result.success(JSONUtil.toBean(JSONUtil.toJsonStr(device), DeviceDTO.class));
    }

    @Override
    public Result<List<DeviceDTO>> selectDevice(List<String> keys,List<String> values, Integer page, Integer size, HttpServletResponse response) {
        if (keys == null || values == null || keys.size() != values.size()) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        if (UserContext.getPermissions() == 1){
            if (keys.contains("create_user")){
                response.setStatus(403);
                return Result.error(ResultCode.ERROR_PARAMETERS,"不符合权限的参数");
            }
        }else if (UserContext.getPermissions() == 0){
            if (keys.contains("owner") || keys.contains("create_user")){
                response.setStatus(403);
                return Result.error(ResultCode.ERROR_PARAMETERS,"不符合权限的参数");
            }
        }
        Page<DevicePo> devicePoPage = this.select(keys, values, size, page);
        if (devicePoPage == null) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "未查询到设备");
        }
        if (UserContext.getPermissions() == 0){
            Result<List<ProjectDTO>> list = this.projectClient.list(1, -1);
            Set<String> set = new HashSet<>();
            for (ProjectDTO projectDTO : list.getData()){
                set.add(projectDTO.getId());
            }
            for (int i = 0; i < devicePoPage.getRecords().size();i++) {
                if (!set.contains(devicePoPage.getRecords().get(i).getOwner())){
                    devicePoPage.getRecords().remove(i);
                    i--;
                }
            }
        }
        return Result.success(JSONUtil.toList(JSONUtil.toJsonStr(devicePoPage.getRecords()), DeviceDTO.class), devicePoPage.getPages());
    }

    @Override
    public Result<String> control(String device, ControlVo controlVo, HttpServletResponse response) {
        if (device.isEmpty()) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        DevicePo device1 = this.getById(device);
        if (redisUtil.getHashEntries(device).isEmpty()) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "设备不在线");
        }
        if ("TCP-T".equals(device1.getProtocol()) && controlVo.getTag() == null) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("device", device);
            jsonObject1.put("command", controlVo.getValue());
            jsonObject1.put("cmdid", this.getRandomString(5) + "-" + this.getRandomString(5) + "-" + this.getRandomString(5) + "-" + this.getRandomString(5) + "-");
            redisUtil.sendMsg("tc", jsonObject1.toString());
            return Result.success("发送成功");
        }
        SensorDTO sensor = sensorClient.getSensor(controlVo.getTag(),device).getData();
        if (sensor == null) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "未查询到执行器");
        }
        if ("TCP-T".equals(device1.getProtocol())) {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("cmdid", this.getRandomString(5) + "-" + this.getRandomString(5) + "-" + this.getRandomString(5) + "-" + this.getRandomString(5) + "-");
            jsonObject1.put("device", device);
            if (sensor.getType() == 2) {
                StringBuilder command = new StringBuilder(Integer.toHexString(Integer.parseInt(controlVo.getValue())));
                while (command.length() % 4 != 0)
                    command.insert(0, "0");
                jsonObject1.put("command", command.toString());
            } else {
                if ("1".equals(controlVo.getValue())) {
                    jsonObject1.put("command", sensor.getCon());
                } else {
                    jsonObject1.put("command", sensor.getCoff());
                }
            }
            jsonObject1.put("start", sensor.getStart());
            jsonObject1.put("tag", sensor.getId());
            String fun = sensor.getFunc();
            if ("01".equals(fun)) {
                jsonObject1.put("func", "05");
            } else if ("03".equals(fun)) {
                jsonObject1.put("func", "06");
            }
            jsonObject1.put("addr", sensor.getAddr());
            redisUtil.sendMsg("tc", jsonObject1.toString());
        } else {
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("cmdid", this.getRandomString(5) + "-" + this.getRandomString(5) + "-" + this.getRandomString(5) + "-" + this.getRandomString(5) + "-");
            jsonObject1.put("device", device);
            jsonObject1.put("tag", controlVo.getTag());
            jsonObject1.put("data", Integer.valueOf(controlVo.getValue()));
            redisUtil.sendMsg("control", jsonObject1.toString());
        }

        return Result.success("发送成功");
    }
}
