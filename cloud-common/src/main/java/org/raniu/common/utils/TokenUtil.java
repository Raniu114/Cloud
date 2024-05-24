package org.raniu.common.utils;

import io.jsonwebtoken.*;
import org.raniu.api.dto.UserDTO;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @projectName: raniu-cloud_disk
 * @package: org.raniu.utlis
 * @className: TokenUtil
 * @author: Raniu
 * @description: TODO
 * @date: 2023/10/10 10:38
 * @version: 1.0
 */
public class TokenUtil {
    private static final long ACCESS_EXPIRE_DATE = 60 * 30 * 1000;
    private static final long REFRESH_EXPIRE_DATE = 60 * 60 * 1000 * 30;
    //token秘钥
    private static final String TOKEN_SECRET = "fwa4w89GSADF485fawd48gaw54FAW89dwaF8Gad485412354aw";

    public static String accessToken(int permission, Long user, String auth) {
        Date date = new Date(System.currentTimeMillis() + ACCESS_EXPIRE_DATE);
        return Jwts.builder()
                .setId(String.valueOf(user))
                .setIssuedAt(new Date())
                .setExpiration(date)
                .claim("permission", permission)
                .claim("user", user)
                .claim("auth", auth)
                .signWith(SignatureAlgorithm.HS256, TOKEN_SECRET)
                .compact();
    }

    public static String refreshToken(Long user) {
        Date date = new Date(System.currentTimeMillis() + REFRESH_EXPIRE_DATE);
        return Jwts.builder()
                .setId(String.valueOf(user))
                .setIssuedAt(new Date())
                .setExpiration(date)
                .claim("user", user)
                .signWith(SignatureAlgorithm.HS256, TOKEN_SECRET)
                .compact();
    }

    public static Map<String,String> verify(String token) {
        Map<String,String> data = new HashMap<>();
        try {
            System.out.println(token);
            Claims claims = Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token).getBody();

            if (claims.get("permission") != null) {
                data.put("permission", claims.get("permission").toString());
                data.put("auth", claims.get("auth").toString());
            }
            data.put("user", claims.get("user").toString());
            return data;
        }catch (ExpiredJwtException e){
            data.put("user","-1");
            return data;
        } catch (MalformedJwtException e){
            data.put("user","-2");
            return data;
        }catch (SignatureException e){
            data.put("user","-3");
            return data;
        }catch (UnsupportedJwtException e){
            data.put("user","-4");
            return data;
        }
    }

    public static String getAccessToken(UserDTO user) {
        if (user == null) {
            return null;
        } else {
            return accessToken(user.getPermissions(), user.getId(), user.getAuth());
        }
    }

    public static String getRefreshToken(UserDTO user) {
        if (user == null) {
            return null;
        } else {
            return refreshToken(user.getId());
        }
    }

    public static String verifyPermissions(String token) {
        Map<String, String> map = verify(token);
        return map.get("permission");
    }

    public static String verifyUser(String token) {
        Map<String, String> map = verify(token);
        return map.get("user");
    }

    public static String verifyAuth(String token) {
        Map<String, String> map = verify(token);
        return map.get("auth");
    }
}
