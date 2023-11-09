package com.virtualdrive.services;

import com.virtualdrive.mapper.FileMapper;
import com.virtualdrive.model.File;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileUploadService {

    private final FileMapper fileMapper;

    public FileUploadService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public int insert(File file) {
        return fileMapper.insert(file);
    }

    public void delete(Integer fileId) {
        fileMapper.deleteById(fileId);
    }

    public List<File> getAllFiles(Integer userId) {
        return fileMapper.findAll(userId);
    }

    public File findById(Integer fileId) {
        return fileMapper.findById(fileId);
    }

    public File findByFileName(String filename, Integer userId) {
        return fileMapper.findByFileName(filename, userId);
    }
}
