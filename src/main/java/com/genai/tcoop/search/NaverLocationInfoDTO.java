package com.genai.tcoop.search;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class NaverLocationInfoDTO {

    private String title;
    private String address;
    private Double latitude;
    private Double longitude;
}
