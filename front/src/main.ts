import { createApp } from 'vue'
import { createPinia } from 'pinia'
import createPersistedState from "pinia-plugin-persistedstate"

import App from './App.vue'
import router from './router'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import 'bootstrap/dist/css/bootstrap-utilities.css'

const app = createApp(App)

const pinia = createPinia()
pinia.use(createPersistedState)

app.use(pinia)
app.use(router)
app.use(ElementPlus)

app.mount('#app')
