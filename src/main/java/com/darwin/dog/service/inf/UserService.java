package com.darwin.dog.service.inf;

import com.darwin.dog.dto.in.RegisteringUserInDTO;
import com.darwin.dog.po.User;
import com.darwin.dog.service.inf.sys.BasicService;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.activation.MimeTypeParseException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public interface UserService extends UserDetailsService , BasicService {
    boolean register(RegisteringUserInDTO registeringUserDTO) throws IOException, MimeTypeParseException;

    String makeUsername(HttpSession session);

}
