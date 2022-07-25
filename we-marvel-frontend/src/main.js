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

registerLicense(process.env.VUE_APP_SYNCFUSION_KEY)

const routes = [
    {
        path: '/',
        component: LandingPage,
        children:[
            {
                path: '/welcome',
                component: Characters
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
    }
}

export const characterTemplate = app.component('character', {
    template: `
    <div class="e-list-wrapper">
        <div>
          <div class="e-card-header">
            <div class="e-card-header-text">
              <div class="e-card-header-title">
                <h3>{{data.name}}</h3>
              </div>
                <div class="e-card-header-subtitle">
                  <p>Modified on {{date(data.modified)}}</p>
                </div>
            </div>
          </div>
          <div class="e-card-content">
            <div class="grid-container">
                <div class="grid-item">
                  <img :src="data.thumbnail" :alt="data.name">
                </div>
                <div v-if="data.description" class="grid-item">
                    <div class="e-card-content-text">
                      <p class="text-left">{{data.description}}</p>
                    </div>
                </div>
            </div>
          </div>
        </div>
    </div>
  `,
    data: function() {
        return {
            data: {}
        }
    },
    methods: {
        date(value){
            return new Date(value[0], value[1], value[2]).toLocaleDateString()
        }
    }
});
