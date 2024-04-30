package com.example.spellingcheck.controller;

import com.example.spellingcheck.service.IZeekidsService;
import com.example.spellingcheck.util.Constants;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@AllArgsConstructor
public class ZeekidsController {
    private final IZeekidsService zeekidsService;
    @GetMapping("/start-zeekids")
    public String startZeekids() {
        zeekidsService.startZeekids();
        return "Zeekids started.";
    }

    @GetMapping("/stop-zeekids")
    public String stopZeekids() {
        zeekidsService.stopZeekids();
        return "Zeekids stopped.";
    }

    @PostMapping("/edit-config")
    public String editConfig(@RequestBody String content) {
        String filePath = "./zeek/myscript/" + Constants.ZEEK_CONFIG_NAME;

        try {
            // Tạo một đối tượng FileWriter để ghi nội dung vào tệp mới
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(content);
            fileWriter.close();

            return "success";
        } catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }
    }
    @GetMapping("/get-config")
    public String getConfig() {
        String filePath = "./zeek/myscript/" + Constants.ZEEK_CONFIG_NAME;

        try {
            // Tạo một đối tượng FileWriter để ghi nội dung vào tệp mới
            File file = new File(filePath);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n"); // Thêm dòng mới sau mỗi dòng trong file
            }

            reader.close();

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "fail";
        }
    }
}
