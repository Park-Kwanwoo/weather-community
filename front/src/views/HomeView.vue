<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";

const posts = ref([])

axios.get("/api/posts?page=1&size=10").then((response) => {
  response.data.forEach((r: any) => {
    posts.value.push(r);
  })
})
</script>

<template>
  <ul>
    <li v-for="post in posts" :key="post.id">
      <div class="title">
        <router-link :to="{name: 'read', params: { postId: post.id}}">{{post.title}}</router-link>
      </div>

      <div class="content">
        {{post.content}}
      </div>
    </li>
  </ul>
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
