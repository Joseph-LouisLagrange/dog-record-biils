package com.darwin.dog.base.file;

import com.darwin.dog.po.sys.FilePlan;
import lombok.ToString;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

@ToString
public class LocalFileNode implements FileNode {
    File file;

    FilePlan filePlan;

    String downloadResourceURI;

    String previewResourceURI;

    public LocalFileNode(FilePlan filePlan,File file
            ,String downloadResourceURI
            ,String previewResourceURI){
        this.filePlan = filePlan;
        this.file = file;
        this.previewResourceURI = previewResourceURI;
        this.downloadResourceURI = downloadResourceURI;
    }


    @Override
    public long getID() {
        return filePlan.getID();
    }

    @Override
    public long getSize() {
        return filePlan.getSize();
    }

    @Override
    public String getFilename() {
        return filePlan.getName();
    }

    @Override
    public String getContentType() {
        return filePlan.getType();
    }

    @Override
    public Charset getCharset() {
        return Charset.forName(filePlan.getCharset());
    }

    @Override
    public LocalDateTime getCreateTime() {
        return filePlan.getCreateDateTime();
    }

    public String getDownloadResourceURI() {
        return null;
    }

    @Override
    public String getPreviewResourceURI() {
        return previewResourceURI;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return FileUtils.openInputStream(file);
    }
}
