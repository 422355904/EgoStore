package com.ego.gateway.filter;

import com.ego.auth.utils.JwtUtils;
import com.ego.common.utils.CookieUtils;
import com.ego.gateway.config.FilterProperties;
import com.ego.gateway.config.JwtProperties;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * @Author TheKing
 * @Date 2019/10/11 18:17
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@Component
@EnableConfigurationProperties({JwtProperties.class, FilterProperties.class})
public class LoginFilter extends ZuulFilter {

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private FilterProperties filterProperties;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        String uri = request.getRequestURI();
        //判断 uri-->/api/search/page 是否属于白名单
        boolean result = filterProperties.getAllowPaths().stream().anyMatch(path -> uri.startsWith(path));
        //不属于才拦截
        return !result;
    }

    @Override
    public Object run() throws ZuulException {

        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        //取出cookie中的token鉴权
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        try {
            //公钥解密，报错证明解析异常，鉴权失败
            JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
        } catch (Exception e) {
            // 关闭发送zuul自动响应500
            context.setSendZuulResponse(false);
            // 校验出现异常，返回401
            context.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
        }
        return null;
    }
}
