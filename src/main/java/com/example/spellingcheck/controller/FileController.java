package com.example.spellingcheck.controller;

import com.example.spellingcheck.service.IFileService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class FileController {
    private final IFileService fileService;

    @GetMapping("/config/file")
    public ResponseEntity<Resource> getFile(@RequestParam(name = "path", defaultValue = "") String path) {
        return fileService.getFile(path);
    }
    @GetMapping("/config/dir")
    public ResponseEntity<String> getListFiles(@RequestParam(name = "path", defaultValue = "/") String path) {
        return fileService.getListFiles(path);
    }
    @GetMapping("/logs/{filename}")
    public ResponseEntity<Resource> GetLog(@PathVariable String filename) {
        return fileService.getFile("./zeek/" + filename);
    }
}
