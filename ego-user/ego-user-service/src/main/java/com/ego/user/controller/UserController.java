package com.ego.user.controller;

import com.ego.user.pojo.User;
import com.ego.user.service.UserSerive;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author TheKing
 * @Date 2019/10/10 14:42
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RestController
public class UserController {

    @Autowired
    private UserSerive userSerive;

    /**
     * 根据类型校验用户名是否存在，校验手机号发送验证码
     *
     * @param data
     * @param type
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data, @PathVariable("type") Integer type) {
        Boolean result = userSerive.checkData(data, type);
        return ResponseEntity.ok(result);
    }

    @PostMapping("send")
    public ResponseEntity<Void> send(@RequestParam("phone")String phone){
        userSerive.sendSms(phone);
        return ResponseEntity.ok().build();
    }

    @PostMapping("register")
    public ResponseEntity<Void>register(@Valid User user,@RequestParam("code")String code){
        Boolean result=userSerive.register(user,code);
        if (!result){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
