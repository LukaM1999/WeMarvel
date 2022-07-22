<template>
  <div>
    <ejs-button class="e-control e-lib e-primary">Click me</ejs-button>
    <ejs-listview v-if="characters.length > 0" :cssClass="'e-list-template'" :dataSource="characters"
                  :query="query" :template="getTemplate" :fields="fields"
                  :headerTemplate="'headerTemplate'" :showHeader="true">
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
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import {ListViewComponent} from "@syncfusion/ej2-vue-lists";
import {PagerComponent} from "@syncfusion/ej2-vue-grids";
import { Query } from '@syncfusion/ej2-data';
import {DropDownListComponent} from "@syncfusion/ej2-vue-dropdowns";
import axios from "axios";
import {characterTemplate} from "@/main";

export default {
  name: 'HelloWorld',
  props: {
    msg: String
  },
  data() {
    return {
      characters: [],
      totalCharacters: 0,
      pageSize: 10,
      query: new Query().range(0, 10),
      fields: { text: 'name', id: 'id' },
      characterTemplate: null,
      search: '',
      sort: 'name',
    };
  },
  components: {
    'ejs-button': ButtonComponent,
    'ejs-listview': ListViewComponent,
    'ejs-pager': PagerComponent,
    'ejs-dropdownlist': DropDownListComponent
  },
  mounted() {
    this.getCharacters(null);
    this.getCharacterCount();
  },
  methods:{
    getTemplate(){
      return {
        template: characterTemplate,
      }
    },
    async getCharacters(e){
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
        get(`${process.env.VUE_APP_BACKEND}/characters?limit=${limit}&offset=${offset}&sortBy=${this.sort}
        ${this.search ? '&name=' + this.search : ''}`);
      const characters = response.data
      for(let c of characters){
        const lastDot = c.thumbnail.lastIndexOf(".");
        c.thumbnail = c.thumbnail.substring(0, lastDot) + "/portrait_incredible" + c.thumbnail.substring(lastDot);
      }
      this.characters = characters;
      await this.getCharacterCount();
    },
    async getCharacterCount(){
      const response = await axios.get(`${process.env.VUE_APP_BACKEND}/characters/count${this.search ? '?name=' + this.search : ''}`);
      this.totalCharacters = response.data;
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
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
