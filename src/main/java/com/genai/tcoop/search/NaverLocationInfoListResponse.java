package com.genai.tcoop.search;

import com.genai.tcoop.search.NaverLocationInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NaverLocationInfoListResponse {
    List<NaverLocationInfoDTO> data;
}
