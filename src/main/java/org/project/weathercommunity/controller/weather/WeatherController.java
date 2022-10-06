package org.project.weathercommunity.controller.weather;

import lombok.RequiredArgsConstructor;
import org.project.weathercommunity.request.weather.WeatherRequest;
import org.project.weathercommunity.service.weather.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather/forecast")
    public Mono<Object> ultraShortForecast(@ModelAttribute @Valid WeatherRequest weatherRequest) {
        return weatherService.ultraShortForecast(weatherRequest);
    }
}
