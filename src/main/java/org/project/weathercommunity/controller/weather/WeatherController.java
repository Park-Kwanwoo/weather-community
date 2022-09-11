package org.project.weathercommunity.controller.weather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.request.weather.WeatherInfo;
import org.project.weathercommunity.service.weather.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
public class WeatherController {

    private final WeatherService weatherService;

    @PostMapping("/weather")
    public Mono<String> weatherInformation(@RequestBody @Valid WeatherInfo weatherInfo) {
        return weatherService.getDayWeather(weatherInfo);
    }
}
