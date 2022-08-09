import { createApp } from 'vue/dist/vue.esm-bundler'
import App from './App.vue'
import { registerLicense } from '@syncfusion/ej2-base'
import axios from 'axios'
import VueAxios from 'vue-axios'
import {createRouter, createWebHistory} from 'vue-router'
import { createStore } from 'vuex'
import createPersistedState from 'vuex-persistedstate'
import {jwtInterceptor} from "@/_helpers/jwtInterceptor";
import LandingPage from "@/components/LandingPage";
import Characters from "@/components/Characters";
import TopRatedCharacters from "@/components/TopRatedCharacters";
import ForumOverview from "@/components/ForumOverview";
import Topic from "@/components/Topic";
import Profile from "@/components/Profile";

registerLicense(process.env.VUE_APP_SYNCFUSION_KEY)

const routes = [
    {
        path: '/',
        component: LandingPage,
        redirect: '/welcome',
        children:[
            {
                path: '/welcome',
                name: 'welcome',
                component: Characters
            },
            {
                path: '/characters/top-rated',
                name: 'top-rated-characters',
                component: TopRatedCharacters
            },
            {
                path: '/forum',
                name: 'forum',
                component: ForumOverview
            },
            {
                path: '/forum/topic/:id',
                name: 'topic',
                component: Topic
            },
            {
                path: '/profile/:username',
                name: 'profile',
                component: Profile
            }
        ]
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export const store = createStore({
    plugins: [createPersistedState()],
    state: {
        user: null,
        token: null,
        firebaseToken: null,
        scrollPosition: 0,
    },
    mutations: {
        setToken(state, token) {
            state.token = token
        },
        setUser(state, user) {
            state.user = user
        },
        setFirebaseToken(state, token) {
            state.firebaseToken = token
        },
        setScrollPosition(state, position) {
            state.scrollPosition = position
        }
    },
    getters: {
        token(state) {
            return state.token
        },
        user(state) {
            return state.user
        },
        firebaseToken(state) {
            return state.firebaseToken
        },
        scrollPosition(state) {
            return state.scrollPosition
        }
    }
})

jwtInterceptor()

const app = createApp(App)
app.use(VueAxios, axios)
app.use(router)
app.use(store)
app.mount('#app')

app.config.globalProperties.$filters = {
    date(value) {
        return new Date(value[0], value[1], value[2]).toLocaleDateString()
    },
    dateTime(value) {
        return new Date(value[0], value[1], value[2], value[3], value[4]).toLocaleString()
    }
}
