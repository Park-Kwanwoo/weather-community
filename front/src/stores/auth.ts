import {defineStore} from "pinia";


export const useAuthStore = defineStore('auth', {
    state: () => ({
        isAuth: false,
        accessToken: ''
    }),
    getters: {
        getIsAuth(state) {
            return state.isAuth;
        },
        getAccessToken(state) {
            return state.accessToken;
        }
    },
    actions: {
        setAccessToken(accessToken: any) {
            this.accessToken = accessToken;
        },
        setAuth(boolean: boolean) {
            this.isAuth = boolean;
        },
        clear() {
            this.isAuth = false;
            this.accessToken = '';
        }
    },
    persist: true
})