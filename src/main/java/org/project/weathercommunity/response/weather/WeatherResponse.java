package org.project.weathercommunity.response.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//JSON 입력 값에 대하여 Mapping 시 클래스에 선언되지 않은 property 무시
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    private String fcstTime;
    private String lgt;
    private String pty;
    private String rn1;
    private String sky;
    private String t1h;
    private String reh;
    private String uuu;
    private String vvv;
    private String vec;
    private String wsd;

    @Builder
    public WeatherResponse(String fcstTime, String lgt, String pty, String rn1, String sky, String t1h, String reh, String uuu, String vvv, String vec, String wsd) {
        this.fcstTime = fcstTime;
        this.lgt = lgt;
        this.pty = pty;
        this.rn1 = rn1;
        this.sky = sky;
        this.t1h = t1h;
        this.reh = reh;
        this.uuu = uuu;
        this.vvv = vvv;
        this.vec = vec;
        this.wsd = wsd;
    }
}
