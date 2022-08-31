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
import Pusher from "pusher-js";
import Users from "@/components/Users";
import CharacterBoard from "@/components/MarvelEntityBoard";
import ComicBoard from "@/components/ComicBoard";
import BoardTopics from "@/components/BoardTopics";
import MarvelEntityBoard from "@/components/MarvelEntityBoard";

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
                path: '/forum/board/:boardId([123])',
                name: 'entity-board',
                component: MarvelEntityBoard,
            },
            {
                path: '/forum/board/:boardId([123])/topic',
                redirect: to => {
                    return { name: 'entity-board', params: to.params }
                }
            },
            {
                path: '/forum/board/:boardId([123])/:entity(character|comic|series)',
                component: MarvelEntityBoard,
            },
            {
                path: '/forum/board/:boardId/:entity(character|comic|series)/:entityId',
                name: 'entity-topics',
                component: BoardTopics,
            },
            {
                path: '/forum/board/:boardId/:entity(character|comic|series)/:entityId/topic',
                redirect: to => {
                    return {
                        name: 'entity-topics',
                        params: {
                            boardId: to.params.boardId,
                            entity: to.params.entity,
                            entityId: to.params.entityId
                        }
                    }
                }
            },
            {
                path: '/forum/board/:id/topic',
                redirect: to => {
                    return {name: 'board', params: {id: to.params.id}}
                }
            },
            {
                path: '/forum/board/:boardId/:entity(character|comic|series)/:entityId/topic/:id',
                name: 'entity-topic',
                component: Topic,
            },
            {
                path: '/forum/board/:boardId([^123])',
                name: 'board',
                component: BoardTopics,
            },
            {
                path: '/forum/board/:boardId([^123])/topic/:id',
                name: 'topic',
                component: Topic,
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
