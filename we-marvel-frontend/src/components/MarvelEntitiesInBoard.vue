<template>
  <div id="entitiesInBoardContainer">
    <div class="row">
      <div class="col">
        <ejs-grid ref="grid" :dataSource='entities'
                  :allowPaging='true'
                  :pageSettings="pageSettings"
                  :toolbar="toolbar" height='273px'
                  :allowFiltering='true'
                  :filterSettings="filterSettings"
                  :allowSorting="true"
                  :allowResizing="true"
                  :editSettings="editSettings"
                  :allowGrouping="type.name === 'comic'"
                  :groupSettings="type.name === 'comic' ? groupSettings : undefined">
          <e-columns>
            <e-column field='seriesTitle' :headerText="'Series'" textAlign='Center' width="70"></e-column>
            <e-column :allowGrouping="false" field='title' :headerText='capitalize(type.name)' textAlign='Center' width="120" :template="'titleTemplate'"></e-column>
            <e-column :allowGrouping="false" field='topicCount'  headerText='Topics' textAlign="Center" width=50></e-column>
            <e-column :allowGrouping="false" field='postCount'  headerText='Posts' textAlign="Center" width=50></e-column>
            <e-column :allowGrouping="false" field='lastPostDate' textAlign="Center" headerText='Last posted' width="70"></e-column>
          </e-columns>
          <template v-slot:titleTemplate="{data}">
            <div class="row">
              <div class="col">
                <img style="box-shadow: 0px 0px 10px 1px black"
                     :src="data.thumbnail"
                     :alt="data.title" :title="data.title"/>
              </div>
              <div class="col">
                <b style="font-size: 14px"><a class="custom-link" :href="`./${type.name}/${data.id}`"
                                              @click.prevent="openEntity(data.id)">{{data.title}}</a></b>
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
  ColumnsDirective, Edit,
  Filter,
  GridComponent, Group, Page, Resize,
  Search,
  Sort,
  Toolbar
} from "@syncfusion/ej2-vue-grids";
import axios from "axios";
import {router} from "@/main";
import {capitalize} from "eslint-plugin-vue/lib/utils/casing";

export default {
  name: "MarvelEntitiesInBoard",
  components: {
    'ejs-grid' : GridComponent,
    'e-columns' : ColumnsDirective,
    'e-column' : ColumnDirective,
  },
  props: {
    type: {
      type: Object,
      required: true,
    }
  },
  data(){
    return {
      entities: [],
      toolbar: ['Search'],
      editSettings: {
        allowAdding: true,
        mode: 'Dialog',
      },
      groupSettings: {
        columns: ['seriesTitle'],
        disablePageWiseAggregates: true,
      },
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
    await this.getEntities();
  },
  methods: {
    async getEntities(){
      if(this.type.name !== 'comic') {
        this.$refs.grid.ej2Instances.columns = this.$refs.grid.ej2Instances.columns.filter(c => c.field !== 'seriesTitle');
      }
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/${this.type.name}/withPostInfo`);
      this.entities = data;
      for (let e of this.entities){
        if(this.type.name === 'character'){
          e.title = e.name;
        }
        if(!e.thumbnail) continue;
        const lastDot = e.thumbnail.lastIndexOf(".");
        e.thumbnail = e.thumbnail.substring(0, lastDot) + "/portrait_small" + e.thumbnail.substring(lastDot);
      }
    },
    openEntity(id){
      router.push(`./${this.type.name}/${id}`);
    },
  },
  provide: {
    grid: [Sort, Toolbar, Search, Filter, Resize, Page, Edit, Group]
  },
}
</script>

<style>
#entitiesInBoardContainer .e-grid .e-icon-grightarrow::before {
  color: black !important;
}

#entitiesInBoardContainer .e-grid .e-icon-gdownarrow::before {
  color: black !important;
}
</style>