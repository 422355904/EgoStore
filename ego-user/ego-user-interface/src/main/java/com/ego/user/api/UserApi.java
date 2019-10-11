package com.ego.user.api;

import com.ego.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * @Author TheKing
 * @Date 2019/10/11 16:26
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
public interface UserApi {

    /**
     * 根据类型校验用户名是否存在，校验手机号发送验证码
     *
     * @param data
     * @param type
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data, @PathVariable("type") Integer type);

    @PostMapping("send")
    public ResponseEntity<Void> send(@RequestParam("phone")String phone);

    @PostMapping("register")
    public ResponseEntity<Void>register(@Valid User user, @RequestParam("code")String code);

    /**
     * 校验用户登录
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(@RequestParam("username") String username,@RequestParam("password") String password);
}
