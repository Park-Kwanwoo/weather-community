<template>
  <div class="container">
    <div class="title">
      <h2>{{ post.title }}</h2>
    </div>
    <div class="content">
      {{ post.content }}
    </div>
    <div>
      <el-button type="primary" v-if="post.flag" @click="edit()">수정</el-button>
      <el-button type="warning" v-if="post.flag" @click="remove()">삭제</el-button>
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
  memberId: 0,
  flag: false
});

const router = useRouter();
const auth = useAuthStore();
const { getIsAuth } = storeToRefs(auth);


const edit = () => {
  router.push({name: "edit", params: {postId: props.postId}});
}

const remove = () => {
  axios.delete(`/api/posts/${props.postId}`)
      .then((r) => {
        router.replace({name: 'posts'})
      })
      .catch((e) => {
        console.log(e.response.data);
      });
}

onMounted(() => {
  axios.get(`/api/posts/${props.postId}`).then((response) => {
    post.value = response.data;
  })
})

</script>