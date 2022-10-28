package org.project.weathercommunity.service.weather;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.project.weathercommunity.common.GpsTransfer;
import org.project.weathercommunity.common.WeatherResponseParser;
import org.project.weathercommunity.request.weather.WeatherRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Slf4j
public class WeatherService {

    private static final String BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0";
    private static final String SERVICE_KEY = "jS6Wrc0k0cSlZlPaVsvx3HIMQcjkWzob1pr0nJTL%2F3Ko8duvWK99XPKdoG4pMddHrSnXOK9IY%2FKvyHOo48Csjg%3D%3D";
    private static final String NUM_OF_ROWS = "60";
    private static final String PAGE_NO = "1";
    private static final String DATA_TYPE = "JSON";

    public Mono<String> ultraShortForecast(WeatherRequest weatherRequest) {

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory(BASE_URL);
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.VALUES_ONLY);

        WebClient webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .baseUrl(BASE_URL)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Map<String, Integer> transfer = GpsTransfer.transfer(weatherRequest.getLongitude(), weatherRequest.getLatitude());

        return webClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/getUltraSrtFcst")
                                .queryParam("serviceKey", SERVICE_KEY)
                                .queryParam("numOfRows", NUM_OF_ROWS)
                                .queryParam("pageNo", PAGE_NO)
                                .queryParam("dataType", DATA_TYPE)
                                .queryParam("nx", transfer.get("nx"))
                                .queryParam("ny", transfer.get("ny"))
                                .queryParam("base_date", weatherRequest.getBaseDate())
                                .queryParam("base_time", weatherRequest.getBaseTime())
                                .build())
                .retrieve()
                .bodyToMono(String.class);
    }

}
