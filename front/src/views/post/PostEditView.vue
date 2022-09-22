<template>
  <div>
    <el-input v-model="post.title" placeholder="제목을 입력해주세요"/>
  </div>

  <div class="mt-2">
    <el-input v-model="post.content" type="textarea" rows="15"></el-input>
  </div>

  <div class="mt-2">
    <el-button type="warning" @click="edit()">수정 완료</el-button>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";
import {useAuthStore} from "@/stores/auth";

const post = ref({
  id: 0,
  title: "",
  content: ""
});

const router = useRouter();
const auth = useAuthStore();

const props = defineProps({
  postId: {
    type:[Number, String],
    require: true
  }
})

axios.get(`/api/posts/${props.postId}`).then((response) => {
  post.value = response.data;
})

const edit = function () {
  axios.patch(`/api/posts/${props.postId}`, post.value)
      .then(() => {
        auth.clear();
        router.replace({name: "login"})
      })
};
</script>

<style scoped>

</style>