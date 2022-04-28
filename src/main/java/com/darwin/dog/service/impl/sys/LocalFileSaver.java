package com.darwin.dog.service.impl.sys;

import com.darwin.dog.base.file.FileNode;
import com.darwin.dog.base.file.ResourceMapper;
import com.darwin.dog.constant.ObjectsCode;
import com.darwin.dog.po.sys.FilePlan;
import com.darwin.dog.base.file.LocalFileNode;
import com.darwin.dog.exception.BaseExceptionType;
import com.darwin.dog.exception.CommonException;
import com.darwin.dog.repository.sys.FilePlanRepository;
import com.darwin.dog.service.inf.sys.FileSaver;
import com.darwin.dog.util.GlobalStaticBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.MimeType;
import javax.activation.MimeTypeParseException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Transactional
public class LocalFileSaver implements FileSaver {

    private FilePlanRepository filePlanRepository;

    private File baseRepository;

    private ResourceMapper resourceMapper;

    public LocalFileSaver(FilePlanRepository filePlanRepository,String basePath,ResourceMapper resourceMapper) {
        this(filePlanRepository,new File(basePath),resourceMapper);
    }

    public LocalFileSaver(FilePlanRepository filePlanRepository,File baseRepository,ResourceMapper resourceMapper) {
        this.filePlanRepository = filePlanRepository;
        this.baseRepository = baseRepository;
        this.resourceMapper = resourceMapper;
    }

    public void start() throws IOException {
        FileUtils.forceMkdir(baseRepository);
        if (baseRepository.exists()){
            baseRepository.setExecutable(false);
            log.info("文件存储库 {} 启动成功",baseRepository.getAbsolutePath());
        }
    }

    @Override
    public FileNode syncSave(InputStream input, String filename) throws IOException, MimeTypeParseException {
        return syncSave(input, filename,null, null);
    }


    @Override
    public CompletableFuture<FileNode> asyncSave(InputStream input, String filename) {
        return asyncSave(input,filename,null,null);
    }

    private String hash(String filename){
        return DigestUtils.sha512_256Hex(filename + RandomStringUtils.randomAlphabetic(64));
    }

    private File saveFile(InputStream inputStream,String hash) throws IOException {
        File dest = new File(baseRepository, hash);
        if (dest.exists()){
            return saveFile(inputStream,hash(hash));
        }
        FileUtils.copyToFile(inputStream,dest);
        return dest;
    }

    @Override
    public FileNode syncSave(InputStream input, String filename, MimeType mimeType, Charset charset) throws IOException, MimeTypeParseException {
        String hash = hash(filename);       // 对文件名安全处理
        File savedFile = saveFile(input, hash);     // 保存文件到本地文件系统
        try {
            FilePlan willSaveFilePlan = FilePlan.of(filename, hash, LocalDateTime.now(), savedFile.length());
            if (mimeType == null){
                // 默认一个
                 mimeType = new MimeType();
                // 读取文件头的魔数，获取文件类型
                String mimeTypeName = GlobalStaticBean.TIKA.detect(savedFile);
                if (!StringUtils.isEmpty(mimeTypeName))
                    mimeType = new MimeType(mimeTypeName);
            }
            willSaveFilePlan.setType(mimeType.toString());
            if (charset==null){
                charset = Charset.defaultCharset();
            }
            willSaveFilePlan.setCharset(charset.name());
            FilePlan savedFilePlan = filePlanRepository.saveAndFlush(willSaveFilePlan);
            return new LocalFileNode(savedFilePlan, savedFile
                    , resourceMapper.downloadMap(savedFilePlan)
                    ,resourceMapper.previewMap(savedFilePlan));
        }catch (Exception e){
            // 文件回滚删除
            FileUtils.forceDeleteOnExit(savedFile);
            throw e;
        }
    }

    @Override
    public CompletableFuture<FileNode> asyncSave(InputStream input, String filename, MimeType mimeType, Charset charset) {
       return CompletableFuture.supplyAsync(()-> {
           try {
               return syncSave(input, filename, mimeType,charset);
           } catch (IOException | MimeTypeParseException e) {
               e.printStackTrace();
           }
           return null;
       });
    }

    @Override
    public FileNode read(long ID) throws IOException {
        FilePlan filePlan = filePlanRepository.findById(ID)
                .orElseThrow(()-> CommonException.of(BaseExceptionType.MISS
                        , ObjectsCode.FILE_PLAN
                        ,"目标文件索引不存在"));
        File file = new File(baseRepository, filePlan.getPath());
        if (!file.exists()){
            throw CommonException.of(BaseExceptionType.ABNORMAL_STATUS
                    ,ObjectsCode.PHYSICS_FILE
                    ,"文件一致性状态异常");
        }
        return new LocalFileNode(filePlan,file
                , resourceMapper.downloadMap(filePlan)
                ,resourceMapper.previewMap(filePlan));
    }
}
