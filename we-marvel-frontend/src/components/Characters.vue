<template>
  <div>
    <ejs-listview v-if="characters.length > 0" :cssClass="'e-list-template'" :dataSource="characters"
                  :query="query" :template="'template'" :fields="fields"
                  :headerTemplate="'headerTemplate'" :showHeader="true">
      <template v-slot:template="{data}">
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
      </template>
      <template v-slot:headerTemplate="{}">
          <div class="grid">
            <div class="grid-item">
              <div class="e-list-header-text">
                <span class="e-list-header-text-content">Characters</span>
              </div>
            </div>
            <div class="grid-item">
              <input type="search" @keyup.enter="getCharacters(null)" @click.stop v-model="search" placeholder="Search..." class="e-input" />
            </div>
            <div class="grid-item">
              <label for="sort" class="e-label">Sort by</label>
            </div>
            <div class="grid-item" style="width: 50%; justify-self: left" @click.stop>
              <ejs-dropdownlist id="sort" v-model="sort" :value="sort" :itemTemplate="'itemTemplate'"
                                :valueTemplate="'valueTemplate'" :dataSource="['name', 'modified']">
                <template v-slot:itemTemplate="{data}">
                  <span>{{data === 'modified' ? 'Date modified' : 'Name'}}</span>
                </template>
                <template v-slot:valueTemplate="{data}">
                  {{data === 'modified' ? 'Date modified' : 'Name'}}
                </template>
              </ejs-dropdownlist>
            </div>
          </div>
      </template>

    </ejs-listview>

    <ejs-pager ref="pager" :totalRecordsCount="totalCharacters" :pageSize="pageSize"
               :pageCount="5" @click="getCharacters($event)"></ejs-pager>
  </div>
</template>

<script>
import {ListViewComponent} from "@syncfusion/ej2-vue-lists";
import {PagerComponent} from "@syncfusion/ej2-vue-grids";
import { Query } from '@syncfusion/ej2-data';
import {DropDownListComponent} from "@syncfusion/ej2-vue-dropdowns";
import axios from "axios";

export default {
  name: 'Characters',
  data() {
    return {
      characters: [],
      totalCharacters: 0,
      pageSize: 10,
      query: new Query().range(0, 10),
      fields: { text: 'name', id: 'id' },
      search: '',
      sort: 'name',
    };
  },
  components: {
    'ejs-listview': ListViewComponent,
    'ejs-pager': PagerComponent,
    'ejs-dropdownlist': DropDownListComponent
  },
  mounted() {
    this.getCharacters(null);
    this.getCharacterCount();
  },
  methods:{
    date(value){
      return new Date(value[0], value[1], value[2]).toLocaleDateString()
    },
    async getCharacters(e){
      console.log(this.sort)
      let limit = 10
      let offset = 0
      if(e){
        limit = this.pageSize
        offset = (e.currentPage - 1) * this.pageSize
      }
      if(e === null){
        this.$refs.pager.goToPage(1)
      }
      const response = await axios.
        get(`${process.env.VUE_APP_BACKEND}/character?limit=${limit}&offset=${offset}&sortBy=${this.sort}${this.search ? '&name=' + this.search : ''}`);
      const characters = response.data
      for(let c of characters){
        const lastDot = c.thumbnail.lastIndexOf(".");
        c.thumbnail = c.thumbnail.substring(0, lastDot) + "/portrait_incredible" + c.thumbnail.substring(lastDot);
      }
      this.characters = characters;
      await this.getCharacterCount();
    },
    async getCharacterCount(){
      const response = await axios.get(`${process.env.VUE_APP_BACKEND}/character/count${this.search ? '?name=' + this.search : ''}`);
      this.totalCharacters = response.data;
    }
  }
}
</script>

<style scoped>

h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}

.grid {
  display: grid;
  grid-template-columns: auto auto auto auto;
  grid-gap: 10px;
}
</style>
