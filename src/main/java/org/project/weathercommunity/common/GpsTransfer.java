package org.project.weathercommunity.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

import static java.lang.Math.*;

@Getter
@Setter
@ToString
public class GpsTransfer {
    private double lat; // gps로 반환받은 위도 (x좌표 값)
    private double lon; // gps로 반환받은 경도 (y좌표 값)

    private double xLat; // x좌표로 변환된 위도
    private double yLon; // y좌표로 변환된 경도

    private GpsTransfer(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public static Map<String, Integer> transfer(double longitude, double latitude) {

        /**
         * LCC DFS 좌표 변환
         * "TO_GRID"(위경도-> 좌표, lat_Y: 위도, lng_X: 경도)
         */
        final double RE = 6371.00877;  // 지구 반경(km)
        final double GRID = 5.0;       // 격자 간격(km)
        final double SLAT1 = 30.0;     // 투영 위도1(degree)
        final double SLAT2 = 60.0;     // 투영 위도2(degree)
        final double OLON = 126.0;     // 기준점 경도(degree)
        final double OLAT = 38.0;      // 기준점 위도(degree)
        final double XO = 43;          // 기준점 X좌표(GRID)
        final double YO = 136;         // 기준점 Y좌표(GRID)

        final double DEGRAD = PI / 180.0;
        final double RADDEG = 180.0 / PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = tan(PI * 0.25 + slat2 * 0.5) / tan(PI * 0.25 + slat1 * 0.5);
        sn = log(cos(slat1) / cos(slat2)) / log(sn);
        double sf = tan(PI * 0.25 + slat1 * 0.5);
        sf = pow(sf, sn) * cos(slat1) / sn;

        double ro = tan(PI * 0.25 + olat * 0.5);
        ro = re * sf / pow(ro, sn);

        double ra = tan(PI * 0.25 + (latitude) * DEGRAD * 0.5);
        ra = re * sf / pow(ra, sn);
        double theta = (longitude) * DEGRAD - olon;
        if (theta > PI) theta -= 2.0 * PI;
        if (theta < -PI) theta += 2.0 * PI;
        theta *= sn;
        int x = (int) (floor(ra * sin(theta)) + XO);
		int y = (int) (floor(ro - ra*cos(theta)) + YO);

        return Map.of("nx", x, "ny", y);
    }
}
