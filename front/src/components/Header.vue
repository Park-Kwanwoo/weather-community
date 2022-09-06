<template>
  <el-header class="header">
    <el-menu mode="horizontal">
      <img alt="" src="">
      <el-menu-item><router-link v-if="!isAuth" to="login">Login</router-link></el-menu-item>
      <el-menu-item><router-link v-if="!isAuth" to="join">Join</router-link></el-menu-item>
      <el-menu-item><router-link v-if="isAuth" to="join">내 정보</router-link></el-menu-item>
    </el-menu>
  </el-header>
</template>

<script setup lang="ts">

import {useAuthStore} from "@/stores/auth";
import {storeToRefs} from "pinia";

const auth = useAuthStore();
const { isAuth } = storeToRefs(auth)


</script>

<style >

</style>
