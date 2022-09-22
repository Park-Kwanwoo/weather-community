import {defineStore} from "pinia";


export const useAuthStore = defineStore('auth', {
    state: () => ({
        isAuth: false,
        accessToken: '',
        email: ''
    }),
    getters: {
        getIsAuth(state) {
            return state.isAuth;
        },
        getAccessToken(state) {
            return state.accessToken;
        },
        getEmail(state) {
            return state.email;
        }
    },
    actions: {
        setAccessToken(accessToken: any) {
            this.accessToken = accessToken;
        },
        setAuth(boolean: boolean) {
            this.isAuth = boolean;
        },
        setEmail(email: any) {
            this.email = email;
        },
        clear() {
            this.isAuth = false;
            this.accessToken = '';
            this.email = '';
        }
    },
    persist: true
})