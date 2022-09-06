<template>
  <el-header class="header">
    <el-menu mode="horizontal">
      <img alt="" src="">
      <el-menu-item v-if="!getIsAuth" @click="login">로그인</el-menu-item>
      <el-menu-item v-if="!getIsAuth" @click="join">회원가입</el-menu-item>
      <el-menu-item v-if="getIsAuth" @click="myInfo">내 정보</el-menu-item>
      <el-menu-item v-if="getIsAuth" @click="logout">로그아웃</el-menu-item>
    </el-menu>
  </el-header>
</template>

<script setup lang="ts">

import {useAuthStore} from "@/stores/auth";
import {storeToRefs} from "pinia";
import {useRouter} from "vue-router";

const auth = useAuthStore();
const { getIsAuth } = storeToRefs(auth)
const router = useRouter();

const logout = function () {
  auth.clear();
  router.replace({name: 'home'})
};
const login = () => {
  router.replace( {name: 'login'})
}
const join = () => {
  router.replace( {name: 'join'})
}
const myInfo = () => {
  router.replace( {name: 'myInfo'})
}
</script>

<style >

</style>
