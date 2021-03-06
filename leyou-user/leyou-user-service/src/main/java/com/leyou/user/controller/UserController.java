package com.leyou.user.controller;

import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.util.JwtUtils;
import com.leyou.common.pojo.PageResult;
import com.leyou.user.config.JwtProperties;
import com.leyou.user.pojo.User;
import com.leyou.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@EnableConfigurationProperties(JwtProperties.class)
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProperties prop;

    /**
     * 用户校验
     * @param data
     * @param type
     * @return
     */
    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(@PathVariable("data") String data, @PathVariable(value = "type") Integer type){
        Boolean flag = userService.checkData(data,type);
        return ResponseEntity.ok(flag);
    }

    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    @PostMapping("/code")
    public ResponseEntity<Void> sendVerifyCode(String phone) {
        userService.sendVerifyCode(phone);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code) {
        userService.register(user, code);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("/query")
    public ResponseEntity<User> queryUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        User user = userService.queryUser(username, password);
        return ResponseEntity.ok(user);
    }


    @ApiOperation(value = "条件分页查询用户")
    @GetMapping("/page")
    public ResponseEntity<PageResult<User>> queryUsersByPage(
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "page", defaultValue = "1")Integer page,
            @RequestParam(value = "rows", defaultValue = "10")Integer rows,
            @RequestParam(value = "sortBy", required = false)String sortBy,
            @RequestParam(value = "desc", required = false)Boolean desc){
        PageResult<User> result = userService.queryUsersByPage(key, page, rows, sortBy, desc);
        return ResponseEntity.ok(result);
    }

    @ApiOperation(value = "根据id删除用户")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "根据token获取用户信息")
    @GetMapping("/token")
    public ResponseEntity<User> queryUserByToken(@CookieValue("LY_TOKEN") String token ) throws Exception {

        // 从token中解析token信息
        UserInfo userInfo = JwtUtils.getInfoFromToken(token, prop.getPublicKey());

        User user = userService.queryUserById(userInfo.getId());
        return ResponseEntity.ok(user);
    }

    @ApiOperation(value = "修改用户")
    @PutMapping("/")
    public ResponseEntity<Void> updateUserPassword(@RequestParam("id") Long id, @RequestParam("password") String password){

        userService.updateUserPassword(id,password);
        return ResponseEntity.ok().build();
    }

}