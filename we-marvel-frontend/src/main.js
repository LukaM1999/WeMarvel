import { createApp } from 'vue/dist/vue.esm-bundler'
import App from './App.vue'
import { registerLicense } from '@syncfusion/ej2-base'
import axios from 'axios'
import VueAxios from 'vue-axios'

registerLicense(process.env.VUE_APP_SYNCFUSION_KEY)

const app = createApp(App)
app.mount('#app')
app.use(VueAxios, axios)

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
