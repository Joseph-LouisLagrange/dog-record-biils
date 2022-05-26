package com.darwin.dog.controller;

import com.darwin.dog.dto.in.RegisteringUserInDTO;
import com.darwin.dog.dto.mapper.UserMapper;
import com.darwin.dog.dto.out.UserOutDto;
import com.darwin.dog.service.inf.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeTypeParseException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/basicLogin")
    public boolean basicLogin(){return true;}

    @GetMapping("/getMe")
    public UserOutDto getMe(){
        return userMapper.userToUserOutDto(userService.getFullMe());
    }

    @GetMapping("/isLogin")
    public boolean isLogin(){
        return userService.getMe() != null;
    }

    @GetMapping("/applyUsername")
    public String applyUsername(HttpSession session) {
        return userService.makeUsername(session);
    }

    @PostMapping("/register")
    public boolean register(@RequestParam("nickName") String nickName,
                            @RequestParam("password") String password,
                            @RequestParam("avatar") MultipartFile avatar,
                            HttpSession session) throws IOException, MimeTypeParseException {
        return userService.register(
                RegisteringUserInDTO.of(nickName
                        , (String) session.getAttribute("username")
                        , password
                        , avatar)
        );
    }

    @PostMapping("/update")
    public boolean update(@RequestParam(value = "nickName",required = false) String nickName,
                            @RequestParam(value = "password",required = false) String password,
                            @RequestParam(value = "avatar",required = false) MultipartFile avatar) throws IOException, MimeTypeParseException {
        return userService.update(nickName, password, avatar);
    }

    @GetMapping("/liveDayCount")
    public long liveDayCount(){
        return userService.liveDayCount();
    }
}
