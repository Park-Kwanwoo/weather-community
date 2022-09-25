<template>
  <div>
    <h1>오늘의 날씨</h1>
    <div>
거      <el-table :data="weatherData" height="280px" style="width: 50%">
        <el-table-column prop="fcstTime" label="시간"/>
        <el-table-column prop="t1h" label="기온"/>
        <el-table-column prop="reh" label="습도"/>
        <el-table-column prop="wsd" label="풍속"/>
        <el-table-column prop="sky" label="하늘 상태"/>
        <el-table-column prop="rn1" label="예상 강수량"/>
        <el-table-column prop="pty" label="강수형태"/>
      </el-table>
    </div>
    <br/>
    <div id="map" style="width:750px;height:350px;"></div>
  </div>
</template>

<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";

const mapKey = import.meta.env.VITE_MAP_KEY;

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
script.src = 'http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=' + mapKey;
document.body.appendChild(script)

/*
스크립트가 로드 되기전에 kakao객체에 접근하여 kakao객체를 찾지 못해 발생하는 에러를
방지하기 위해 스크립트가 로드 된 뒤에 실제 지도를 그리도록 함
*/
script.onload = () => {
  kakao.maps.load(() => {

    const container = document.getElementById('map');
    const options = { //지도를 생성할 때 필요한 기본 옵션
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

    // HTML5의 geolocation으로 사용할 수 있는지 확인합니다
    if (navigator.geolocation) {

      // GeoLocation을 이용해서 접속 위치를 얻어옵니다
      navigator.geolocation.getCurrentPosition(function (position) {

        let lat = position.coords.latitude, // 위도
            lon = position.coords.longitude; // 경도

        let locPosition = new kakao.maps.LatLng(lat, lon) // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다

        // 마커와 인포윈도우를 표시합니다
        displayMarker(locPosition);
      });

    } else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다

      let locPosition = new kakao.maps.LatLng(33.450701, 126.570667)

      displayMarker(locPosition);
    }

    function displayMarker(locPosition: any) {
      // 지도 중심좌표를 접속위치로 변경합니다
      map.setCenter(locPosition);
    }

    // 지도에 클릭 이벤트를 등록합니다
    // 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
    kakao.maps.event.addListener(map, 'click', function (mouseEvent: any) {

      // 클릭한 위도, 경도 정보를 가져옵니다
      let latlng = mouseEvent.latLng;

      marker.setPosition(latlng);

      // 마커 위치를 클릭한 위치로 옮깁니다
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

            if (weatherData.value.length != 0) {
              weatherData.value.length = 0;
            }
            r.data.forEach((d: any) => {
              weatherData.value.push(d)
            });
          })
          .catch(e => {
            console.log(e.response.data)
          })
    });
  })
}
</script>