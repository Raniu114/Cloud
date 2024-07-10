package org.raniu.configuration.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletResponse;
import org.raniu.api.vo.Result;
import org.raniu.configuration.domain.po.ConfigurationPo;
import org.raniu.configuration.domain.vo.ConfigurationVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author raniu
 * @since 2024-01-10
 */
public interface ConfigurationService extends IService<ConfigurationPo> {

    Result<List<ConfigurationPo>> configurationList(String projectId,Integer page, Integer size, HttpServletResponse response);

    Result<ConfigurationPo> addConfiguration(ConfigurationVo configurationVo, HttpServletResponse response);

    Result<ConfigurationPo> updateConfiguration(Long id,ConfigurationVo configurationVo, HttpServletResponse response);

    Result<ConfigurationPo> delConfiguration(Long id, HttpServletResponse response);

    Result<ConfigurationPo> updateImage(String id, MultipartFile multipartFile, HttpServletResponse response);

    Result<ConfigurationPo> downloadImage(String id, String img, HttpServletResponse response);

    Result<String[]> imgList(String id, HttpServletResponse response);

    Result<ConfigurationPo> delImg(String id, String img, HttpServletResponse response);
}
