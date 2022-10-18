package org.project.weathercommunity.service.weather;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import static org.springframework.web.util.DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY;


class WeatherServiceTest {

    private static final String BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0";
    private static final String SERVICE_KEY = "jS6Wrc0k0cSlZlPaVsvx3HIMQcjkWzob1pr0nJTL%2F3Ko8duvWK99XPKdoG4pMddHrSnXOK9IY%2FKvyHOo48Csjg%3D%3D";
    private static final String NUM_OF_ROWS = "10";
    private static final String PAGE_NO = "1";
    private static final String DATA_TYPE = "JSON";

    @Test
    @DisplayName("API 값 가져오기")
    void GET_API_VALUE() {

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BASE_URL);
        factory.setEncodingMode(VALUES_ONLY);

        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<String> stringMono = webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/getUltraSrtNcst")
                                .queryParam("serviceKey", SERVICE_KEY)
                                .queryParam("numOfRows", NUM_OF_ROWS)
                                .queryParam("pageNo", PAGE_NO)
                                .queryParam("dataType", DATA_TYPE)
                                .queryParam("nx", "42.3167")
                                .queryParam("ny", "130.400")
                                .queryParam("base_date", "20220822")
                                .queryParam("base_time", "1500")
                                .build())
                .retrieve()
                .bodyToMono(String.class);

        System.out.println(stringMono.block());
    }

}