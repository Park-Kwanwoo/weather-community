package org.project.weathercommunity.controller.weather;

import lombok.RequiredArgsConstructor;
import org.project.weathercommunity.request.weather.WeatherRequest;
import org.project.weathercommunity.service.weather.WeatherService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class WeatherController {

    private final WeatherService weatherService;

    @PostMapping("/weather/forecast")
    public Mono<Object> ultraShortForecast(@RequestBody @Valid WeatherRequest weatherRequest) {
        return weatherService.ultraShortForecast(weatherRequest);
    }
}
