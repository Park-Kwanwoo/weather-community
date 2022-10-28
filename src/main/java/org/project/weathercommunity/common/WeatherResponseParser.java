package org.project.weathercommunity.common;

import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.project.weathercommunity.response.weather.WeatherResponse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class WeatherResponseParser {

    public static List<WeatherResponse> Parse(String data) throws ParseException {

        // json 데이터 변환
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(data);
        JSONObject parse_response = (JSONObject) jsonObject.get("response");
        JSONObject parse_body = (JSONObject) parse_response.get("body"); // response 로 부터 body 찾아오기
        JSONObject parse_items = (JSONObject) parse_body.get("items"); // body 로 부터 items 받아오기
        JSONArray parse_item = (JSONArray) parse_items.get("item");

        // item들을 담을 List
        JSONObject object;
        String fcstTime;
        String lgt;
        String pty;
        String rn1;
        String sky;
        String t1h;
        String reh;
        String uuu;
        String vvv;
        String vec;
        String wsd;


        List<WeatherResponse> responseList = new ArrayList<>();

        // category 횟수 만큼 나눈다.
        for (int i = 0; i < parse_item.size() / 10; i++) {
            object = (JSONObject) parse_item.get(i);
            fcstTime = (String) object.get("fcstTime");
            fcstTime = fcstTime.substring(0, 2) + "시";
            lgt = (String) object.get("fcstValue");
            object = (JSONObject) parse_item.get(i + 6);
            pty = (String) object.get("fcstValue");
            switch (pty) {
                case "0":
                    pty = "없음";
                    break;
                case "1":
                    pty = "비";
                    break;
                case "2":
                    pty = "비/눈";
                    break;
                case "3":
                    pty = "눈";
                    break;
                case "4":
                    pty = "소나기";
                    break;
                case "5":
                    pty = "빗방울";
                    break;
                case "6":
                    pty = "빗방울눈날림";
                    break;
                case "7":
                    pty = "눈날림";
                    break;
            }

            object = (JSONObject) parse_item.get(i + 12);
            rn1 = (String) object.get("fcstValue");
            object = (JSONObject) parse_item.get(i + 18);
            sky = (String) object.get("fcstValue");
            switch (sky) {
                case "1":
                    sky = "맑음";
                    break;
                case "3":
                    sky = "구름 많음";
                    break;
                case "4":
                    sky = "흐림";
                    break;
            }
            object = (JSONObject) parse_item.get(i + 24);
            t1h = (String) object.get("fcstValue");
            t1h = t1h + "°C";
            object = (JSONObject) parse_item.get(i + 30);
            reh = (String) object.get("fcstValue");
            reh = reh + "%";
            object = (JSONObject) parse_item.get(i + 36);
            uuu = (String) object.get("fcstValue");
            object = (JSONObject) parse_item.get(i + 42);
            vvv = (String) object.get("fcstValue");
            object = (JSONObject) parse_item.get(i + 48);
            vec = (String) object.get("fcstValue");
            object = (JSONObject) parse_item.get(i + 54);
            wsd = (String) object.get("fcstValue");
            wsd = wsd + "m/s";

            responseList.add(WeatherResponse.builder()
                    .fcstTime(fcstTime)
                    .lgt(lgt)
                    .pty(pty)
                    .rn1(rn1)
                    .sky(sky)
                    .t1h(t1h)
                    .reh(reh)
                    .uuu(uuu)
                    .vvv(vvv)
                    .vec(vec)
                    .wsd(wsd)
                    .build());
        }

        return responseList;
    }
}
