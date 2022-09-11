<template>
  <div>
    <h1>오늘의 날씨</h1>
    <div id="map" style="width:750px;height:350px;"></div>
  </div>

</template>
<script>
import axios from "axios";
import {ref} from "vue";

const weatherData = {}

export default {

  data() {
    return {
      map: null
    }
  },
  mounted() {

    const script = document.createElement("script");
    script.src = '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=5a2c5c9f77995197f6b3cd4d1dc40862';
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
        kakao.maps.event.addListener(map, 'click', function (mouseEvent) {

          // 클릭한 위도, 경도 정보를 가져옵니다
          let latlng = mouseEvent.latLng;

          // 마커 위치를 클릭한 위치로 옮깁니다
          marker.setPosition(latlng);

          // 경도
          let nx = latlng.getLat();
          // 위도
          let ny = latlng.getLng();

          axios.post("/api/weather", {
            nx: nx,
            ny: ny
          })
              .then(r => {
                r.data.response.body.items.item.forEach((d) => {
                  weatherData[d.category] = d.obsrValue;
                });

                console.log(weatherData)
              })
              .catch(e => {
                console.log(e.response.data)
              })
        });
      })
    }
  }
}
</script>