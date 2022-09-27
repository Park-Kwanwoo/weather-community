import {defineStore} from "pinia";


export const useAuthStore = defineStore('auth', {
    state: () => ({
        isAuth: false,
        accessToken: '',
        id: 0
    }),
    getters: {
        getIsAuth(state) {
            return state.isAuth;
        },
        getAccessToken(state) {
            return state.accessToken;
        },
        getId(state) {
            return state.id;
        }
    },
    actions: {
        setAccessToken(accessToken: any) {
            this.accessToken = accessToken;
        },
        setAuth(boolean: boolean) {
            this.isAuth = boolean;
        },
        setId(id: any) {
            this.id = id;
        },
        clear() {
            this.isAuth = false;
            this.accessToken = '';
            this.id = 0;
        }
    },
    persist: true
})