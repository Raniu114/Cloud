package org.raniu.user.service.impl;

import org.raniu.common.utils.TokenUtil;
import org.raniu.user.domain.po.UserPo;
import org.raniu.user.service.TokenService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @projectName: cloud
 * @package: org.raniu.service.impl
 * @className: TokenServiceImpl
 * @author: Raniu
 * @description: TODO
 * @date: 2023/12/11 17:14
 * @version: 1.0
 */
@Service
public class TokenServiceImpl implements TokenService {

    protected TokenUtil tokenUtil = new TokenUtil();

    @Override
    public String getAccessToken(UserPo user) {
        if (user == null) {
            return null;
        } else {
            return tokenUtil.accessToken(user.getPermissions(), user.getId());
        }
    }

    @Override
    public String getRefreshToken(UserPo user) {
        if (user == null) {
            return null;
        } else {
            return tokenUtil.refreshToken(user.getId());
        }
    }

    @Override
    public int verifyPermissions(String token) {
        Map<String, String> map = tokenUtil.verify(token);
        if (map != null) {
            return Integer.parseInt(map.get("permission"));
        } else {
            return -1;
        }
    }

    @Override
    public Long verifyUser(String token) {
        Map<String, String> map = tokenUtil.verify(token);
        if (map != null) {
            return Long.valueOf(map.get("user"));
        } else {
            return null;
        }
    }
}
