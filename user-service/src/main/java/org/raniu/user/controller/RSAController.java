package org.raniu.user.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.raniu.user.service.RSAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @projectName: cloud
 * @package: org.raniu.controller
 * @className: RSAController
 * @author: Raniu
 * @description: TODO
 * @date: 2023/12/11 17:00
 * @version: 1.0
 */
@RestController
@CrossOrigin
@RequestMapping("/RSA")
@Api(tags = "RSA接口",value = "RSA接口")
public class RSAController {
    @Autowired
    private RSAService rsaService;

    @GetMapping("/get")
    @ApiOperation(notes = "获取RSA公钥",value = "获取公钥")
    public String get(){
        return rsaService.getPublicKey();
    }
}
