package com.darwin.dog;

import com.darwin.dog.base.file.FileNode;
import com.darwin.dog.service.inf.sys.HttpFileService;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.util.FileCopyUtils;

import javax.activation.MimeTypeParseException;
import java.io.File;
import java.io.IOException;
import java.net.URLConnection;

@SpringBootTest
public class HttpFileServiceTest {

    @Autowired
    HttpFileService httpFileService;

    @Test
    public void testUpload() throws IOException, MimeTypeParseException {

    }
}
