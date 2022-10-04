<template>
  <div>
    <h1>자유 게시판</h1>
  </div>
  <div class="el-form-item">
    <el-table :data="posts" :height="440">
      <el-table-column prop="id" width="100" label="번호"/>
      <el-table-column prop="title" label="제목">
        <template v-slot:default="table">
          <router-link :to="{name: 'post', params: { postId: table.row.id }}" tag="span">
            {{ table.row.title }}
          </router-link>
        </template>
      </el-table-column>
      <el-table-column prop="memberName" label="작성자" width="150px"/>
      <el-table-column prop="createdTime" label="작성일" width="150px"/>
    </el-table>
  </div>
  <div class="pagination" >
    <el-pagination
        style="justify-content: center"
        small
        background
        layout="prev, pager, next"
        :total="total()"
        @next-click="pagination"
        @prev-click="pagination"
        @current-change="pagination"
        class="mt-4"
    />
  </div>
  <div>
    <el-button v-if="isAuth" type="primary" @click="write">글 작성</el-button>
  </div>
</template>

<script setup lang="ts">
import axios from "axios";
import {ref} from "vue";
import {useAuthStore} from "@/stores/auth";
import {storeToRefs} from "pinia";
import router from "@/router";
import {ElMessage} from "element-plus";

const auth = useAuthStore();
const posts = ref([])
const totalPage = ref(0);
const { isAuth } = storeToRefs(auth)
const { getAccessToken } = storeToRefs(auth);

const configs = {
  headers: {
    Authorization: getAccessToken.value,
  }
}

axios.get("/api/post/totalPage", configs)
    .then(r => {
      totalPage.value = r.data;
    }).catch(e => {
      ElMessage(e.response.data)
})


axios.get(`/api/posts?page=1&size=10`, configs)
    .then(res => {
      res.data.forEach((r: any) => {
        posts.value.push(r);
      })
    })
    .catch(e => {
      ElMessage(e.response.data)
      auth.clear();
      router.push({name: 'login'})
    })

const write = function () {
  router.replace( {name: 'write'} )
};

const total = function () {
  return totalPage.value > 10 ? totalPage.value : 10;
};

const pagination = (e: number) => {

  axios.get(`/api/posts?page=${e}&size=10`, configs)
      .then(res => {
        posts.value.length = 0
        res.data.forEach((r: any) => {
          posts.value.push(r);
        })
      })
      .catch(e => {
        ElMessage(e.response.data)
        auth.clear();
        router.push({name: 'login'})
      })
}
</script>
