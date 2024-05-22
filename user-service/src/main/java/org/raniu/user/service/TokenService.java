package org.raniu.user.service;

import org.raniu.user.domain.po.UserPo;

/**
 * @projectName: cloud
 * @package: org.raniu.service
 * @className: TokenService
 * @author: Raniu
 * @description: TODO
 * @date: 2023/12/11 17:13
 * @version: 1.0
 */
public interface TokenService {

    String getAccessToken(UserPo user);

    String getRefreshToken(UserPo user);

    int verifyPermissions(String token);

    Long verifyUser(String token);
}
