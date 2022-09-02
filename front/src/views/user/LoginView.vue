<template>
  <el-form
      :model="loginForm"
      label-width="120px"
      class="demo-ruleForm">
    <el-form-item
        label="email">
      <el-input name="email" v-model="loginForm.email" type="text"/>
    </el-form-item>
      <el-form-item label="password">
        <el-input name="password" v-model="loginForm.password" type="password"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="submitForm">로그인</el-button>
    </el-form-item>
  </el-form>
<!--  <form method="post" action="http://localhost:8080/login_proc">-->
<!--    <input type="text" title="email" name="email" placeholder="username" />-->
<!--    <input type="password" name="password" placeholder="password" />-->
<!--    <button type="submit" class="btn">Login</button>-->
<!--  </form>-->
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import axios from "axios";

const loginForm = reactive({
  email: '',
  password: '',
})

const configs = {
  headers: {
    "contentType": "x-www-form-urlencoded"
  }
}

const submitForm = () => {
  axios.postForm("/api/login_proc", loginForm, configs)
      .then((res) => {
        console.log(res)
      }).catch((error) => {
        console.log(error);
      })
}
</script>