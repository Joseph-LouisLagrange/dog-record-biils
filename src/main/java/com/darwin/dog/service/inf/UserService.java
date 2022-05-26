package com.darwin.dog.service.inf;

import com.darwin.dog.dto.in.RegisteringUserInDTO;
import com.darwin.dog.po.User;
import com.darwin.dog.service.inf.sys.BasicService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeTypeParseException;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public interface UserService extends UserDetailsService, BasicService {
    boolean register(RegisteringUserInDTO registeringUserDTO) throws IOException, MimeTypeParseException;

    String makeUsername(HttpSession session);

    boolean update(String nickName,
                   String password,
                   MultipartFile avatar) throws MimeTypeParseException, IOException;

    long liveDayCount();

    User getFullMe();

}
