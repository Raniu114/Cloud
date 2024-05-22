package org.raniu.user.service.impl;

import cn.hutool.core.io.IoUtil;
import org.json.JSONObject;

import org.raniu.common.key.KeyMap;
import org.raniu.user.service.RSAService;
import org.raniu.common.utils.RSAUtil;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: cloud
 * @package: org.raniu.service
 * @className: RSAServiceImpl
 * @author: Raniu
 * @description: TODO
 * @date: 2023/12/11 16:56
 * @version: 1.0
 */
@Service
public class RSAServiceImpl implements RSAService {

    @Override
    public String getPublicKey() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("publicKey", KeyMap.PUBLIC_KEY);
        jsonObject.put("status", 1);
        return jsonObject.toString();
    }

    @Override
    public String decrypt(String data) throws Exception {
        return RSAUtil.decryptByPrivateKey(data, KeyMap.PRIVATE_KEY);
    }

}
