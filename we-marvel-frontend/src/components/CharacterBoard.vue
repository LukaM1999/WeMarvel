<template>
<div style="overflow-x: hidden;">
  <h1>{{board?.title}}</h1>
  <p>{{board?.description}}</p>
  <ejs-tab ref="tabs" :selected="tabSelected" class="e-fill">
    <e-tabitems>
      <e-tabitem :header="{text: 'Recent'}" :content="'recentTemplate'">
        <template v-slot:recentTemplate="{}">
          <RecentCharacterTopics v-if="topics" :topics-prop="topics"/>
        </template>
      </e-tabitem>
      <e-tabitem :header="{text: 'Characters'}" :content="'characterTemplate'">
        <template v-slot:characterTemplate="{}">
          <CharactersInBoard/>
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
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import RichTextEditor from "@/components/RichTextEditor";
import axios from "axios";
import {store} from "@/main";
import {TabComponent, TabItemDirective, TabItemsDirective} from "@syncfusion/ej2-vue-navigations";
import {auth} from "@/firebaseConfig";
import {onIdTokenChanged} from "firebase/auth";
import CharactersInBoard from "@/components/CharactersInBoard";
import RecentCharacterTopics from "@/components/RecentCharacterTopics";

export default {
  name: "CharacterBoard",
  components: {
    RecentCharacterTopics,
    CharactersInBoard,
    "ejs-tab": TabComponent,
    "e-tabitems": TabItemsDirective,
    "e-tabitem": TabItemDirective,
  },
  data() {
    return {
      board: {},
      topics: undefined,
    }
  },
  async mounted() {
    await this.getBoard();
    if(this.$route.path.indexOf('character') > -1) {
      this.$refs.tabs.select(1);
    }
  },
  methods: {
    async getBoard(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/forum/board/1`);
      this.board.id = data.id;
      this.board.title = data.title;
      this.board.description = data.description;
      this.topics = data.topics;
    },
    async tabSelected(e) {
      if(e.selectedIndex === 0){
        this.$router.push(`/forum/board/1`);
        return;
      }
      this.$router.push(`/forum/board/1/character`);
    },
  },
  provide: {
    grid: [Sort, Toolbar, Search, Filter, Resize]
  }
}
</script>

<style scoped>

</style>