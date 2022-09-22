<template>

  <div>
    <h3 class="text-gray-900 font-weight-bold">마이페이지</h3>
    <br/>
    <B class="mb-2 text-gray-900">기본 정보</B>
    <hr>
    <el-form class="user" label-width="120px">
      <el-form-item label="이메일">
        <el-input v-model="userInfo.email" readonly/>
      </el-form-item>

      <el-form-item label="이름">
        <el-input v-model="userInfo.name" />
      </el-form-item>

      <el-form-item label="전화번호">
        <el-input v-model="userInfo.phone" readonly />
      </el-form-item>
      <el-form-item label="">
        <el-button type="primary" @click="edit()">수정</el-button>
        <el-button type="warning" @click="remove()">삭제</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import axios from "axios";
import {defineProps, onMounted, ref} from "vue";
import {useAuthStore} from "@/stores/auth";
import {storeToRefs} from "pinia";
import {useRouter} from "vue-router";
import router from "@/router";

const auth = useAuthStore();
const { getAccessToken } = storeToRefs(auth);
const { getEmail } = storeToRefs(auth);

const userInfo = ref({
  id: 0,
  email: "",
  password: "",
  name: "",
  phone: ""
})

const configs = {
  headers: {
    Authorization: getAccessToken.value
  }
}

axios.get(`/api/members/${getEmail.value}`, configs)
    .then(r => {
      userInfo.value = r.data
    })
    .catch(e => {
      alert(e.response.data)
      router.push({name: 'home'})
    })

const edit = function () {
  axios.patch(`/api/members/${getEmail.value}`, {
    password: userInfo.value.password,
    name: userInfo.value.name,
    phone: userInfo.value.phone,
  }, configs)
      .then(r => {
        router.replace({name: 'myPage'})
      })
      .catch(e => {
        alert(e.response.data.validation.name)
      })
};


const remove = () => {
  axios.delete(`/api/members/${getEmail.value}`, configs)
      .then(r => {
        auth.clear();
        router.replace({name: 'home'})
      })
      .catch(e => {
        alert(e.response.data)
        auth.clear();
        router.push({name: 'home'})
      })
}
</script>
