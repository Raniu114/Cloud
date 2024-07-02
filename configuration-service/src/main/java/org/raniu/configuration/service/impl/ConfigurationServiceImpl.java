package org.raniu.configuration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.raniu.api.client.ProjectClient;
import org.raniu.api.dto.ProjectDTO;
import org.raniu.api.enums.ResultCode;
import org.raniu.api.vo.Result;
import org.raniu.common.enums.PermissionsEnum;
import org.raniu.common.utils.UserContext;
import org.raniu.configuration.domain.po.ConfigurationPo;
import org.raniu.configuration.domain.vo.ConfigurationVo;
import org.raniu.configuration.mapper.ConfigurationMapper;
import org.raniu.configuration.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author raniu
 * @since 2024-01-10
 */
@Service
public class ConfigurationServiceImpl extends ServiceImpl<ConfigurationMapper, ConfigurationPo> implements ConfigurationService {

    @Resource
    private ProjectClient projectClient;

    @Autowired
    private ConfigurationMapper configurationMapper;

    public Page<ConfigurationPo> list(Integer page, Integer size, Long userId, Integer permission, String projectId) {
        Page<ConfigurationPo> configurationPoPage = new Page<>(page, size);
        QueryWrapper<ConfigurationPo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", projectId);
        if (permission == PermissionsEnum.USER.ordinal()) {
            queryWrapper.eq("owner", userId);
        } else if (permission == PermissionsEnum.ADMIN.ordinal()) {
            queryWrapper.eq("create_user", userId);
        } else if (permission == PermissionsEnum.SUPER_ADMIN.ordinal()) {
            return this.configurationMapper.selectPage(configurationPoPage, queryWrapper);
        } else {
            return null;
        }
        return this.configurationMapper.selectPage(configurationPoPage, queryWrapper);
    }

