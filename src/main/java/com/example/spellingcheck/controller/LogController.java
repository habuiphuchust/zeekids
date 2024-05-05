package com.example.spellingcheck.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("logs")
public class LogController {
    private static final String FILE_DIRECTORY = "./zeek/";

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> GetLog(@PathVariable String filename) {
        try {
            Path path = Paths.get(FILE_DIRECTORY + filename);
            Resource resource = new UrlResource(path.toUri());
            if (resource.exists())
                return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .header(HttpHeaders.CONTENT_DISPOSITION)
                    .body(resource);
            return null;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
