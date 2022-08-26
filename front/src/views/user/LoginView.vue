<template>
  <div>
    <el-input v-model="email" placeholder="아이디"/>
  </div>

  <div class="mt-2">
    <el-input v-model="password" type="password" rows="15"></el-input>
  </div>

  <div class="mt-2">
    <el-button type="primary" @click="login()">로그인</el-button>
  </div>

</template>

<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";

const email = ref("");
const password = ref("")

const login = function () {
  axios.post("/api/members/join", {
    email: email.value,
    password: password.value
  }).then((res) => {
    console.log(res)
  }).catch((error) => {
    console.log(error);
    throw new Error();
  })
}
</script>