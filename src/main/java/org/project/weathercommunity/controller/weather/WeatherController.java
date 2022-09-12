package org.project.weathercommunity.controller.weather;

import lombok.RequiredArgsConstructor;
import org.project.weathercommunity.request.weather.WeatherInfo;
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

    @PostMapping("/weather/ultrashortliveinquiry")
    public Mono<String> ultraShortLiveInquiry(@RequestBody @Valid WeatherInfo weatherInfo) {
        return weatherService.ultraShortLiveInquiry(weatherInfo);
    }

    @PostMapping("/weather/ultrashortforecast")
    public Mono<String> ultraShortForecast(@RequestBody @Valid WeatherInfo weatherInfo) {
        return weatherService.ultraShortForecast(weatherInfo);
    }

    @PostMapping("/weather/shortforecast")
    public Mono<String> shortForecast(@RequestBody @Valid WeatherInfo weatherInfo) {
        return weatherService.shortForecast(weatherInfo);
    }
}
