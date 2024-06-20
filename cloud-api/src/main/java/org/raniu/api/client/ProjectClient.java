package org.raniu.api.client;

import org.raniu.api.dto.ProjectDTO;
import org.raniu.api.vo.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;


/**
 * @projectName: IOTCloud
 * @package: org.raniu.api.client
 * @className: ProjectClient
 * @author: Raniu
 * @description: TODO
 * @date: 2024/5/17 15:12
 * @version: 1.0
 */

@FeignClient("project-service")
public interface ProjectClient {

    @GetMapping("/project/get")
    Result<ProjectDTO> getProject(@RequestParam("id") String id);

    @GetMapping("/project/select/precise")
    Result<List<ProjectDTO>> selectPrecise(@RequestParam("keys") Collection<String> keys, @RequestParam("values") Collection<String> values, @RequestParam("page") Integer page, @RequestParam("size") Integer size);

    @GetMapping("/project/list")
    Result<List<ProjectDTO>> list(@RequestParam("page") Integer page, @RequestParam("size") Integer size);

}
