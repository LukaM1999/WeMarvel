<template>
<div style="overflow-x: hidden;">
  <h1>{{board?.title}}</h1>
  <div class="row mb-2 justify-content-center">
    <div class="col-8">
      <p>{{board?.description}}</p>
    </div>
  </div>
  <ejs-tab v-if="type.id" ref="tabs" :selected="tabSelected" class="e-fill">
    <e-tabitems>
      <e-tabitem :header="{text: 'Recent'}" :content="'recentTemplate'">
        <template v-slot:recentTemplate="{}">
          <RecentMarvelEntityTopics :type="type" :topics-prop="topics"/>
        </template>
      </e-tabitem>
      <e-tabitem :header="{text: capitalize(type.name)}" :content="'entityTemplate'">
        <template v-slot:entityTemplate="{}">
          <MarvelEntitiesInBoard :type="type"/>
        </template>
      </e-tabitem>
    </e-tabitems>
  </ejs-tab>
</div>
</template>

<script>
import {
  ColumnDirective,
  ColumnsDirective,
  Filter, FilterSettings,
  GridComponent, Resize,
  Search,
  Sort,
  Toolbar
} from "@syncfusion/ej2-vue-grids";
import axios from "axios";
import {TabComponent, TabItemDirective, TabItemsDirective} from "@syncfusion/ej2-vue-navigations";
import RecentMarvelEntityTopics from "@/components/RecentMarvelEntityTopics";
import {capitalize} from "eslint-plugin-vue/lib/utils/casing";
import MarvelEntitiesInBoard from "@/components/MarvelEntitiesInBoard";

export default {
  name: "MarvelEntityBoard",
  components: {
    RecentMarvelEntityTopics,
    MarvelEntitiesInBoard,
    "ejs-tab": TabComponent,
    "e-tabitems": TabItemsDirective,
    "e-tabitem": TabItemDirective,
  },
  data() {
    return {
      board: {},
      topics: undefined,
      types: {
        1: 'character',
        2: 'comic',
        3: 'series',
      },
      type: {
        id: undefined,
        name: undefined,
      },
      capitalize,
    }
  },
  async mounted() {
    await this.getBoard();
    this.type.name = this.types[this.$route.params.boardId];
    this.type.id = this.$route.params.boardId;
    if(this.$route.params.entity === this.type.name){
      await this.$nextTick(() => {
        this.$refs.tabs.select(1);
      });
    }
  },
  methods: {
    async getBoard(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/forum/board/${this.$route.params.boardId}`);
      const {topics, ...board} = data;
      this.board = board;
      this.topics = topics;
    },
    async tabSelected(e) {
      if(e.selectedIndex === 0){
        this.$router.push(`/forum/board/${this.type.id}`);
        return;
      }
      this.$router.push(`/forum/board/${this.type.id}/${this.type.name}`);
    },
  },
  provide: {
    grid: [Sort, Toolbar, Search, Filter, Resize]
  }
}
</script>

<style scoped>

</style>