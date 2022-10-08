<template>
  <el-header class="header">
    <el-menu mode="horizontal">
      <el-menu-item @click="home"><router-link :to="{name:'home'}">HOME</router-link></el-menu-item>
      <el-menu-item v-if="!auth.getIsAuth" @click="login">로그인</el-menu-item>
      <el-menu-item v-if="!auth.getIsAuth" @click="join">회원가입</el-menu-item>
      <el-menu-item v-if="auth.getIsAuth" @click="logout">로그아웃</el-menu-item>
    </el-menu>
  </el-header>
</template>

<script setup lang="ts">

import {useAuthStore} from "@/stores/auth";
import {storeToRefs} from "pinia";
import {useRouter} from "vue-router";
import axios from "axios";

const auth = useAuthStore();
const router = useRouter();
const configs = {
  headers: {
    Authorization: auth.getAccessToken
  }
}

const test = function () {
  auth.clear();
  router.push({name: 'home'})
};

const logout = function () {

  axios.get("/api/members/logout", configs).then(r => {
    console.log(r.data)
    auth.clear();
    router.push({name: 'home'})
  }).catch(e => {
    // 토큰 만료되었을 때
    auth.clear();
    router.push({name: 'home'})
  });

};

const home = () => {
  router.push({name: 'home'})
}
const login = () => {
  router.replace({name: 'login'})
}
const join = () => {
  router.replace({name: 'join'})
}
</script>