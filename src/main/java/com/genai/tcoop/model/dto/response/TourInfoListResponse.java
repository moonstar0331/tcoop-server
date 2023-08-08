package com.genai.tcoop.model.dto.response;

import com.genai.tcoop.model.dto.TourInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class TourInfoListResponse {
    List<TourInfoDTO> data;
}
