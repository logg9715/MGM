package com.example.MultiGreenMaster.api;

import com.example.MultiGreenMaster.device.ScFrm;
import com.example.MultiGreenMaster.device.UdpCon;
import com.example.MultiGreenMaster.entity.PlantENT;
import com.example.MultiGreenMaster.entity.UserENT;
import com.example.MultiGreenMaster.service.PlantSRV;
import com.example.MultiGreenMaster.service.UserSRV;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/udp")
public class UdpAPI {

    @Autowired
    private UdpCon udpCon;

    @Autowired
    private PlantSRV plantSRV;

    @Autowired
    private UserSRV userSRV;

    /* 세션 정보를 읽어서 배정된 식물 정보 제공 */
    // 로그인오류&세션오류 => badRequest
    // 배정된 식물이 없음 => ok, {"ip", "0"}
    // 배정된 식물이 있음 => ok, {"ip", "127.0.0.2"}
    @GetMapping("/getipaddress")
    public ResponseEntity<?> getIpAddress(HttpSession session) {
        Map<String, String> result = new HashMap<>();

        Object loginId = session.getAttribute("userId");
        if(loginId == null)
            return ResponseEntity.badRequest().body("Login session not Found");
        if(userSRV.findUserById((Long) loginId) == null)
            return ResponseEntity.badRequest().body("Login user not Found");

        PlantENT target_plant = plantSRV.findByUserId((Long) loginId);
        if (target_plant == null)
            result.put("ip", "0");

        result.put("ip", target_plant.getIpaddress());
        return ResponseEntity.ok(result);

    }

    @GetMapping("/sensor/{ipAddress}")
    public Map<String, Object> getSensorData(@PathVariable String ipAddress) {
        Map<String, Object> response = new HashMap<>();
        try {
            String[] data = udpCon.start(ipAddress, 20920);
            response.put("sensorData", data);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    @GetMapping("/rotater/{ipAddress}")
    public Map<String, Object> getRotaterData(@PathVariable String ipAddress) {
        Map<String, Object> response = new HashMap<>();
        try {
            String[] data = udpCon.actuator(ipAddress, 20920, ScFrm.ROTATER);
            response.put("rotaterData", data);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    @GetMapping("/sprinkler/{ipAddress}")
    public Map<String, Object> getSprinklerData(@PathVariable String ipAddress) {
        Map<String, Object> response = new HashMap<>();
        try {
            String[] data = udpCon.actuator(ipAddress, 20920, ScFrm.SPRINKLER);
            response.put("sprinklerData", data);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    @GetMapping("/led/{ipAddress}")
    public Map<String, Object> getLedData(@PathVariable String ipAddress) {
        Map<String, Object> response = new HashMap<>();
        try {
            String[] data = udpCon.actuator(ipAddress, 20920, ScFrm.LED);
            response.put("ledData", data);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

    @GetMapping("/screenshot/{ipAddress}")
    public Map<String, Object> getScreenshot(@PathVariable String ipAddress) {
        Map<String, Object> response = new HashMap<>();
        try {
            udpCon.receiveAndProcessData("175.123.202.85");
            String screenshot = udpCon.getLatestScreenshotBase64();
            response.put("screenshot", screenshot);
        } catch (Exception e) {
            response.put("error", e.getMessage());
        }
        return response;
    }

}
