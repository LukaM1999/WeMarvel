<template>
  <div class="row">
    <div class="col">
      <ejs-grid ref="grid" :dataSource='series'
                :allowPaging='true'
                :pageSettings="pageSettings"
                :toolbar="toolbar" height='273px'
                :allowFiltering='true'
                :filterSettings="filterSettings"
                :allowSorting="true"
                :allowResizing="true"
                :allowTextWrap="true">
        <e-columns>
          <e-column field='title' headerText='Series' textAlign='Center' width="120" :template="'titleTemplate'"></e-column>
          <e-column field='description' headerText='Description' textAlign="Center" width=100></e-column>
          <e-column field='type' headerText='Type' textAlign="Center" width=50></e-column>
          <e-column field='startYear' headerText='Start year' textAlign="Center" width=50></e-column>
          <e-column field='endYear' headerText='End year' textAlign="Center" width=50></e-column>
        </e-columns>
        <template v-slot:titleTemplate="{data}">
          <div class="row">
            <div class="col">
              <img style="box-shadow: 0px 0px 10px 1px black"
                   :src="data.thumbnail"
                   :alt="data.title" :title="data.title"/>
            </div>
            <div class="col">
              <b style="font-size: 14px"><a class="custom-link" :href="`/series/${data.id}`">{{data.title}}</a></b>
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
  GridComponent, Group, Page, Resize,
  Search,
  Sort,
  Toolbar
} from "@syncfusion/ej2-vue-grids";
import {capitalize} from "eslint-plugin-vue/lib/utils/casing";
import axios from "axios";

export default {
  name: "Series",
  components: {
    'ejs-grid' : GridComponent,
    'e-columns' : ColumnsDirective,
    'e-column' : ColumnDirective,
  },
  props: {
    seriesProp: {
      type: Array,
    }
  },
  data() {
    return {
      series: [],
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
    console.log(this.seriesProp);
    this.series = this.seriesProp;
    if(!this.series) {
      await this.getSeries();
    }
  },
  methods: {
    async getSeries() {
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/series`);
      for(let s of data){
        const lastDot = s.thumbnail.lastIndexOf(".");
        s.thumbnail = s.thumbnail.substring(0, lastDot) + "/portrait_small" + s.thumbnail.substring(lastDot);
      }
      this.series = data;
    },
  },
  provide: {
    grid: [Sort, Toolbar, Search, Filter, Resize, Page, Group]
  },
}
</script>

<style scoped>

</style>