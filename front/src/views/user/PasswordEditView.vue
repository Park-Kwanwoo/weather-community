<template>
  <div>
    <h3 class="text-gray-900 font-weight-bold">마이페이지</h3>
    <br/>
    <B class="mb-2 text-gray-900">기본 정보</B>
    <hr>
    <div id="password-edit">
      <el-form class="user" label-width="120px">
        <el-form-item label="기존 비밀번호">
          <el-input type="password" v-model="passwordRequest.oldPassword"/>
        </el-form-item>

        <el-form-item label="변경할 비밀번호">
          <el-input type="password" v-model="passwordRequest.newPassword"/>
        </el-form-item>

        <el-form-item label="비밀번호 확인">
          <el-input type="password" v-model="passwordRequest.passwordCheck"/>
        </el-form-item>
        <el-form-item class="float-end">
          <el-button type="primary" id="passwordEditBtn" @click="edit">수정</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">

import axios from "axios";
import router from "@/router";
import {useAuthStore} from "@/stores/auth";
import {ElMessage} from "element-plus";
import {ref} from "vue";

const auth = useAuthStore();
const props = defineProps({
  memberId: {
    type: Number,
    require: true
  }
})

const configs = {
  headers: {
    Authorization: auth.getAccessToken
  }
}

const passwordRequest = ref({
  oldPassword: "",
  newPassword: "",
  passwordCheck: ""
});

const edit = function () {
  if (passwordRequest.value.oldPassword.length == 0) {
    ElMessage("비밀번호를 제대로 입력해주세요.")
  } else if (passwordRequest.value.newPassword != passwordRequest.value.passwordCheck){
    ElMessage("변경하려는 비밀번호가 일치하지 않습니다.")
  } else if (passwordRequest.value.newPassword.length == 0 || passwordRequest.value.passwordCheck.length == 0) {
    ElMessage("변경할 비밀번호를 입력해주세요.")
  } else {
    axios.patch(`/api/members/password/${props.memberId}`,
        {
          oldPassword: passwordRequest.value.oldPassword,
          newPassword: passwordRequest.value.newPassword,
        }, configs)
        .then(r => {
          router.push({name: 'home'})
        })
        .catch(e => {
          if (e.response.data.validation.token) {
            auth.clear();
            router.push({name: 'home'})
          } else if (e.response.data.validation.password){
            ElMessage(e.response.data.validation.password)
          } else if (e.response.data.validation.oldPassword) {
            ElMessage(e.response.data.validation.oldPassword)
          } else if (e.response.data.validation.newPassword) {
            ElMessage(e.response.data.validation.newPassword)
          }
        })
  }
};

</script>

<style lang="scss" scoped>
.el-form-item {
  width: 60%;
}

</style>