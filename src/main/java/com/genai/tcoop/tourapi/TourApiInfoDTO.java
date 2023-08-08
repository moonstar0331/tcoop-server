package com.genai.tcoop.tourapi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TourApiInfoDTO {
    private String addr1;
    private String firstimage;
    private String firstimage2;
    private Double mapx;
    private Double mapy;
    private String title;
}
