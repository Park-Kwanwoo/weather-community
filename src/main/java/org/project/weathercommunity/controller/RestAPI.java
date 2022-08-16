package org.project.weathercommunity.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RestAPI {

    private final RestService restService;


    @GetMapping("/weather")
    public Mono<String> weatherInformation() {

        return restService.getDayWeather();
    }
}
