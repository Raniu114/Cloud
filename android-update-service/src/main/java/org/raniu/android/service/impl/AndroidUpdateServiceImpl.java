package org.raniu.android.service.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.raniu.android.service.AndroidUpdateService;
import org.raniu.api.enums.ResultCode;
import org.raniu.api.vo.Result;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Arrays;

/**
 * @projectName: IOTCloud
 * @package: org.raniu.android.service.impl
 * @className: AndroidUpdateServiceImpl
 * @author: Raniu
 * @description: TODO
 * @date: 2024/6/26 15:22
 * @version: 1.0
 */

@Service
public class AndroidUpdateServiceImpl implements AndroidUpdateService {
    @Override
    public Result<String> getVersion(String name, HttpServletResponse response) {
        if (StringUtils.isBlank(name)) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        String filePath = "/www/wwwroot/cloud/apk/" + name + "/";
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
        int max = 0;
        for (String fileName : filesList) {
            int version = Integer.parseInt(fileName);
            if (version > max) {
                max = Integer.parseInt(fileName);
            }
        }
        if (max == 0) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "没有版本");
        }
        return Result.success("获取成功",String.valueOf(max));
    }

    @Override
    public Result<String> getApk(String name, String version, HttpServletResponse response) {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(version)) {
            response.setStatus(412);
            return Result.error(ResultCode.MISSING, "参数不可为空");
        }
        String filePath = "/www/wwwroot/cloud/apk/" + name + "/" + version + "/";
        File file = new File(filePath, name + ".apk");
        if (file.exists()) {
            response.addHeader("Content-Length", "" + file.length());
            response.addHeader("Content-Disposition", "inline;fileName=" + name + ".apk");
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
                return Result.success("获取成功",version);
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
}
