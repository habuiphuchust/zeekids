package com.example.spellingcheck.controller;

import com.example.spellingcheck.model.dto.response.ZeekDTO;
import com.example.spellingcheck.service.IZeekidsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ZeekidsController {
    private final IZeekidsService zeekidsService;
    @GetMapping("/start-zeekids")
    public ResponseEntity<ZeekDTO> startZeekids() {
        return zeekidsService.start();
    }

    @GetMapping("/stop-zeekids")
    public ResponseEntity<ZeekDTO> stopZeekids() {
        return zeekidsService.stop();
    }

    @GetMapping("/check-zeekids")
    public ResponseEntity<ZeekDTO> checkZeekids() {
        return zeekidsService.checkState();
    }

    @PostMapping("/edit-config")
    public ResponseEntity<ZeekDTO> editConfig(@RequestBody String content) {
        return zeekidsService.changeConfig(content);
    }
    @GetMapping("/get-config")
    public ResponseEntity<ZeekDTO> getConfig() {
        return zeekidsService.getConfig();
    }
}
