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
        <el-button type="primary" @click="edit()">수정</el-button>
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
import {ElMessage} from "element-plus";

const auth = useAuthStore();
const router = useRouter();
const { getAccessToken } = storeToRefs(auth)

const props = defineProps({
  memberId: {
    type: Number,
    require: true
  }
})

const configs = {
  headers: {
    Authorization: getAccessToken.value
  }
}

const userInfo = ref({
  id: 0,
  email: "",
  nickname: "",
  phone: ""
})

onMounted(() => {
axios.get(`/api/members/${props.memberId}`, configs)
    .then(r => {
      userInfo.value = r.data
    })
    .catch(e => {
      ElMessage(e.response.data)
      auth.clear();
      router.replace({name: 'home'})
    })
})
const edit = function () {
  router.push({name: 'myPageEdit', params: {memberId: props.memberId}})
};

</script>