    @Override
    public Result<List<ConfigurationPo>> configurationList(String projectId, Integer page, Integer size, HttpServletResponse response) {
        try {
            projectClient.getProject(projectId);
        } catch (Exception e) {
            response.setStatus(403);
            if (UserContext.getPermissions() < 2) {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "权限不足");
            } else {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "设备不存在");
            }

        }
        Page<ConfigurationPo> configurations = list(page, size, UserContext.getUser(), UserContext.getPermissions(), projectId);
        if (configurations == null) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "未查询到组态");
        }
        return Result.success(configurations.getRecords(), configurations.getPages());
    }

    @Override
    public Result<ConfigurationPo> addConfiguration(ConfigurationVo configurationVo, HttpServletResponse response) {
        try {
            projectClient.getProject(configurationVo.getProjectId());
        } catch (Exception e) {
            response.setStatus(403);
            if (UserContext.getPermissions() < 2) {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "权限不足");
            } else {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "设备不存在");
            }

        }
        ConfigurationPo configuration = new ConfigurationPo();
        configuration.setProjectId(configurationVo.getProjectId());
        configuration.setName(configurationVo.getName());
        configuration.setPage(configurationVo.getPage());
        if (!save(configuration)) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "添加失败，请检查id是否重复");
        }
        return Result.success("添加成功");
    }

    @Override
    public Result<ConfigurationPo> updateConfiguration(ConfigurationVo configurationVo, HttpServletResponse response) {
        try {
            projectClient.getProject(configurationVo.getProjectId());
        } catch (Exception e) {
            response.setStatus(403);
            if (UserContext.getPermissions() < 2) {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "权限不足");
            } else {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "设备不存在");
            }

        }
        ConfigurationPo configuration = new ConfigurationPo();
        configuration.setProjectId(configurationVo.getProjectId());
        configuration.setName(configurationVo.getName());
        configuration.setPage(configurationVo.getPage());
        configuration.setId(configuration.getId());
        if (!updateById(configuration)) {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "修改失败,请检查id是否正确");
        }
        return Result.success("修改成功");
    }

    @Override
    public Result<ConfigurationPo> delConfiguration(Long id, HttpServletResponse response) {
        if (id == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        try {
            projectClient.getProject(getById(id).getProjectId());
        } catch (Exception e) {
            response.setStatus(403);
            if (UserContext.getPermissions() < 2) {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "权限不足");
            } else {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "设备不存在");
            }

        }
        if (removeById(id)) {
            return Result.success("删除成功");
        }
        response.setStatus(412);
        return Result.error(ResultCode.SYSTEM_ERROR, "删除失败");
    }

    @Override
    public Result<ConfigurationPo> updateImage(String id, MultipartFile multipartFile, HttpServletResponse response) {
        if (id == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        try {
            projectClient.getProject(getById(id).getProjectId());
        } catch (Exception e) {
            response.setStatus(403);
            if (UserContext.getPermissions() < 2) {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "权限不足");
            } else {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "设备不存在");
            }

        }
        try {
            if (multipartFile.isEmpty()) {
                response.setStatus(412);
                return Result.error(ResultCode.MISSING, "文件不可为空");
            }
            String fileName = multipartFile.getOriginalFilename();
            String filePath = "/www/wwwroot/cloud/image/" + id + "/";
            File file = new File(filePath, fileName);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } else {
                response.setStatus(412);
                return Result.error(ResultCode.ERROR_PARAMETERS, "文件已存在");
            }
            multipartFile.transferTo(file);
            return Result.success("保存成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setStatus(412);
        return Result.error(ResultCode.SYSTEM_ERROR, "保存失败");
    }

    @Override
    public Result<ConfigurationPo> downloadImage(String id, String img, HttpServletResponse response) {
        if (id == null || img == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        try {
            projectClient.getProject(getById(id).getProjectId());
        } catch (Exception e) {
            response.setStatus(403);
            if (UserContext.getPermissions() < 2) {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "权限不足");
            } else {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "设备不存在");
            }

        }
        String filePath = "/www/wwwroot/cloud/image/" + id + "/";
        File file = new File(filePath, img);
        if (file.exists()) {
            response.addHeader("Content-Disposition", "inline;fileName=" + img);
            response.addHeader("Content-Length", "" + file.length());
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
                return Result.success("获取成功");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        response.setStatus(412);
        return Result.error(ResultCode.ERROR_PARAMETERS, "获取失败，请检查文件id是否正确");
    }

    @Override
    public Result<String[]> imgList(String id, HttpServletResponse response) {
        if (id == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        try {
            projectClient.getProject(getById(id).getProjectId());
        } catch (Exception e) {
            response.setStatus(403);
            if (UserContext.getPermissions() < 2) {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "权限不足");
            } else {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "设备不存在");
            }

        }
        String filePath = "/www/wwwroot/cloud/image/" + id + "/";
        File file = new File(filePath);
        File[] files;
        String[] filesList = null;
        try {
            files = file.listFiles();
            filesList = new String[files.length];
            for (int i = 0; i < files.length; i++) {
                filesList[i] = files[i].getName();
            }
        } catch (Exception e) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "对象为空");
        }
        return Result.success(filesList);
    }

    @Override
    public Result<ConfigurationPo> delImg(String id, String img, HttpServletResponse response) {
        if (id == null || img == null) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        try {
            projectClient.getProject(getById(id).getProjectId());
        } catch (Exception e) {
            response.setStatus(403);
            if (UserContext.getPermissions() < 2) {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "权限不足");
            } else {
                return Result.error(ResultCode.PERMISSIONS_ERROR, "设备不存在");
            }

        }
        String filePath = "/www/wwwroot/cloud/image/" + id + "/";
        File file = new File(filePath, img);
        if (file.exists()) {
            file.delete();
        } else {
            response.setStatus(412);
            return Result.error(ResultCode.ERROR_PARAMETERS, "未找到文件");
        }
        return Result.success("删除成功");
    }
}
