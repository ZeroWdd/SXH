package com.leyou.user.api;

import com.leyou.user.pojo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

public interface UserApi {

    @GetMapping("/query")
    User queryUser(
        @RequestParam("username") String username,
        @RequestParam("password") String password);

    @PutMapping("/")
    void updateUserPassword(@RequestParam("id") Long id,@RequestParam("password") String password);
}