<template>
  <div class="mt-2">
    <h3>아이디</h3>
    <p id="email"> {{ errorResponse.email }} </p>
    <el-input v-model="email" placeholder="이메일을 입력해주세요."/>
  </div>

  <div class="mt-2">
    <h3>비밀번호</h3>
    <p id="password"> {{ errorResponse.password }} </p>
    <el-input v-model="password" type="password" rows="15" placeholder="숫자, 문자, 특수문자 포함 8~15자리 이내로 입력해주세요."/>
  </div>


  <div class="mt-2">
    <h3>전화번호</h3>
    <p id="phone"> {{ errorResponse.phone }} </p>
    <el-input v-model="phone" rows="15"
              maxlength="13"
              placeholder="숫자만 입력해주세요."
              :formatter='(phone) => `${phone}`.replace(/[^0-9]/g, "")
    .replace(/^(\d{0,3})(\d{0,4})(\d{0,4})$/g, "$1-$2-$3").replace(/(\-{1,2})$/g, "")'
    >
    </el-input>
  </div>


  <div class="mt-2">
    <h3>닉네임</h3>
    <p id="name">{{ errorResponse.nickname }}</p>
    <el-input v-model="nickname" rows="15" placeholder="닉네임을 입력해주세요."></el-input>
  </div>

  <div class="mt-2">
    <el-button type="primary" @click="join()">회원가입</el-button>
  </div>
</template>

<script setup lang="ts">

import {ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";

const router = useRouter();
const email = ref('');
const phone = ref('');
const nickname = ref('');
const password = ref('');

const errorResponse = ref({
  email: '',
  password: '',
  nickname: '',
  phone: ''
})

const join = function () {

  axios.post('/api/members/join', {
    email: email.value,
    password: password.value,
    nickname: nickname.value,
    phone: phone.value
  })
      .then((r) => {
        if (r.status == 200) {
          router.replace({name: "home"});
        }
      })
      .catch((e) => {
        errorResponse.value = {email: '', nickname: '', phone: '', password: ''};
        errorResponse.value = e.response.data.validation;
      })
}
</script>

<style scoped>
p {
  color: red;
  font-size: 0.8rem;
}
</style>