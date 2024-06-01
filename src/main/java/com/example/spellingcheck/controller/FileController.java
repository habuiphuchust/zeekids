package com.example.spellingcheck.controller;

import com.example.spellingcheck.model.dto.response.ZeekDTO;
import com.example.spellingcheck.service.IFileService;
import com.example.spellingcheck.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@AllArgsConstructor
public class FileController {
    private final IFileService fileService;

    @GetMapping("/config/file")
    public ResponseEntity<Resource> getFile(@RequestParam(name = "path", defaultValue = "") String path) {
        return fileService.getFile(Constants.ZEEK_CONFIG_PATH + path);
    }
    @GetMapping("/config/dir")
    public ResponseEntity<String> getListFiles(@RequestParam(name = "path", defaultValue = "/") String path) {
        return fileService.getListFiles(path);
    }
    @GetMapping("/logs/{filename}")
    public ResponseEntity<Resource> GetLog(@PathVariable String filename) {
        return fileService.getFile("./zeek/" + filename);
    }
    @PostMapping("/config/file")
    public ResponseEntity<String> addFile(@RequestParam(name = "path", defaultValue = "") String path, @RequestBody String content,
                                          @RequestParam(name = "new", defaultValue = "false") String create) {
        boolean newFile = false;
        if (Objects.equals(create, "true")) newFile = true;
        return fileService.addFile(path, content, newFile);
    }
    @GetMapping("/config/file/delete")
    public ResponseEntity<String> deleteFile(@RequestParam(name = "path", defaultValue = "") String path) {
        return fileService.deleteFile(path);
    }
    @PostMapping("/config/dir")
    public ResponseEntity<String> addDir(@RequestParam(name = "path", defaultValue = "") String path) {
        return fileService.addDirectory(path);
    }
    @GetMapping("/config/dir/delete")
    public ResponseEntity<String> deleteDir(@RequestParam(name = "path", defaultValue = "") String path) {
        return fileService.deleteDirectory(path);
    }
}
