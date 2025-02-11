package com.example.spellingcheck.controller;

import com.example.spellingcheck.service.IFileService;
import com.example.spellingcheck.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    public ResponseEntity<Resource> getLog(@PathVariable String filename) {
        return fileService.getFile("./zeek/" + filename);
    }
    @PostMapping("/config/file")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addFile(@RequestParam(name = "path", defaultValue = "") String path, @RequestBody String content,
                                          @RequestParam(name = "new", defaultValue = "false") String create) throws IOException {
        boolean newFile = false;
        if (Objects.equals(create, "true")) newFile = true;
        return fileService.addFile(path, content, newFile);
    }
    @GetMapping("/config/file/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteFile(@RequestParam(name = "path", defaultValue = "") String path) {
        return fileService.deleteFile(path);
    }
    @PostMapping("/config/dir")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> addDir(@RequestParam(name = "path", defaultValue = "") String path) {
        return fileService.addDirectory(path);
    }
    @GetMapping("/config/dir/delete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteDir(@RequestParam(name = "path", defaultValue = "") String path) {
        return fileService.deleteDirectory(path);
    }
}
