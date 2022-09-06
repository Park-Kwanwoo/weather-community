import {defineStore} from "pinia";


export const useAuthStore = defineStore({
    id: 'auth',
    state: () => ({
        isAuth: false,
        user: null,
        role: ""
    }),
    getters: {
        getIsAuth(state) {
            return state.isAuth;
        },
        getUser(state) {
            return state.user;
        },
        getRole(state) {
          return state.role;
        }
    },
    actions: {
        setUser(user: any) {
            this.user = user;
        },
        setAuth(boolean: boolean) {
            this.isAuth = boolean;
        },
        setRole(role: string){
            this.role = role;
        }
    }
})