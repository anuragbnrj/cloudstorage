package com.udacity.jwdnd.course1.cloudstorage.service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class FileService {

    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public File findFileById(Long fileId) {
        return fileMapper.findFileById(fileId);
    }

    public List<File> findFilesByUserId(Long userId) {
        return fileMapper.findFilesByUserId(userId);
    }

    public int createFile(MultipartFile multipartFile, Long userId) throws IOException {
        File file = new File();
        file.setFileName(multipartFile.getOriginalFilename());
        file.setContentType(multipartFile.getContentType());
        file.setFileSize(multipartFile.getSize());
        file.setUserId(userId);
        file.setFileData(multipartFile.getBytes());
        return fileMapper.createFile(file);
    }

    public int deleteFileById(Long fileId) {
        return fileMapper.deleteFileById(fileId);
    }

}
