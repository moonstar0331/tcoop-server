package com.genai.tcoop.tourapi;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TourApiInfoListResponse {
    List<TourApiInfoDTO> data;
}
