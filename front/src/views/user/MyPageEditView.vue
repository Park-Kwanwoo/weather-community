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

      <el-form-item label="닉네임">
        <el-input v-model="userInfo.nickname" />
      </el-form-item>

      <el-form-item label="전화번호">
        <el-input v-model="userInfo.phone" readonly />
      </el-form-item>
      <el-form-item label="">
        <el-button type="primary" @click="edit">수정</el-button>
        <el-button type="warning" @click="remove">삭제</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import axios from "axios";
import {defineProps, ref} from "vue";
import {useAuthStore} from "@/stores/auth";
import {storeToRefs} from "pinia";
import router from "@/router";
import {ElMessage} from "element-plus";

const auth = useAuthStore();
const { getAccessToken } = storeToRefs(auth);

const props = defineProps({
  memberId: {
    type: Number,
    require: true
  }
})

const userInfo = ref({
  email: "",
  nickname: "",
  phone: ""
})

const configs = {
  headers: {
    Authorization: getAccessToken.value
  }
}

axios.get(`/api/members/${props.memberId}`, configs)
    .then(r => {
      userInfo.value = r.data
    })
    .catch(e => {
      alert(e.response.data)
      router.push({name: 'home'})
    })

const edit = function () {
  axios.patch(`/api/members/${props.memberId}`, {
    nickname: userInfo.value.nickname
  }, configs)
      .then(r => {
        router.push({name: 'myPage', params: {memberId: props.memberId}})
      })
      .catch(e => {
        ElMessage(e.response.data.validation.nickname)
      })
};


const remove = () => {
  axios.delete(`/api/members/${props.memberId}`, configs)
      .then(r => {
        auth.clear();
        router.replace({name: 'home'})
      })
      .catch(e => {
        ElMessage(e.response.data)
        auth.clear();
        router.push({name: 'home'})
      })
}
</script>
