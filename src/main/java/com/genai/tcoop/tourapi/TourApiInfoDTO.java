package com.genai.tcoop.tourapi;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TourApiInfoDTO)) return false;
        TourApiInfoDTO that = (TourApiInfoDTO) o;
        return Objects.equals(getAddr1(), that.getAddr1()) && Objects.equals(getFirstimage(), that.getFirstimage()) && Objects.equals(getFirstimage2(), that.getFirstimage2()) && Objects.equals(getMapx(), that.getMapx()) && Objects.equals(getMapy(), that.getMapy()) && Objects.equals(getTitle(), that.getTitle());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddr1(), getFirstimage(), getFirstimage2(), getMapx(), getMapy(), getTitle());
    }
}
