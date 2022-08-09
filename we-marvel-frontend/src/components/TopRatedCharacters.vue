<template>
  <ejs-grid :key="tableKey" ref="grid" :dataSource="characters" :allowPaging="true"
            :pageSettings="pageSettings" :rowTemplate="'rowTemplate'">
    <e-columns>
      <e-column field="rank" headerText="Rank" width="40" textAlign="Center"></e-column>
      <e-column field='name' headerText='Name' textAlign='Center' width=100></e-column>
      <e-column field='averageRating' headerText='Average rating' textAlign="Center" width=40></e-column>
    </e-columns>
    <template v-slot:rowTemplate="{data}">
      <tr>
        <td class="rank">
          {{ data.rank }}
        </td>
        <td>
          <div class="row">
            <div class="col-2 d-flex align-self-center">
              <img :src="data.thumbnail" :alt="data.name">
            </div>
            <div class="col-10 mt-2 d-flex">
              <div class="row d-block">
                <div class="col d-flex justify-content-start">
                  <div class="name">
                    {{ data.name }}
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
          <div class="row d-inline-flex">
            <div class="col">
              <box-icon :name="'star'" type="solid" :color="'#ffb400'" :size="'sm'"></box-icon>
            </div>
            <div class="col align-self-center avg-rating">
              {{data.averageRating}} ({{data.ratingCount}})
            </div>
          </div>
        </td>
      </tr>
    </template>
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
      pageSettings: {pageSizes: [10, 20, 50, 100], pageSize: 10, pageCount: 5, currentPage: 1},
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
      get(`${process.env.VUE_APP_BACKEND}/characters/top-rated?limit=10000&offset=0`);
      const characters = response.data
      for(let i = 0; i < characters.length; i++){
        const c = characters[i]
        c.rank = i + 1
        const lastDot = c.thumbnail.lastIndexOf(".");
        c.thumbnail = c.thumbnail.substring(0, lastDot) + "/portrait_medium" + c.thumbnail.substring(lastDot);
      }
      this.characters = characters;
      console.log(this.characters);
    },
  },
  provide: {
    grid: [Page]
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