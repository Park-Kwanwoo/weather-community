<template>
  <el-menu
      default-active="2"
      class="el-menu-vertical-demo">
    <el-sub-menu index="1">
      <template #title>
        <el-icon><location /></el-icon>
        <span>날씨 보기</span>
      </template>
        <el-menu-item index="1-3">
          <el-link><router-link to="#">지역별 날씨</router-link></el-link>
        </el-menu-item>
<!--      <el-sub-menu index="1-4">-->
<!--        <template #title><span>item four</span></template>-->
<!--        <el-menu-item index="1-4-1">item one</el-menu-item>-->
<!--      </el-sub-menu>-->
    </el-sub-menu>
    <el-menu-item index="2">
      <el-icon><icon-menu /></el-icon>
      <el-link><router-link to="/post/write">글 쓰러가기</router-link></el-link>
    </el-menu-item>
    <el-menu-item index="3">
      <el-icon><document /></el-icon>
      <template #title><router-link to="/posts">글 목록</router-link></template>
    </el-menu-item>
    <el-menu-item index="4">
      <el-icon><setting /></el-icon>
      <template #title>Navigator Four</template>
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

</script>

<style>
.el-menu-vertical-demo:not(el-menu) {
  width: 200px;
  min-height: 400px;
}
</style>
