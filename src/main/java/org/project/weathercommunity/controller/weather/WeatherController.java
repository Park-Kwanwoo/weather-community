package org.project.weathercommunity.controller.weather;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.weathercommunity.service.weather.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
public class WeatherController {

    private final WeatherService weatherService;


    @GetMapping("/weather")
    public Mono<String> weatherInformation() {
        return weatherService.getDayWeather();
    }
}
