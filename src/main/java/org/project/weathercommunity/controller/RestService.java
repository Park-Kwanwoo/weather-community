package org.project.weathercommunity.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

@Service
public class RestService {

    private static final String BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0";

    public Mono<String> getDayWeather() {

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BASE_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/getUltraSrtNcst")
                                .queryParam("serviceKey", "nKJLr9q512o8UEWsDGYEgLdMFLNMgUId3e3mqbsMwGLK7Mf9ntFf38YmKFa5Dt3HIY%2B5NBbIoDYh3kBuHqMLiA%3D%3D")
                                .queryParam("numOfRows", "12")
                                .queryParam("pageNo", "1")
                                .queryParam("dataType", "JSON")
                                .queryParam("nx", "37")
                                .queryParam("ny", "124")
                                .queryParam("base_date", "20220809")
                                .queryParam("base_time", "0800")
                                .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
