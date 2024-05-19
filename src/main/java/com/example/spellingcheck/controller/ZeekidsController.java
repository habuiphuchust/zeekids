package com.example.spellingcheck.controller;

import com.example.spellingcheck.model.dto.response.ZeekDTO;
import com.example.spellingcheck.service.IZeekidsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ZeekidsController {
    private final IZeekidsService zeekidsService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/start-zeekids")
    public ResponseEntity<ZeekDTO> startZeekids() {
        return zeekidsService.start();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/stop-zeekids")
    public ResponseEntity<ZeekDTO> stopZeekids() {
        return zeekidsService.stop();
    }

    @GetMapping("/check-zeekids")
    public ResponseEntity<ZeekDTO> checkZeekids() {
        return zeekidsService.checkState();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/edit-config/{name}")
    public ResponseEntity<ZeekDTO> editConfig(@PathVariable String name, @RequestBody String content) {
        return zeekidsService.changeConfig(name, content);
    }
    @GetMapping("/get-config/{name}")
    public ResponseEntity<ZeekDTO> getConfig(@PathVariable String name) {
        return zeekidsService.getConfig(name);
    }
}
