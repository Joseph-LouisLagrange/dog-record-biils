package com.darwin.dog.service.inf.sys;

import com.darwin.dog.base.file.FileNode;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

/**
 * 文件储存接口
 */
public interface FileSaver {
    void start() throws IOException;

    FileNode syncSave(InputStream input, String filename) throws IOException, MimeTypeParseException;

    CompletableFuture<FileNode> asyncSave(InputStream input, String filename);

    FileNode syncSave(InputStream input, String filename, MimeType mimeType, Charset charset) throws IOException, MimeTypeParseException;

    CompletableFuture<FileNode> asyncSave(InputStream input, String filename, MimeType mimeType, Charset charset) ;

    FileNode read(long ID) throws IOException;

}
