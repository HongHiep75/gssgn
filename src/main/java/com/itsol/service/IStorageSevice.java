package com.itsol.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IStorageSevice {
    String storaFile(MultipartFile file,String[] type,float size,boolean encodeNameFile);
     boolean validateTypeFile(MultipartFile file,String[] type);
    boolean validateSizeFile(MultipartFile file,float size);
    byte[] readFileContent(String fileName);
    Resource getResource(String fileName);




}
