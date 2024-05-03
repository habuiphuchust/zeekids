package com.example.spellingcheck.service;

import com.example.spellingcheck.model.dto.response.ZeekDTO;
import org.springframework.http.ResponseEntity;

public interface IZeekidsService {
    ResponseEntity<ZeekDTO> start();
    ResponseEntity<ZeekDTO> stop();
    ResponseEntity<ZeekDTO> checkState();
    ResponseEntity<ZeekDTO> changeConfig(String content);
    ResponseEntity<ZeekDTO> getConfig();
}
