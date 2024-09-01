package com.example.Flower.api;

import com.example.Flower.device.UdpCon;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class ScreenshotApiController {
    @Autowired
    private UdpCon udpCon;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/api/start-udp")
    @ResponseBody
    public Map<String, Object> startUdp() {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("Starting UDP data reception and processing...");
            udpCon.receiveAndProcessData();
            response.put("status", "success");
            log.info("UDP data reception and processing started successfully.");
        } catch (Exception e) {
            log.error("Error starting UDP data reception and processing:", e);
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return response;
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/api/udp-data")
    @ResponseBody
    public Map<String, Object> getUdpData() {
        Map<String, Object> response = new HashMap<>();
        String screenshotBase64 = udpCon.getLatestScreenshotBase64();
        response.put("screenshot", screenshotBase64);
        log.info("Fetched screenshot data: {}", screenshotBase64 != null ? "exists" : "does not exist");
        return response;
    }
}
