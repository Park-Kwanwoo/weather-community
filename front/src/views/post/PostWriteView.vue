<template>
  <div>
    <el-input v-model="title" placeholder="제목을 입력해주세요"/>
  </div>

  <div class="mt-2">
    <el-input v-model="content" type="textarea" rows="15"></el-input>
  </div>

  <div class="mt-2">
    <el-button type="primary" @click="write()">글 작성</el-button>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";
import {useAuthStore} from "@/stores/auth";
import {storeToRefs} from "pinia";

const title = ref("")
const content = ref("")
const router = useRouter();
const auth = useAuthStore();
const { getAccessToken } = storeToRefs(auth)
const configs = {
  headers: {
    Authorization: getAccessToken.value
  }
}

const write = function () {
  axios.post("/api/posts/create", {
    title: title.value,
    content: content.value,
  },  configs)
      .then(() => {
        router.replace({name: "posts"})
      })
      .catch(e => {
        auth.clear();
        router.push({name: "login"})
      })
};
</script>

<style scoped>

</style>