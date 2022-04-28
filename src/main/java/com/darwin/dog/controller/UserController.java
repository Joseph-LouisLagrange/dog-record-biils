package com.darwin.dog.controller;

import com.darwin.dog.dto.in.RegisteringUserInDTO;
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

    @GetMapping("/basicLogin")
    public boolean basicLogin(){return true;}

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
}
