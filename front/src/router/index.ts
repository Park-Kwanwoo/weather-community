import { createRouter, createWebHistory } from 'vue-router'
import PostView from '../views/post/PostView.vue'
import WriteView from '../views/post/WriteView.vue'
import ReadView from '../views/post/ReadView.vue'
import EditView from '../views/post/EditView.vue'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/user/LoginView.vue'
import JoinView from '../views/user/JoinView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    {
      path: '/posts',
      name: 'posts',
      component: PostView
    },
    {
      path: '/write',
      name: 'write',
      component: WriteView
    },
    {
      path: '/read/:postId',
      name: "read",
      component: ReadView,
      props: true,
    },
    {
      path: "/edit/:postId",
      name: "edit",
      component: EditView,
      props: true,
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      props: true
    },
    {
      path: '/join',
      name: 'join',
      component: JoinView,
      props: true
    }
  ]
})

export default router
