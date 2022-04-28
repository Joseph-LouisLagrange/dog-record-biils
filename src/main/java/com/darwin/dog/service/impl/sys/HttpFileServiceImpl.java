package com.darwin.dog.service.impl.sys;


import com.darwin.dog.base.file.FileNode;
import com.darwin.dog.service.inf.sys.FileSaver;
import com.darwin.dog.service.inf.sys.HttpFileService;
import com.darwin.dog.util.ResolveUtil;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeTypeParseException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Transactional
@Service
public class HttpFileServiceImpl implements HttpFileService {

    @Autowired
    private FileSaver fileSaver;

    @Override
    public FileNode upload(MultipartFile multipartFile) throws IOException, MimeTypeParseException {
        String contentType = multipartFile.getContentType();
        return fileSaver.syncSave(multipartFile.getInputStream()
                , multipartFile.getOriginalFilename()
                , ResolveUtil.parseMimeTypeFromContentType(contentType)
                , ResolveUtil.parseCharsetFromContentType(contentType));

    }

    @Override
    public FileNode[] uploads(MultipartFile[] multipartFiles) throws IOException, MimeTypeParseException {
        FileNode[] fileNodes = new FileNode[multipartFiles.length];
        for (int i = 0; i < multipartFiles.length; i++) {
            fileNodes[i] = upload(multipartFiles[i]);
        }
        return fileNodes;
    }

    @Override
    public void download(long ID, HttpServletResponse response) throws IOException {
        FileNode fileNode = fileSaver.read(ID);
        response.setContentType(fileNode.getContentType());
        response.setCharacterEncoding(fileNode.getCharset().name());
        response.setContentLengthLong(fileNode.getSize());
        response.setHeader("Content-Disposition",String.format("attachment; filename=%s",fileNode.getFilename()));
        IOUtils.copy(fileNode.getInputStream(),response.getOutputStream());
        response.getOutputStream().flush();
        response.flushBuffer();
    }

    @Override
    public void preview(long ID, HttpServletResponse response) throws IOException {
        FileNode fileNode = fileSaver.read(ID);
        response.setContentType(fileNode.getContentType());
        response.setContentLengthLong(fileNode.getSize());
        response.setHeader("Content-Disposition",String.format("inline; filename=%s",fileNode.getFilename()));
        IOUtils.copy(fileNode.getInputStream(),response.getOutputStream());
        response.getOutputStream().flush();
        response.flushBuffer();
    }
}
