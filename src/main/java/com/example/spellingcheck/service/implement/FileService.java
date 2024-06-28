package com.example.spellingcheck.service.implement;

import com.example.spellingcheck.exception.CustomException;
import com.example.spellingcheck.exception.ExceptionCode;
import com.example.spellingcheck.service.IFileService;
import com.example.spellingcheck.util.Constants;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
public class FileService implements IFileService {
    @Override
    public ResponseEntity<Resource> getFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
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
            throw new CustomException(ExceptionCode.INTERNAL_SERVER_ERROR);
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
                        newPath = newPath.replace('\\', '/');

                        if (file.isFile()) {
                            json.append("{\"type\": \"file\", \"path\": \"").append(newPath).append("\", \"name\": \"").append(file.getName()).append("\"},");
                        } else if (file.isDirectory()) {
                            json.append("{\"type\": \"directory\", \"path\": \"").append(newPath).append("\", \"name\": \"").append(file.getName()).append("\"},");
                        }
                    }
                }
                if (json.length() != 1) {
                    json.deleteCharAt(json.length() - 1);
                    json.append("]");
                } else {
                    json.append("]");
                }

                return ResponseEntity.ok(json.toString());
            } else {
                return ResponseEntity.badRequest().body("the path is not a directory");
            }
        } catch (IOException e) {
            throw new CustomException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> addFile(String path, String content, boolean create) throws IOException {
        File file = new File(Constants.ZEEK_CONFIG_PATH + path);
        if (file.exists() && !create)
            return ResponseEntity.badRequest().body("file existed");
        try ( FileWriter fileWriter = new FileWriter(Constants.ZEEK_CONFIG_PATH + path)) {
            // Tạo một đối tượng FileWriter để ghi nội dung vào tệp mới
            fileWriter.write(content);
            return ResponseEntity.ok("create file success");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @Override
    public ResponseEntity<String> addDirectory(String path) {
        File dir = new File(Constants.ZEEK_CONFIG_PATH + path);

        if (!dir.exists()) {
            if (dir.mkdir()) {
                return ResponseEntity.ok("Directory created successfully");
            } else {
                return ResponseEntity.badRequest().body("Failed to create the directory");
            }
        } else {
            return ResponseEntity.badRequest().body("Directory already exists");
        }
    }

    @Override
    public ResponseEntity<String> deleteFile(String path) {
        File file = new File(Constants.ZEEK_CONFIG_PATH + path);

        if (file.exists() && file.isFile()) {
            try {
                Files.delete(file.toPath());
                return ResponseEntity.ok("File deleted successfully");
            } catch (IOException e){
                return ResponseEntity.badRequest().body("Failed to delete the file");
            }
        } else {
            return ResponseEntity.badRequest().body("File does not exist");
        }
    }

    @Override
    public ResponseEntity<String> deleteDirectory(String path) {
        File dir = new File(Constants.ZEEK_CONFIG_PATH + path);

        if (dir.exists() && dir.isDirectory()) {
            try {
                Files.delete(dir.toPath());
                return ResponseEntity.ok("Directory deleted successfully");
            } catch (IOException e)
            {
                return ResponseEntity.badRequest().body("Failed to delete the directory");
            }
        } else {
            return ResponseEntity.badRequest().body("Directory does not exist or is not a directory");
        }
    }
}
