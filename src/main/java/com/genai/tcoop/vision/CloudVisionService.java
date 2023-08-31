package com.genai.tcoop.vision;


import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Feature;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gcp.vision.CloudVisionTemplate;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CloudVisionService {

    private final CloudVisionTemplate cloudVisionTemplate;

    private final ResourceLoader resourceLoader;

    public String getLandmarkFromImage(MultipartFile file) {
        AnnotateImageResponse response = cloudVisionTemplate.analyzeImage(
                file.getResource(), Feature.Type.LANDMARK_DETECTION);

        return response.getLandmarkAnnotationsList().toString();
    }
}
