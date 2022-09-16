package org.project.weathercommunity.request.weather;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class WeatherRequest {

    @NotNull(message = "경도 정보가 필요합니다.")
    private final Double longitude;

    @NotNull(message = "위도 정보가 필요합니다.")
    private final Double latitude;
    @NotBlank(message = "날을 입력해주세요.")
    private final String baseDate;

    @NotBlank(message = "제대로된 시간을 입력해주세요.")
    private final String baseTime;

    public WeatherRequest(Double longitude, Double latitude, String baseDate, String baseTime) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.baseDate = baseDate;
        this.baseTime = baseTime;
    }
}
