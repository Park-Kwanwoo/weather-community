<template>
  <div class="container">
    <div class="title">
      <h1>제목</h1>
      <el-input v-model="post.title" readonly></el-input>
    </div>
    <div class="content">
      <h1>내용</h1>
      <el-input type="textarea" v-model="post.content" resize="none" rows="25" readonly></el-input>
    </div>
    <div class="pt-4 float-end">
      <el-button type="primary" v-if="flag" @click="edit">수정</el-button>
      <el-button type="warning" v-if="flag" @click="remove">삭제</el-button>
    </div>
  </div>

  <div class="comments mt-5">
    <h2>댓글</h2>
    <div class="comment">
      <div class="comment-write">
        <el-input type="textarea" resize="none" v-model="comment.content" rows="3"></el-input>
      </div>
      <div class="comment-request mt-2">
        <el-button class="float-end" type="info" size="small" @click="writeComment">등록</el-button>
      </div>
    </div>
    <div class="comment-view">
      <el-table :data="comments">

        <el-input type="hidden" prop="id"/>
        <el-table-column prop="content" label="댓글"/>
        <el-table-column prop="nickname" label="작성자"/>
        <el-table-column prop="createTime" label="작성일"/>
        <el-table-column label="답글">
          <template v-slot:default="table">
            <el-button type="plain" :icon="Message" :id="table.row.id" link @click="add"></el-button>
          </template>
        </el-table-column>
        <el-table-column
            inline-template
            label="-">
          <template v-slot:default="table">
            <el-button type="info" :icon="Delete" link @click="deleteComment(table.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup lang="ts">
import {
  Delete,
  Message,
} from '@element-plus/icons-vue'

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
  memberId: 0,
});

const comment = ref({
  content: "",
  postId: props.postId
})


const comments = ref([]);
const reply = ref("");

const flag = ref(false);
const router = useRouter();
const auth = useAuthStore();
const {getAccessToken} = storeToRefs(auth)
const {getId} = storeToRefs(auth)
const configs = {
  headers: {
    Authorization: getAccessToken.value
  }
}

const getComments = function () {
  axios.get(`/api/comments`, configs).then((response) => {
    comments.value.length = 0;
    response.data.forEach((r: any) => {
      comments.value.push(r);
    })
  })
};

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
    console.log(getId.value)
    console.log(post.value)
    if (getId.value === post.value.memberId) {
      flag.value = true
    }
  }).catch(e => {
    auth.clear();
    router.replace({name: 'home'})
  })
  getComments();
})

const writeComment = function () {
  axios.post('/api/comments/create', comment.value, configs)
      .then(r => {
        comment.value.content = "";
        getComments();
      }).catch(e => {
    alert(e.response.data.validation.content);
  })
};

const deleteComment = function (e: any) {
  axios.delete(`/api/comments/${e}`, configs)
      .then(r => {
        getComments();
      }).catch(e => {
    alert(e.response.data)
  })
};

const add = function (event: any) {
  const replyTextArea = document.createElement('el-element');
  // const click = document.getElementById(e.get.id);
  alert(event.id)
};
</script>