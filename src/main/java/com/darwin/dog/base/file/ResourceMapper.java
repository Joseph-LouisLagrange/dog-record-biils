package com.darwin.dog.base.file;

import com.darwin.dog.po.sys.FilePlan;


public interface ResourceMapper {
    String downloadMap(FilePlan filePlan);

    String previewMap(FilePlan filePlan);
}
