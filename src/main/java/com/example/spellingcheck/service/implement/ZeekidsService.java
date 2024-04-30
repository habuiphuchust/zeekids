package com.example.spellingcheck.service.implement;

import com.example.spellingcheck.service.IZeekidsService;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ZeekidsService implements IZeekidsService {
    private Process zeekidsProcess;
    @Override
    public void startZeekids() {
        if (zeekidsProcess == null || !zeekidsProcess.isAlive()) {
            try {
                // Đường dẫn đến thực thi của Zeekids và các đối số
                String zeekidsPath = "C:\\Windows\\System32\\notepad.exe"; // Thay thế bằng đường dẫn thực tế đến tệp thực thi Zeekids
                String[] command = {zeekidsPath};

                // Khởi động tiến trình Zeekids bằng ProcessBuilder
//                ProcessBuilder processBuilder = new ProcessBuilder(command);
//                zeekidsProcess = processBuilder.start();
                zeekidsProcess = Runtime.getRuntime().exec(command);

                System.out.println(zeekidsProcess.pid());


                // Kiểm tra xem tiến trình đã được khởi động thành công hay không
                if (zeekidsProcess.isAlive()) {
                    System.out.println("Zeekids started successfully.");
                } else {
                    System.out.println("Failed to start Zeekids.");
                }

            } catch (IOException e) {
                System.err.println("Error starting Zeekids: " + e.getMessage());
            }
        } else {
            System.out.println("Zeekids is running.");
        }

    }

    @Override
    public void stopZeekids() {
        System.out.println(zeekidsProcess.pid());
        if (zeekidsProcess != null && zeekidsProcess.isAlive()) {
            // Tắt tiến trình Zeekids nếu đang chạy
            zeekidsProcess.destroy();
            System.out.println("Zeekids stopped successfully.");
        } else {
            System.out.println("Zeekids is not running.");
        }
    }
}
