package org.project.weathercommunity.controller.weather;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.project.weathercommunity.request.weather.WeatherInfo;
import org.project.weathercommunity.service.weather.WeatherService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Arrays;

@RequiredArgsConstructor
@RestController
public class WeatherController {

    private final WeatherService weatherService;

    @PostMapping("/weather/forecast")
    public Mono<String> ultraShortForecast(@RequestBody @Valid WeatherInfo weatherInfo) throws JsonProcessingException {
        return weatherService.ultraShortForecast(weatherInfo);
    }
}
