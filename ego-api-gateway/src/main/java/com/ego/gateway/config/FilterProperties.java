package com.ego.gateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/10/11 21:39
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Data
@ConfigurationProperties("ego.filter")
public class FilterProperties {

    private List<String>allowPaths;

}
