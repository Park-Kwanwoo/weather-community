<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {useAuthStore} from "@/stores/auth";
import {storeToRefs} from "pinia";
import router from "@/router";

const auth = useAuthStore();
const posts = ref([])
const { isAuth } = storeToRefs(auth)

axios.get("/api/posts?page=1&size=10").then((response) => {
  response.data.forEach((r: any) => {
    posts.value.push(r);
  })
})

const write = function () {
  router.replace( {name: 'write'} )
};
</script>

<template>
  <div>
    <ul>
      <li v-for="post in posts" :key="post.id">
        <div class="title">
          <router-link :to="{name: 'post', params: { postId: post.id}}">{{ post.title }}</router-link>
        </div>
      </li>
    </ul>
  </div>
  <div>
    <el-button v-if="isAuth" type="primary" @click="write">글 작성</el-button>
  </div>
</template>

<style scoped>
ul {
  list-style: none;
  padding: 0;
}

li {
  margin-bottom: 1.3rem;
}

li .title {
  font-size: 1.2rem;
  color: #303030;
}

li .title > a {
  text-decoration: none;
}

li .content {
  font-size: 0.95rem;
  color: #5d5d5d;
}

li:last-child {
  margin-bottom: 0;
}
</style>
