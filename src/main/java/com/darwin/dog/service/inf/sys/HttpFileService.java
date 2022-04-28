package com.darwin.dog.service.inf.sys;

import com.darwin.dog.base.file.FileNode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeTypeParseException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
public interface HttpFileService {
    FileNode upload(MultipartFile multipartFile) throws IOException, MimeTypeParseException;

    FileNode[] uploads(MultipartFile[] multipartFiles) throws IOException, MimeTypeParseException;

    void download(long ID,HttpServletResponse response) throws IOException;

    void preview(long ID,HttpServletResponse response) throws IOException;
}
