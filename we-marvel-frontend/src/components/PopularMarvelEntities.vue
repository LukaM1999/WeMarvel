<template>
  <div class="row mt-3">
    <div class="col">
      <ejs-grid :key="tableKey" ref="grid" :dataSource="entities" :allowPaging="true"
                :allowResizing="true" :allowTextWrap="true"
                :allowFiltering="true" :filterSettings="filterSettings"
                :allowSorting="true" :toolbar="toolbar"
                :pageSettings="pageSettings" :rowTemplate="'rowTemplate'">
        <e-columns>
          <e-column field="rank" headerText="Rank" width="40" textAlign="Center"></e-column>
          <e-column field='title' :headerText="capitalize(type)" textAlign='Center' width=100></e-column>
          <e-column field='description' :visible="false"></e-column>
          <e-column :field="type === 'comic' ? 'readingCount' : 'ratingCount'"
                    :headerText="type === 'comic' ? 'Readers' : 'Raters'" textAlign="Center" width=40></e-column>
        </e-columns>
        <template v-slot:rowTemplate="{data}">
          <tr>
            <td class="rank">
              {{ data.rank }}
            </td>
            <td>
              <div class="row">
                <div class="col-2 d-flex align-self-center">
                  <a style="float: left;" :href="data.url" target="_blank"><img :src="data.thumbnail" :alt="data.title"></a>
                </div>
                <div class="col-10 mt-2 d-flex">
                  <div class="row d-block">
                    <div class="col d-flex justify-content-start">
                      <div class="name">
                        <a class="custom-link" :href="`/${type}/${data.id}`">{{ data.title }}</a>
                      </div>
                    </div>
                    <div class="col mt-2">
                      <div class="text-left">
                        {{ data.description }}
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </td>
            <td>
              <span class="avg-rating">{{data.ratingCount || data.readingCount || 0}}</span>
            </td>
          </tr>
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
  GridComponent,
  Page,
  Resize,
  Sort,
  Toolbar
} from "@syncfusion/ej2-vue-grids";
import axios from "axios";
import {capitalize} from "eslint-plugin-vue/lib/utils/casing";

export default {
  name: "PopularMarvelEntities",
  components: {
    'ejs-grid' : GridComponent,
    'e-columns' : ColumnsDirective,
    'e-column' : ColumnDirective
  },
  data(){
    return {
      pageSettings: {pageSizes: [10, 20, 50, 100], pageSize: 20, pageCount: 5},
      entities: undefined,
      tableKey: 0,
      toolbar: ['Search'],
      filterSettings: {type: 'Menu'},
      type: '',
      capitalize,
    }
  },
  async mounted() {
    this.type = this.$route.params.entity;
    await this.getCharacters();
  },
  methods: {
    async getCharacters(){
      const response = await axios.get(`${process.env.VUE_APP_BACKEND}/${this.type}/popular`);
      const entities = response.data
      for(let i = 0; i < entities.length; i++){
        const e = entities[i]
        if(e.name) e.title = e.name;
        e.rank = i + 1
        const lastDot = e.thumbnail.lastIndexOf(".");
        e.thumbnail = e.thumbnail.substring(0, lastDot) + "/portrait_medium" + e.thumbnail.substring(lastDot);
      }
      this.entities = entities;
    },
  },
  provide: {
    grid: [Page, Resize, Filter, Sort, Toolbar]
  }
}
</script>

<style scoped>
.rank {
  font-weight: bold;
  font-size: 40px;
  color: gray;
}

.avg-rating {
  font-size: 20px;
}

.name {
  font-weight: bold;
  font-size: 18px;
}

.text-left {
  text-align: left;
}

img {
  box-shadow: 0 0 4px 0 gray;
}
</style>