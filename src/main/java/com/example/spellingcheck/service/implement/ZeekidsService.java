package com.example.spellingcheck.service.implement;

import com.example.spellingcheck.model.dto.response.ZeekDTO;
import com.example.spellingcheck.service.IZeekidsService;
import com.example.spellingcheck.util.Constants;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ZeekidsService implements IZeekidsService {
    private Process zeekidsProcess;
    private final ZeekDTO zeekDTO = new ZeekDTO();
    @Override
    public ResponseEntity<ZeekDTO> start() {
        if (zeekidsProcess == null || !zeekidsProcess.isAlive()) {
            try {
                // Khởi động tiến trình Zeekids bằng ProcessBuilder
                ProcessBuilder processBuilder = new ProcessBuilder(Constants.ZEEK_START_COMMAND);
                zeekidsProcess = processBuilder.start();

                System.out.println(zeekidsProcess.pid());

                // Kiểm tra xem tiến trình đã được khởi động thành công hay không
                if (zeekidsProcess.isAlive()) {
                    zeekDTO.setStatus(true);
                    zeekDTO.setMessage("Zeekids started successfully.");
                } else {
                    zeekDTO.setStatus(false);
                    zeekDTO.setMessage("Failed to start Zeekids.");
                }

            } catch (IOException e) {
                zeekDTO.setStatus(false);
                zeekDTO.setMessage("Error starting Zeekids: " + e.getMessage());
            }
        } else {
            zeekDTO.setStatus(false);
            zeekDTO.setMessage("Zeekids is running.");
        }
        return ResponseEntity.ok(zeekDTO);

    }

    @Override
    public ResponseEntity<ZeekDTO> stop() {
        if (zeekidsProcess != null && zeekidsProcess.isAlive()) {
            System.out.println(zeekidsProcess.pid());
            // Tắt tiến trình Zeekids nếu đang chạy
            zeekidsProcess.destroy();
            zeekDTO.setStatus(true);
            zeekDTO.setMessage("Zeekids stopped successfully.");
        } else {
            zeekDTO.setStatus(false);
            zeekDTO.setMessage("Zeekids is not running.");
        }
        return ResponseEntity.ok(zeekDTO);
    }

    @Override
    public ResponseEntity<ZeekDTO> checkState() {
        if (zeekidsProcess != null) {
            if (zeekidsProcess.isAlive()) {
                zeekDTO.setStatus(true);
                zeekDTO.setMessage("Zeekids is running.");
                return ResponseEntity.ok(zeekDTO);
            }
        }
        zeekDTO.setStatus((false));
        zeekDTO.setMessage("Zeekids is stopped");
        return ResponseEntity.ok(zeekDTO);
    }

    @Override
    public ResponseEntity<ZeekDTO> changeConfig(String name, String content) {
        try {
            // Tạo một đối tượng FileWriter để ghi nội dung vào tệp mới
            FileWriter fileWriter = new FileWriter(Constants.ZEEK_CONFIG_PATH + name);
            fileWriter.write(content);
            fileWriter.close();

            zeekDTO.setStatus(true);
            zeekDTO.setMessage("thay đổi cấu hình thành công");
        } catch (IOException e) {
            zeekDTO.setStatus(false);
            zeekDTO.setMessage(e.getMessage());
        }
        return ResponseEntity.ok(zeekDTO);
    }

    @Override
    public ResponseEntity<ZeekDTO> getConfig(String name) {
        try {
            // Tạo một đối tượng FileWriter để ghi nội dung vào tệp mới
            File file = new File(Constants.ZEEK_CONFIG_PATH + name);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n"); // Thêm dòng mới sau mỗi dòng trong file
            }

            reader.close();
            zeekDTO.setStatus(true);
            zeekDTO.setMessage(stringBuilder.toString());

        } catch (IOException e) {
            zeekDTO.setStatus(false);
            zeekDTO.setMessage(e.getMessage());

        }
        return ResponseEntity.ok(zeekDTO);
    }

}
