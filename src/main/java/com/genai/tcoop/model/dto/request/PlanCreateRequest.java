package com.genai.tcoop.model.dto.request;

import com.genai.tcoop.common.annotation.IsLatitude;
import com.genai.tcoop.common.annotation.IsLongitude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlanCreateRequest {

    @IsLatitude
    @NotNull(message ="Latitude is required")
    private Double latitude;

    @IsLongitude
    @NotNull(message = "Longitude is required")
    private Double longitude;

    private String address;

    private String title;

    private String content;

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}
