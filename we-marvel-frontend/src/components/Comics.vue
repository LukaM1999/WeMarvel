<template>
  <div id="comicsContainer" style="overflow-x: hidden;">
    <div class="row">
      <div class="col">
        <ejs-grid ref="grid" :dataSource='comics'
                  :allowPaging='true'
                  :pageSettings="pageSettings"
                  :toolbar="toolbar" height='350px'
                  :allowFiltering='true'
                  :filterSettings="filterSettings"
                  :allowSorting="true"
                  :allowResizing="true"
                  :allowGrouping="true"
                  :groupSettings="groupSettings"
                  :allowTextWrap="true">
          <e-columns>
            <e-column field='seriesTitle' :headerText="'Series'" textAlign='Center' width="70"></e-column>
            <e-column :allowGrouping="false" field='title' headerText='Comic' textAlign='Center' width="120" :template="'titleTemplate'"></e-column>
            <e-column :allowGrouping="false" field='description' headerText='Description' textAlign="Center" width=100></e-column>
            <e-column :allowGrouping="false" field='pageCount' headerText='Pages' textAlign="Center" width=50></e-column>
            <e-column :allowGrouping="false" field='format' headerText='Format' textAlign="Center" width=50></e-column>
            <e-column :allowGrouping="false" field='issueNumber' headerText='Issue' textAlign="Center" width=50></e-column>
          </e-columns>
          <template v-slot:titleTemplate="{data}">
            <div class="row">
              <div class="col">
                <img style="box-shadow: 0px 0px 10px 1px black"
                     :src="data.thumbnail"
                     :alt="data.title" :title="data.title"/>
              </div>
              <div class="col">
                <b style="font-size: 14px"><a class="custom-link" :href="`/comic/${data.id}`">{{data.title}}</a></b>
              </div>
            </div>
          </template>
        </ejs-grid>
      </div>
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
  name: "Comics",
  components: {
    'ejs-grid' : GridComponent,
    'e-columns' : ColumnsDirective,
    'e-column' : ColumnDirective,
  },
  props: {
    comicsProp: {
      type: Array,
    }
  },
  data() {
    return {
      comics: [],
      toolbar: ['Search'],
      filterSettings: {type: 'Menu'},
      groupSettings: {disablePageWiseAggregates: true},
      pageSettings: {
        pageCount: 5,
        pageSize: 20,
        pageSizes: [10, 20, 50, 100]
      },
      capitalize,
    }
  },
  async mounted() {
    console.log(this.comicsProp);
    this.comics = this.comicsProp;
    console.log(this.comics);
    if(!this.comics) {
      await this.getComics();
    }
  },
  methods: {
    async getComics() {
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/comic/withSeries`);
      for(let c of data){
        const lastDot = c.thumbnail.lastIndexOf(".");
        c.thumbnail = c.thumbnail.substring(0, lastDot) + "/portrait_small" + c.thumbnail.substring(lastDot);
      }
      this.comics = data;
    },
  },
  provide: {
    grid: [Sort, Toolbar, Search, Filter, Resize, Page, Group]
  },
}
</script>

<style>
#comicsContainer .e-grid .e-icon-grightarrow::before {
  color: black !important;
}

#comicsContainer .e-grid .e-icon-gdownarrow::before {
  color: black !important;
}
</style>