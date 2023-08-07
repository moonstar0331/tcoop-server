package com.genai.tcoop.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TourInfoDTO {

    private String addr1;
    private String addr2;
    private Double dist;
    private String imageUrl;
    private Double mapx;
    private Double mapy;
    private String title;
}
