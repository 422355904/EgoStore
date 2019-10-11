package com.ego.auth.service;

import com.ego.auth.client.UserClient;
import com.ego.auth.config.JwtProperties;
import com.ego.auth.pojo.UserInfo;
import com.ego.auth.utils.JwtUtils;
import com.ego.common.utils.CookieUtils;
import com.ego.user.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author TheKing
 * @Date 2019/10/11 15:49
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Service
@Slf4j
public class AuthService {

    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties properties;

    public String authentication(String username, String password) {
        String token ="";
        User user = userClient.queryUser(username, password).getBody();
        if (null == user) { //用户不存在或者账号密码错误
            //不进行授权
            return null;
        }

        try {
            //进行授权
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(user.getUsername());
            userInfo.setId(user.getId());
            token = JwtUtils.generateToken(userInfo, properties.getPrivateKey(), properties.getExpire());
        } catch (Exception e) {
            log.error("生成Token失败！", e);
            return token;
        }
        return token;
    }

    /**
     * 刷新token，提升用户体验
     * @param userInfo
     * @param request
     * @param response
     */
    public void refushToken(UserInfo userInfo, HttpServletRequest request, HttpServletResponse response) {
        try {
        String token ="";
        // 解析成功要重新刷新token
            token = JwtUtils.generateToken(userInfo, this.properties.getPrivateKey(),
                    this.properties.getExpire());
        // 更新cookie中的token
        CookieUtils.setCookie(request, response, this.properties.getCookieName(),
                token, this.properties.getCookieMaxAge());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
