<template>
  <div class="container">
    <div class="title">
      <h1>제목</h1>
      <el-input v-model="post.title" readonly></el-input>
    </div>
    <div class="content">
      <h1>내용</h1>
      <el-input type="textarea" v-model="post.content" resize="none" rows="30" readonly></el-input>
    </div>
    <div class="pt-4 justify-content-end">
      <el-button type="primary" v-if="flag" @click="edit">수정</el-button>
      <el-button type="warning" v-if="flag" @click="remove">삭제</el-button>
    </div>

  </div>
</template>

<script setup lang="ts">
import {defineProps, onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";
import {useAuthStore} from "@/stores/auth";
import {storeToRefs} from "pinia";

const props = defineProps({
  postId: {
    type: Number,
    require: true,
  },
});

const post = ref({
  id: 0,
  title: "",
  content: "",
  createdTime: "",
  memberEmail: "",
});

const flag = ref(false);
const router = useRouter();
const auth = useAuthStore();
const {getAccessToken} = storeToRefs(auth)
const {getEmail} = storeToRefs(auth)
const configs = {
  headers: {
    Authorization: getAccessToken.value
  }
}


const edit = () => {
  router.push({name: "edit", params: {postId: props.postId}});
}

const remove = () => {
  axios.delete(`/api/posts/${props.postId}`, configs)
      .then((r) => {
        router.replace({name: 'posts'})
      })
      .catch((e) => {
        console.log(e.response.data);
      });
}

onMounted(() => {
  axios.get(`/api/posts/${props.postId}`, configs).then((response) => {
    post.value = response.data;
    if (getEmail.value === post.value.memberEmail) {
      flag.value = true
    }
  }).catch(e => {

  })
})

</script>