<template>
  <el-menu
      default-active="2"
      class="el-menu-vertical-demo">
    <el-menu-item index="1">
      <el-icon><icon-menu /></el-icon>
      <el-link @click="write">글 쓰러가기</el-link>
    </el-menu-item>
    <el-menu-item index="2">
      <el-icon><document /></el-icon>
      <el-link @click="posts">글 목록</el-link>
    </el-menu-item>
  </el-menu>
</template>

<script lang="ts" setup>
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
} from '@element-plus/icons-vue'
import {useAuthStore} from "@/stores/auth";
import {storeToRefs} from "pinia";
import {useRouter} from "vue-router";
import axios from "axios";


const auth = useAuthStore();
const { getIsAuth } = storeToRefs(auth)
const router = useRouter();

const write = function () {

  if (getIsAuth.value) {
    router.push({name: 'write'})
  } else {
    router.replace({name: 'login'})
  }
};

const posts = function () {

  if (getIsAuth.value) {
    router.push({name: 'posts'})
  } else {
    router.replace({name: 'login'})
  }
};

</script>

<style>
.el-menu-vertical-demo:not(el-menu) {
  width: 200px;
  min-height: 400px;
}
</style>
