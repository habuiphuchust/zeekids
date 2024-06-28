package com.example.spellingcheck.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface IFileService {
    ResponseEntity<Resource> getFile(String path);
    ResponseEntity<String> getListFiles(String path);
    ResponseEntity<String> addFile(String path, String content, boolean create) throws IOException;
    ResponseEntity<String> addDirectory(String path);
    ResponseEntity<String> deleteFile(String path);
    ResponseEntity<String> deleteDirectory(String path);
}
