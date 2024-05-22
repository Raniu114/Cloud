package org.raniu.api.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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



}
