package com.ego.gateway.config;

import com.ego.auth.utils.RsaUtils;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;
import java.security.PublicKey;

/**
 * @Author TheKing
 * @Date 2019/10/11 18:14
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Data
@ConfigurationProperties("ego.jwt")
public class JwtProperties {
    private String pubKeyPath;// 公钥
    private PublicKey publicKey; // 公钥
    private String cookieName;
    private static final Logger logger =
            LoggerFactory.getLogger(JwtProperties.class);

    @PostConstruct
    public void init() {
        try {
            // 获取公钥
            this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        } catch (Exception e) {
            logger.error("初始化公钥失败！", e);
            throw new RuntimeException("初始化公钥失败！");
        }
    }
}
