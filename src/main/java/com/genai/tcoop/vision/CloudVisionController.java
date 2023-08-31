package com.genai.tcoop.vision;

import com.genai.tcoop.model.dto.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CloudVisionController {

    private final CloudVisionService cloudVisionService;

    @PostMapping("/ocr")
    public Response<String> getLandmarkFromImage(@RequestParam MultipartFile file) {
        String landmark = cloudVisionService.getLandmarkFromImage(file);
        return Response.success(landmark);
    }
}
