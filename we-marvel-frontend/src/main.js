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
import Board from "@/components/Board";
import Pusher from "pusher-js";
import Users from "@/components/Users";
import CharacterBoard from "@/components/CharacterBoard";
import ComicBoard from "@/components/ComicBoard";

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
                path: '/forum/board',
                redirect: 'forum'
            },
            {
                path: '/forum/board/1',
                name: 'character-board',
                component: CharacterBoard,
            },
            {
                path: '/forum/board/1/character',
                component: CharacterBoard,
            },
            {
                path: '/forum/board/1/character/:characterId',
                name: 'character-topics',
                component: Board,
                alias: '/forum/board/1/character/:characterId/topic'
            },
            {
                path: '/forum/board/2',
                name: 'comic-board',
                component: ComicBoard,
            },
            {
                path: '/forum/board/2/comic',
                component: ComicBoard,
            },
            {
                path: '/forum/board/2/comic/:comicId',
                component: Board,
                name: 'comic-topics',
                alias: '/forum/board/2/comic/:comicId/topic'
            },
            {
                path: '/forum/board/3',
                name: 'comic-board',
                component: ComicBoard,
            },
            {
                path: '/forum/board/3/series',
                component: ComicBoard,
            },
            {
                path: '/forum/board/3/series/:seriesId',
                component: Board,
                name: 'series-topics',
                alias: '/forum/board/3/series/:seriesId/topic'
            },
            {
                path: '/forum/board/:id/topic',
                redirect: to => {
                    return {name: 'board', params: {id: to.params.id}}
                }
            },
            {
                path: '/forum/board/:boardId/topic/:id',
                name: 'topic',
                component: Topic,
            },
            {
                path: '/forum/board/1/character/:characterId/topic/:id',
                name: 'character-topic',
                component: Topic,
            },
            {
                path: '/forum/board/1/comic/:comicId/topic/:id',
                name: 'comic-topic',
                component: Topic,
            },
            {
                path: '/forum/board/:id',
                name: 'board',
                component: Board
            },
            {
                path: '/profile/:username',
                name: 'profile',
                component: Profile
            },
            {
                path: '/users',
                name: 'users',
                component: Users
            },
        ]
    },
]

export const router = createRouter({
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
        socketId: null,
        breadcrumbs: {},
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
        },
        setSocketId(state, socketId) {
            state.socketId = socketId
        },
        setBreadcrumb(state, breadcrumb) {
            state.breadcrumbs[breadcrumb.id] = breadcrumb.name;
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
        },
        socketId(state) {
            return state.socketId
        },
        breadcrumb: (state) => (id) => {
            return state.breadcrumbs[id]
        }
    }
})

jwtInterceptor()

export const pusher = new Pusher('b0aaeba2e43d3ebdf234', {
    cluster: 'eu'
});
Pusher.logToConsole = true;
pusher.connection.bind("connected", () => {
    store.commit('setSocketId', pusher.connection.socket_id);
});

const app = createApp(App)
app.use(VueAxios, axios)
app.use(router)
app.use(store)
app.mount('#app')


app.config.globalProperties.$filters = {
    date(value) {
        return new Date(value[0], value[1], value[2]).toLocaleDateString()
    },
    capitalize(value){
        if(!value) return;
        return value.charAt(0).toUpperCase() + value.slice(1)
    }
}
