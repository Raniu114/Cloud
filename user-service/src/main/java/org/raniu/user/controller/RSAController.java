package org.raniu.user.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "RSA接口",description = "RSA接口")
public class RSAController {
    @Autowired
    private RSAService rsaService;

    @GetMapping("/get")
    @Operation(summary = "获取RSA公钥",description = "获取公钥")
    public String get(){
        return rsaService.getPublicKey();
    }
}
