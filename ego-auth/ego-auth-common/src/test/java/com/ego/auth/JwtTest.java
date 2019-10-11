package com.ego.auth;

import com.ego.auth.pojo.UserInfo;
import com.ego.auth.utils.JwtUtils;
import com.ego.auth.utils.RsaUtils;
import io.jsonwebtoken.JwtBuilder;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @Author TheKing
 * @Date 2019/10/11 15:21
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
public class JwtTest {

    private static final String pubKeyPath = "D:\\JavaStudy\\Course3\\7-ego\\temp\\rsa.pub";
    private static final String priKeyPath = "D:\\JavaStudy\\Course3\\7-ego\\temp\\rsa.pri";

    private PublicKey publicKey;
    private PrivateKey privateKey;

    /**
     * 注释@Before代码运行
     * @throws Exception
     */
    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }
    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(29L, "adminadmin"),
                privateKey, 5);
        System.out.println("token = " + token);
    }
    @Test
    public void testParseToken() throws Exception {
        String token ="eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU3MTIxMzMxOH0.fNU9G2V9KyZGBTfDkTPrVQpgd0_gEBtPFvY3tFU5Uaud7o8rEs4wLUyynO3g57DW6UHfVLNVQCmnW5kdmzPxCmp6XBsO7PcM6poGhHqLs1P-oYF8BiqwikGqs9nNRDHMRe5D0hOZvEz7EXWcblrlwnbzMOaNmrrZYYb8ty1Dd14";
        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }


}
