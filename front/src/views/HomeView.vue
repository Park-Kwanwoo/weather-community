<template>
  <div>
    <h1>오늘의 날씨</h1>
    <div style="display: flex">
      <el-input placeholder="지역을 입력해주세요." style="width: 30%; padding-right: 1%"></el-input>
      <el-button type="primary" @click="">검색</el-button>
    </div>
    <div>
      <el-table :data="weatherData" height="280px" style="width: 60%">
        <el-table-column prop="fcstTime" label="시간"/>
        <el-table-column prop="T1H" label="기온"/>
        <el-table-column prop="REH" label="습도"/>
        <el-table-column prop="WSD" label="풍속"/>
        <el-table-column prop="SKY" label="하늘 상태"/>
        <el-table-column prop="RN1" label="예상 강수량"/>
        <el-table-column prop="PTY" label="강수형태"/>
      </el-table>
    </div>
    <br/>
    <div id="map" style="width:750px;height:350px;"></div>
  </div>
</template>

<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";

const responseWeatherData = ref([])
const key = import.meta.env.VITE_API_KEY;
let date = new Date();
let currentDate = date.toJSON().slice(0, 10).replace(/-/g, '');
let currentHour = date.getHours();
let currentMinutes = date.getMinutes();

const weatherData = ref([])

if (currentMinutes < 30) {
  if (currentHour === 0) {
    currentHour = 23;
  } else {
    currentHour -= 1;
  }

  currentMinutes = 50;
}
let currentTime = currentHour + "" + currentMinutes;

const script = document.createElement("script");
script.src = '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=' + key;
document.head.appendChild(script)

/*
스크립트가 로드 되기전에 kakao객체에 접근하여 kakao객체를 찾지 못해 발생하는 에러를
방지하기 위해 스크립트가 로드 된 뒤에 실제 지도를 그리도록 함
*/

script.onload = () => {
  kakao.maps.load(() => {
    let container = document.getElementById('map');
    let options = { //지도를 생성할 때 필요한 기본 옵션
      center: new kakao.maps.LatLng(37.498095, 127.027610), //지도의 중심좌표.
      level: 3 //지도의 레벨(확대, 축소 정도)
    };
    let map = new kakao.maps.Map(container, options);

    // 지도를 클릭한 위치에 표출할 마커입니다
    let marker = new kakao.maps.Marker({
      // 지도 중심좌표에 마커를 생성합니다
      position: map.getCenter()
    });

    // 지도에 마커를 표시합니다
    marker.setMap(map);

    // 지도에 클릭 이벤트를 등록합니다
    // 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
    kakao.maps.event.addListener(map, 'click', function (mouseEvent: any) {

      // 클릭한 위도, 경도 정보를 가져옵니다
      let latlng = mouseEvent.latLng;

      // 마커 위치를 클릭한 위치로 옮깁니다
      marker.setPosition(latlng);

      // 위도 (y)
      let latitude = latlng.getLat();
      // 경도 (x)
      let longitude = latlng.getLng();

      axios.post("/api/weather/forecast", {
        baseDate: currentDate,
        baseTime: currentTime,
        longitude: parseFloat(longitude),  // 경도
        latitude: parseFloat(latitude),   // 위도
      })
          .then(r => {
            r.data.response.body.items.item.forEach((d) => {
              responseWeatherData.value.push(d);
            });
            weatherData.value.length = 0;
            for (let i = 0; i < 6; i++) {
              let stringData = {
                fcstTime: responseWeatherData.value[i].fcstTime.slice(0,2) + "시",
                LGT: responseWeatherData.value[i].fcstValue,
                PTY: responseWeatherData.value[i + 6].fcstValue,
                RN1: responseWeatherData.value[i + 12].fcstValue,
                SKY: responseWeatherData.value[i + 18].fcstValue,
                T1H: responseWeatherData.value[i + 24].fcstValue + "°C",
                REH: responseWeatherData.value[i + 30].fcstValue + "%",
                UUU: responseWeatherData.value[i + 36].fcstValue,
                VVV: responseWeatherData.value[i + 42].fcstValue,
                VEC: responseWeatherData.value[i + 48].fcstValue,
                WSD: responseWeatherData.value[i + 54].fcstValue + "m/s"
              }
              weatherData.value.push(stringData);
            }
            console.log(weatherData)
          })
          .catch(e => {
            console.log(e.response.data)
          })
    });
  })
}

</script>