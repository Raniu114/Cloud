package org.raniu.user.service.impl;

import org.raniu.api.dto.UserDTO;
import org.raniu.common.utils.TokenUtil;
import org.raniu.user.service.TokenService;
import org.springframework.stereotype.Service;


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

    @Override
    public String getAccessToken(UserDTO user) {
        if (user == null) {
            return null;
        } else {
            return TokenUtil.getAccessToken(user);
        }
    }

    @Override
    public String getRefreshToken(UserDTO user) {
        if (user == null) {
            return null;
        } else {
            return TokenUtil.getRefreshToken(user);
        }
    }

    @Override
    public int verifyPermissions(String token) {
        return Integer.parseInt(TokenUtil.verifyPermissions(token));
    }

    @Override
    public Long verifyUser(String token) {
        return Long.parseLong(TokenUtil.verifyUser(token));
    }
}
