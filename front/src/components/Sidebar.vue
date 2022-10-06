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
      <el-link><router-link :to="{name: 'posts'}">글 목록</router-link></el-link>
    </el-menu-item>
    <el-sub-menu v-if="getIsAuth" index="3">
      <template #title>
        <el-icon><Setting /></el-icon>
        <span>회원 정보</span>
      </template>
        <el-menu-item index="1-1" @click="personalInfoEdit">개인정보 수정</el-menu-item>
        <el-menu-item index="1-2" @click="passwordEdit">비밀번호 수정</el-menu-item>
    </el-sub-menu>
  </el-menu>
</template>

<script lang="ts" setup>
import {
  Document,
  Menu as IconMenu,
  Setting,
} from '@element-plus/icons-vue'
import {useAuthStore} from "@/stores/auth";
import {storeToRefs} from "pinia";
import {useRouter} from "vue-router";

const auth = useAuthStore();
const { getIsAuth } = storeToRefs(auth)
const { getId } = storeToRefs(auth)
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

const personalInfoEdit = () => {
  router.replace( {name: 'myPage', params:{memberId: getId.value}})
}

const passwordEdit = () => {
  router.replace( {name: 'password', params:{memberId: getId.value}})
}


</script>

<style>
.el-menu-vertical-demo:not(el-menu) {
  width: 200px;
  min-height: 400px;
}

</style>
