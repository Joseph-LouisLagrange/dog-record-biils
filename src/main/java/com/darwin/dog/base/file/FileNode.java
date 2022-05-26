package com.darwin.dog.base.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

public interface FileNode extends Serializable {
    long getID();
    long getSize();
    String getFilename();
    String getContentType();
    Charset getCharset();
    LocalDateTime getCreateTime();
    String getDownloadResourceURI();

    String getPreviewResourceURI();

    InputStream getInputStream() throws IOException;
}
