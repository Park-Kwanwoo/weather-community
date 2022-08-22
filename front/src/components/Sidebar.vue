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
      <template #title>Navigator Two</template>
    </el-menu-item>
    <el-menu-item index="3" disabled>
      <el-icon><document /></el-icon>
      <template #title>Navigator Three</template>
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
.el-menu-vertical-demo:not(.el-menu--collapse) {
  width: 200px;
  min-height: 400px;
}

el-menu-item router-link {
  text-decoration: none;
  color: #303030;
}
</style>
