<template>
  <el-form
      label-width="120px"
      class="demo-ruleForm">
    <el-form-item
        label="email">
      <el-input name="email" v-model="loginData.email" type="text"/>
    </el-form-item>
      <el-form-item label="password">
        <el-input name="password" v-model="loginData.password" type="password"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" v-on:click.prevent="submitForm" class="btn-primary">로그인</el-button>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import axios from "axios";
import {reactive, ref} from "vue";
import {useRouter} from "vue-router";

const router = useRouter();

const loginData = reactive({
  email: '',
  password: '',
})

const configs = {
  headers: {
    "X-Requested-With": "XMLHttpRequest"
  }
}

const errorMsg = ref('');

const submitForm = () => {
  axios.post("/api/members/login", loginData, configs)
      .then((r) => {
        console.log(r)
      })
      .catch(e => {
        console.log(e.response.data.message)
        router.replace("/login?error=" + e.response.data.message)
        alert(e.response.data.message)
      })
}
</script>