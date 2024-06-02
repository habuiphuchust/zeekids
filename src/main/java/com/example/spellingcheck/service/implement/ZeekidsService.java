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
        try {
            // Create a process builder
            ProcessBuilder processBuilder = new ProcessBuilder();
            // Split the command into a list of arguments
            processBuilder.command("bash", "-c", Constants.ZEEK_STOP);
            // Start the process
            Process process = processBuilder.start();

            process.waitFor();
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

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ResponseEntity<ZeekDTO> checkState() {
        try {
            // Create a process builder
            ProcessBuilder processBuilder = new ProcessBuilder();
            // Split the command into a list of arguments
            processBuilder.command("bash", "-c", Constants.ZEEK_CHECK_STATUS);
            // Start the process
            Process process = processBuilder.start();

            // Read the output of the command
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            System.out.println(line);
            if(line == null) {
                zeekDTO.setStatus((false));
                zeekDTO.setMessage("Zeekids is stopped");
            } else {
                zeekDTO.setStatus(true);
                zeekDTO.setMessage("Zeekids is running.");
            }

            // Wait for the process to finish and get the exit code
            process.waitFor();
//            System.out.println("\nExited with code: " + exitCode);
            reader.close();
            return ResponseEntity.ok(zeekDTO);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
