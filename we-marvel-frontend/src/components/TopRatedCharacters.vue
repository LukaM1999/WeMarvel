<template>
  <ejs-grid v-if="pageSettings.totalRecordsCount > 0" :key="tableKey" ref="grid" :dataSource="characters" :allowPaging="true" :pageSettings="pageSettings">
    <e-columns>
      <e-column field='name' headerText='Name' textAlign='Left'  width=100></e-column>
      <e-column field='averageRating' headerText='Average rating' textAlign="Left" width=120></e-column>
    </e-columns>
  </ejs-grid>
</template>

<script>
import {ColumnDirective, ColumnsDirective, GridComponent, Page} from "@syncfusion/ej2-vue-grids";
import axios from "axios";

export default {
  name: "TopRatedCharacters",
  components: {
    'ejs-grid' : GridComponent,
    'e-columns' : ColumnsDirective,
    'e-column' : ColumnDirective
  },
  data(){
    return {
      pageSettings: {pageSize: 50, pageCount: 5},
      characters: [],
      tableKey: 0,
    }
  },
  async mounted() {
    await this.getCharacters();
  },
  methods: {
    async getCharacters(){
      const response = await axios.
      get(`${process.env.VUE_APP_BACKEND}/characters/top-rated?limit=0&offset=100`);
      const characters = response.data
      for(let c of characters){
        const lastDot = c.thumbnail.lastIndexOf(".");
        c.thumbnail = c.thumbnail.substring(0, lastDot) + "/portrait_incredible" + c.thumbnail.substring(lastDot);
      }
      this.characters = characters;
    },
  },
  provide: {
    grid: [Page]
  }
}
</script>

<style scoped>

</style>