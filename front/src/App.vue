<script setup lang="ts">

import TopMenu from "@/components/Header.vue";
import Sidebar from "@/components/Sidebar.vue";
import Content from "@/components/Content.vue";
import Footer from "@/components/Footer.vue";
</script>

<template>
  <TopMenu/>
  <el-container style="display: flex">
    <Sidebar/>
    <Content/>
  </el-container>
  <Footer/>
</template>

<style>

</style>