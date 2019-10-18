package com.ego.auth.controller;

import com.ego.auth.config.JwtProperties;
import com.ego.auth.pojo.UserInfo;
import com.ego.auth.service.AuthService;
import com.ego.auth.utils.JwtUtils;
import com.ego.common.utils.CookieUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author TheKing
 * @Date 2019/10/11 15:48
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private JwtProperties jwtProperties;

    @PostMapping("accredit")
    public ResponseEntity<Void> authentication(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            HttpServletResponse response) {
        //验证用户账号密码
        String token = authService.authentication(username, password);
        if (StringUtils.isBlank(token)) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).build();
        }
        //将token存入cookie
        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), token, jwtProperties.getCookieMaxAge(), true);

        return ResponseEntity.status(HttpStatus.SC_OK).build();
    }

    /**
     * 单点登录显示用户信息
     *
     * @param tokenCookie
     * @return
     */
    @GetMapping("verify")
    public ResponseEntity<UserInfo> verifyUser(@CookieValue("EGO_TOKEN") String tokenCookie, HttpServletRequest request, HttpServletResponse response) {
        UserInfo userInfo = null;
        try {

            userInfo = JwtUtils.getInfoFromToken(tokenCookie, jwtProperties.getPublicKey());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_UNAUTHORIZED).build();
        }
        //刷新token，提升用户体验
        authService.refushToken(userInfo,request,response);
        return ResponseEntity.ok(userInfo);
    }
}
