<template>
  <ejs-grid ref="grid" :dataSource='characters'
            :allowPaging='true'
            :pageSettings="pageSettings"
            :toolbar="toolbar" height='273px'
            :allowFiltering='true'
            :filterSettings="filterSettings"
            :allowSorting="true"
            :allowResizing="true"
            :editSettings="editSettings">
    <e-columns>
      <e-column field='name' headerText='Character' textAlign='Center' width="150" :template="'nameTemplate'"></e-column>
      <e-column field='topicCount'  headerText='Topics' textAlign="Center" width=50></e-column>
      <e-column field='postCount'  headerText='Posts' textAlign="Center" width=50></e-column>
      <e-column field='lastPostDate' textAlign="Center" headerText='Last posted' width="100"></e-column>
    </e-columns>
    <template v-slot:nameTemplate="{data}">
      <div class="row">
        <div class="col">
          <img style="box-shadow: 0px 0px 10px 1px black"
               :src="data.thumbnail"
               :alt="data.name" :title="data.name"/>
        </div>
        <div class="col">
          <h2><a class="custom-link" :href="`./character/${data.id}`"
                 @click.prevent="openCharacter(data.id)">{{data.name}}</a></h2>
        </div>
      </div>
    </template>
  </ejs-grid>
</template>

<script>
import {
  ColumnDirective,
  ColumnsDirective, Edit,
  Filter,
  GridComponent, Page, Resize,
  Search,
  Sort,
  Toolbar
} from "@syncfusion/ej2-vue-grids";
import axios from "axios";
import {router} from "@/main";

export default {
  name: "CharactersInBoard",
  components: {
    'ejs-grid' : GridComponent,
    'e-columns' : ColumnsDirective,
    'e-column' : ColumnDirective,
  },
  data(){
    return {
      characters: [],
      toolbar: ['Search', 'Add', 'Edit', 'Delete'],
      editSettings: {
        allowAdding: true,
        allowDeleting: true,
        allowEditing: true,
        mode: 'Dialog',
      },
      filterSettings: {type: 'Menu'},
      pageSettings: {
        pageCount: 5,
        pageSize: 20,
        pageSizes: [10, 20, 50, 100]
      },
    }
  },
  async mounted() {
    await this.getCharacters();
  },
  methods: {
    async getCharacters(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/character/withPostInfo`);
      this.characters = data;
      for (let c of this.characters){
        if(!c.thumbnail) continue;
        const lastDot = c.thumbnail.lastIndexOf(".");
        c.thumbnail = c.thumbnail.substring(0, lastDot) + "/portrait_small" + c.thumbnail.substring(lastDot);
      }
    },
    openCharacter(characterId){
      router.push({name: 'character-topics', params: {characterId: characterId}});
    },
  },
  provide: {
    grid: [Sort, Toolbar, Search, Filter, Resize, Page, Edit]
  },
}
</script>

<style scoped>

</style>