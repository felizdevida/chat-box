package com.box.implatform.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import java.util.Date;


public class JwtUtil {

    /**
     * 生成jwt字符串，30分钟后过期  JWT(json web token)
     * @param userId
     * @param info
     * @param expireIn
     * @param secret
     * @return
     * */
    public static String sign(Long userId, String info,long expireIn,String secret) {
        try {
            Date date = new Date(System.currentTimeMillis() + expireIn*1000);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    //将userId保存到token里面
                    .withAudience(userId.toString())
                    //存放自定义数据
                    .withClaim("info", info)
                    //五分钟后token过期
                    .withExpiresAt(date)
                    //token的密钥
                    .sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据token获取userId
     * @param token
     * @return
     * */
    public static Long getUserId(String token) {
        try {
            String userId = JWT.decode(token).getAudience().get(0);
            return Long.parseLong(userId);
        }catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 根据token获取自定义数据info
     * @param token
     * @return
     * */
    public static String getInfo(String token) {
        try {
            return JWT.decode(token).getClaim("info").asString();
        }catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 校验token
     * @param token
     * @param secret
     * @return
     * */
    public static boolean checkSign(String token,String secret) {
            Algorithm algorithm  = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    //.withClaim("username, username)
                    .build();
            verifier.verify(token);
            return true;
    }
}
