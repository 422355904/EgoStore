package com.ego.user.controller;

import com.ego.user.pojo.Addrs;
import com.ego.user.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author TheKing
 * @Date 2019/10/16 17:35
 * @Version 1.0
 * ⊰愤怒，并不会使你变强⊱
 */
@RestController
@RequestMapping("userDetail")
public class UserDetailController {

    @Autowired
    private UserDetailService userDetailService;

    @PostMapping
    public ResponseEntity<Void> createAddrs(@RequestBody Addrs addrs){
        try {
            userDetailService.createAddrs(addrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Addrs>> queryAddrs(){
        try {
            return ResponseEntity.ok(userDetailService.queryAddrs());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAddrs(@RequestParam("id")Integer id){
        try {
            userDetailService.deleteAddrs(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }
}
