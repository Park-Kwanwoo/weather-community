package org.project.weathercommunity.controller.weather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.request.weather.WeatherRequest;
import org.project.weathercommunity.service.weather.WeatherService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;

    @PostMapping("/weather/forecast")
    public Mono<Object> ultraShortForecast(@RequestBody @Valid WeatherRequest weatherRequest) {
        return weatherService.ultraShortForecast(weatherRequest);
    }
}
