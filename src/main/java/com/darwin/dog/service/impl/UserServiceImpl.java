package com.darwin.dog.service.impl;

import com.darwin.dog.base.file.FileNode;
import com.darwin.dog.constant.ObjectsCode;
import com.darwin.dog.constant.Roles;
import com.darwin.dog.po.User;
import com.darwin.dog.po.sys.Role;
import com.darwin.dog.dto.in.RegisteringUserInDTO;
import com.darwin.dog.exception.BaseExceptionType;
import com.darwin.dog.exception.CommonException;
import com.darwin.dog.repository.UserRepository;
import com.darwin.dog.service.inf.UserService;
import com.darwin.dog.service.inf.sys.BasicService;
import com.darwin.dog.service.inf.sys.HttpFileService;
import com.darwin.dog.service.inf.sys.RoleService;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeTypeParseException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

@Slf4j
@Service("userServiceImpl")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpFileService httpFileService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(
                        () -> CommonException.of(BaseExceptionType.MISS, ObjectsCode.USER, "????????? username")
                );
    }

    @Override
    public boolean register(RegisteringUserInDTO registeringUserDTO) throws IOException, MimeTypeParseException {
        FileNode fileNode = httpFileService.upload(registeringUserDTO.getAvatar());
        userRepository.save(
                new User(
                        Sets.newHashSet(roleService.read(Roles.USER.getRoleName()))
                        , registeringUserDTO.getNickName()
                        , registeringUserDTO.getUsername()
                        , passwordEncoder.encode(registeringUserDTO.getPassword())
                        , fileNode.getPreviewResourceURI()
                )
        );
        return true;
    }

    @Override
    public String makeUsername(HttpSession session) {
        String username = RandomStringUtils.randomNumeric(10);
        session.setAttribute("username",username);
        return username;
    }

    @Override
    public boolean update(String nickName, String password, MultipartFile avatar) throws MimeTypeParseException, IOException {
        User me = userRepository.getOne(getMe().getID());
        if (nickName!=null){
            me.setNickName(nickName);
        }
        if(password!=null){
            me.setPassword(passwordEncoder.encode(password));
        }
        if(avatar!=null && !avatar.isEmpty()){
            FileNode fileNode = httpFileService.upload(avatar);
            me.setAvatarUrl(fileNode.getPreviewResourceURI());
        }
        me = userRepository.save(me);
        getMe().setAvatarUrl(me.getAvatarUrl());
        getMe().setNickName(me.getNickName());
        return true;
    }

    @Override
    public long liveDayCount() {
        return getMe()
                .getCreateDateTime()
                .until(LocalDateTime.now(), ChronoUnit.DAYS) + 1;
    }

    @Override
    public User getFullMe() {
        return userRepository.findById(getMe().getID()).get();
    }
}
