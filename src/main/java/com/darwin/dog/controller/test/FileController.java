package com.darwin.dog.controller.test;

import com.darwin.dog.service.inf.sys.HttpFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/file")
public class FileController {
    @Autowired
    private HttpFileService httpFileService;

    @GetMapping("/download")
    public void download(@RequestParam("ID") long ID, HttpServletResponse response) throws IOException {
        httpFileService.download(ID,response);
    }

    @GetMapping("/preview")
    public void preview(@RequestParam("ID") long ID, HttpServletResponse response) throws IOException {
        httpFileService.preview(ID,response);
    }

    
}
