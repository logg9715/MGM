package com.example.Flower.api;

import com.example.Flower.device.ScForm;
import com.example.Flower.device.UdpCon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/udp")
public class UdpApiController {

    @Autowired
    private UdpCon udpCon;

    @GetMapping("/sensor")
    public Map<String, Object> getSensorData() {
        Map<String, Object> response = new HashMap<>();
        try {
            String[] data = udpCon.start("175.123.202.85", 20920);
            response.put("sensorData", data);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    @GetMapping("/rotater")
    public Map<String, Object> getRotaterData() {
        Map<String, Object> response = new HashMap<>();
        try {
            String[] data = udpCon.actuator("175.123.202.85", 20920, ScForm.ROTATER);
            response.put("rotaterData", data);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    @GetMapping("/sprinkler")
    public Map<String, Object> getSprinklerData() {
        Map<String, Object> response = new HashMap<>();
        try {
            String[] data = udpCon.actuator("175.123.202.85", 20920, ScForm.SPRINKLER);
            response.put("sprinklerData", data);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    @GetMapping("/led")
    public Map<String, Object> getLedData() {
        Map<String, Object> response = new HashMap<>();
        try {
            String[] data = udpCon.actuator("175.123.202.85", 20920, ScForm.LED);
            response.put("ledData", data);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    @GetMapping("/screenshot")
    public Map<String, Object> getScreenshot() {
        Map<String, Object> response = new HashMap<>();
        try {
            udpCon.receiveAndProcessData();
            String screenshot = udpCon.getLatestScreenshotBase64();
            response.put("screenshot", screenshot);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

}
