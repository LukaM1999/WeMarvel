<template>
  <div class="row">
    <div class="col">
      <ejs-grid ref="grid" :dataSource='characters'
                :allowPaging='true'
                :pageSettings="pageSettings"
                :toolbar="toolbar" height='273px'
                :allowFiltering='true'
                :filterSettings="filterSettings"
                :allowSorting="true"
                :allowResizing="true"
                :allowTextWrap="true">
        <e-columns>
          <e-column field='name' headerText='Character' textAlign='Center' width="120" :template="'nameTemplate'"></e-column>
          <e-column field='description' headerText='Description' textAlign="Center"></e-column>
        </e-columns>
        <template v-slot:nameTemplate="{data}">
          <div class="row">
            <div class="col">
              <img style="box-shadow: 0px 0px 10px 1px black"
                   :src="data.thumbnail"
                   :alt="data.name" :title="data.name"/>
            </div>
            <div class="col">
              <b style="font-size: 14px"><a class="custom-link" :href="`/character/${data.id}`">{{data.name}}</a></b>
            </div>
          </div>
        </template>
      </ejs-grid>
    </div>
  </div>
</template>

<script>
import {
  ColumnDirective,
  ColumnsDirective,
  Filter,
  GridComponent, Page, Resize,
  Search,
  Sort,
  Toolbar
} from "@syncfusion/ej2-vue-grids";
import {capitalize} from "eslint-plugin-vue/lib/utils/casing";
import axios from "axios";

export default {
  name: "MarvelCharacters",
  components: {
    'ejs-grid' : GridComponent,
    'e-columns' : ColumnsDirective,
    'e-column' : ColumnDirective,
  },
  props: {
    charactersProp: {
      type: Array,
    }
  },
  data() {
    return {
      characters: [],
      toolbar: ['Search'],
      filterSettings: {type: 'Menu'},
      pageSettings: {
        pageCount: 5,
        pageSize: 20,
        pageSizes: [10, 20, 50, 100]
      },
      capitalize,
    }
  },
  async mounted() {
    this.characters = this.charactersProp;
    if(!this.characters) {
      await this.getCharacters();
    }
  },
  methods: {
    async getCharacters() {
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/character?offset=0&limit=1000000`);
      for(let c of data){
        const lastDot = c.thumbnail.lastIndexOf(".");
        c.thumbnail = c.thumbnail.substring(0, lastDot) + "/portrait_small" + c.thumbnail.substring(lastDot);
      }
      this.characters = data;
    },
  },
  provide: {
    grid: [Sort, Toolbar, Search, Filter, Resize, Page]
  },
}
</script>

<style scoped>

</style>