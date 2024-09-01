package com.example.Flower.controller;

import com.example.Flower.entity.Plants;
import com.example.Flower.service.PlantService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class PlantsController extends SessionCheckController{

    private final PlantService plantService;

    @Autowired
    public PlantsController(PlantService plantService) {
        this.plantService = plantService;
    }

    @GetMapping("/plants")
    public String getPlants(Model model) {
        List<Plants> plants = plantService.findAll();
        List<PlantDTO> plantDTOs = plants.stream().map(this::convertToDTO).collect(Collectors.toList());
        model.addAttribute("plants", plantDTOs);
        return "plants";
    }

    @PostMapping("/plants")
    public String uploadPlant(@RequestParam("name") String name,
                              @RequestParam("ypImage") MultipartFile ypImage,
                              @RequestParam("mpImage") MultipartFile mpImage,
                              Model model) {
        try {
            plantService.savePlantWithImages(name, ypImage.getBytes(), mpImage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Failed to upload images");
            return "error";
        }
        return "redirect:/plants";
    }

    private PlantDTO convertToDTO(Plants plant) {
        String ypImage = Base64.getEncoder().encodeToString(plant.getYP_picture());
        String mpImage = Base64.getEncoder().encodeToString(plant.getMP_picture());
        return new PlantDTO(plant.getName(), ypImage, mpImage);
    }

    public static class PlantDTO {
        private final String name;
        private final String ypImage;
        private final String mpImage;

        public PlantDTO(String name, String ypImage, String mpImage) {
            this.name = name;
            this.ypImage = ypImage;
            this.mpImage = mpImage;
        }

        public String getName() {
            return name;
        }

        public String getYpImage() {
            return ypImage;
        }

        public String getMpImage() {
            return mpImage;
        }
    }
    @Autowired
    private HttpServletRequest request;
}