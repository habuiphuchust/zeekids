package com.example.spellingcheck.service.implement;

import com.example.spellingcheck.service.IFileService;
import com.example.spellingcheck.util.Constants;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
public class FileService<T> implements IFileService {
    @Override
    public ResponseEntity<Resource> getFile(String filePath) {
        try {
            Path path = Paths.get(Constants.ZEEK_CONFIG_PATH + filePath);
            Resource resource = new UrlResource(path.toUri());
            File directory = resource.getFile();
            if (directory.isFile())
                return ResponseEntity.ok()
                        .contentType(MediaType.TEXT_PLAIN)
                        .header(HttpHeaders.CONTENT_DISPOSITION)
                        .body(resource);
            else
                return ResponseEntity.badRequest().body(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<String> getListFiles(String dirPath) {
        try {
            Path path = Paths.get(Constants.ZEEK_CONFIG_PATH + dirPath);
            Resource resource = new UrlResource(path.toUri());
            File directory = resource.getFile();

            if (directory.isDirectory()) {
                File[] filesList = directory.listFiles();
                StringBuilder json = new StringBuilder("[");

                if (filesList != null) {
                    for (File file : filesList) {
                        String zeekConfigPath = Constants.ZEEK_CONFIG_PATH;
                        if (System.getProperty("os.name").toLowerCase().contains("win"))
                            zeekConfigPath = Constants.ZEEK_CONFIG_PATH.replace('/', '\\');
                        String filePath = file.getPath();
                        String newPath = filePath.substring(filePath.indexOf(zeekConfigPath) + zeekConfigPath.length());

                        if (file.isFile()) {
                            System.out.println("File: " + file.getName());
                            json.append("{\"type\": \"file\", \"path\": \"").append(newPath).append("\", \"name\": \"").append(file.getName()).append("\"},");
                        } else if (file.isDirectory()) {
                            System.out.println("Directory: " + file.getName());
                            json.append("{\"type\": \"directory\", \"path\": \"").append(newPath).append("\", \"name\": \"").append(file.getName()).append("\"},");
                        }
                    }
                } else {
                    System.out.println("The directory is empty or an error occurred.");
                }
                json.append("]");
                return ResponseEntity.ok(json.toString());
            } else {
                System.out.println("The path is not a directory.");
                return ResponseEntity.badRequest().body("the path is not a directory");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<String> addFile(String path, String content) {
        return null;
    }

    @Override
    public ResponseEntity<String> addDirectory(String path) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteFile(String path) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteDirectory(String path) {
        return null;
    }
}
