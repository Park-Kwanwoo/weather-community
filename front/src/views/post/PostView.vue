<template>
  <div class="container">
    <div class="post">
      <div class="title">
        <h1>제목</h1>
        <el-input v-model="post.title" readonly></el-input>
      </div>
      <div class="content">
        <h1>내용</h1>
        <el-input type="textarea" v-model="post.content" resize="none" rows="25" readonly></el-input>
      </div>
      <div class="pt-4 float-end">
        <el-button type="primary" v-if="postFlag" @click="editPost">수정</el-button>
        <el-button type="warning" v-if="postFlag" @click="removePost">삭제</el-button>
      </div>
    </div>
    <div>
      <br>
      <el-button type="info" link><router-link :to="{name: 'posts'}">목록</router-link></el-button>
    </div>

    <div class="comments mt-5">
      <h2>댓글 작성</h2>
      <div class="comment">
        <div class="comment-write">
          <el-input type="textarea" resize="none" v-model="comment.content" rows="3"></el-input>
        </div>
        <div class="comment-request mt-2">
          <el-button class="float-end" type="info" size="small" @click="writeComment">등록</el-button>
        </div>
      </div>

      <h2>댓글</h2>
      <div class="comment-view">
        <div v-for="comment in comments" :key="comment.id" :id="comment.id">
          <el-row>
            <el-col :span="4"> {{ comment.nickname }}</el-col>
            <el-col :span="16"> {{ comment.content }}</el-col>
            <el-col :span="3"> {{ comment.createTime }}</el-col>
            <el-button :span="1" type="info" link :icon="Delete"
                       @click="deleteComment(comment.id, comment.memberId)"></el-button>
            <el-divider/>
          </el-row>
        </div>
        <div id="app2">
        </div>
      </div>
    </div>

  </div>
</template>

<script setup lang="ts">
import {Delete} from '@element-plus/icons-vue'

import {defineProps, h, onMounted, ref} from "vue";
import axios from "axios";
import {useRouter} from "vue-router";
import {useAuthStore} from "@/stores/auth";
import {storeToRefs} from "pinia";
import {ElMessage} from 'element-plus'

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

const postFlag = ref(false);
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
  axios.get(`/api/comments?postId=${props.postId}`).then((response) => {
    comments.value.length = 0;
    response.data.forEach((r: any) => {
      comments.value.push(r);
    })
  })
};

const editPost = () => {
  router.push({name: "edit", params: {postId: props.postId}});
}

const removePost = () => {
  axios.delete(`/api/posts/${props.postId}`, configs)
      .then((r) => {
        router.replace({name: 'posts'})
      })
      .catch((e) => {
        ElMessage(e.response.data);
      });
}

onMounted(() => {
  axios.get(`/api/posts/${props.postId}`).then((response) => {
    post.value = response.data;
    if (getId.value === post.value.memberId) {
      postFlag.value = true
    }
  }).catch(e => {
    auth.clear();
    router.replace({name: 'home'})
  })
  getComments();
})

const writeComment = function () {

  if (auth.getIsAuth) {
    axios.post('/api/comments/create', comment.value, configs)
        .then(r => {
          comment.value.content = "";
          getComments();
        }).catch(e => {
      ElMessage(e.response.data.validation.content);
    })
  } else {
    router.push({name: 'login'})
  }
};

const deleteComment = function (commentId: Number, memberId: Number) {
  if (auth.getId === memberId) {
    axios.delete(`/api/comments/${commentId}`, configs)
        .then(r => {
          getComments();
        }).catch(e => {
      ElMessage(e.response.data)
    });
  } else {
    ElMessage('삭제 권한이 없습니다!')
  }

};
</script>
<style lang="scss" scoped>

.el-row {
  margin-bottom: 20px;
}

.el-row:last-child {
  margin-bottom: 0;
}

.el-col {
  border-radius: 4px;
}

.grid-content {
  border-radius: 4px;
  min-height: 36px;
}
</style>