<template>
<div>
  <h1>{{board.title}}</h1>
  <p>{{board.description}}</p>
</div>
</template>

<script>
import {ColumnDirective, ColumnsDirective, GridComponent, Search, Sort, Toolbar} from "@syncfusion/ej2-vue-grids";
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import RichTextEditor from "@/components/RichTextEditor";
import axios from "axios";
import {store} from "@/main";

export default {
  name: "ComicBoard",
  components: {
    // 'ejs-grid' : GridComponent,
    // 'e-columns' : ColumnsDirective,
    // 'e-column' : ColumnDirective,
    // 'ejs-button' : ButtonComponent,
    // RichTextEditor,
  },
  data() {
    return {
      board: {topics: []},
      tableKey: 0,
      toolbarOptions: ['Search'],
      showNewTopicForm: false,
      newTopic: {
        title: '',
        content: ''
      },
    }
  },
  async mounted() {
    await this.getBoard();
  },
  methods: {
    async getBoard(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/forum/board/2`);
      this.board = data;
      this.tableKey++;
    },
    openTopic(topicId){
      this.$router.push({name: 'topic', params: {boardId: this.$route.params.id, id: topicId}});
    },
    openProfile(username){
      this.$router.push({name: 'profile', params: {username: username}});
    },
    async toggleWatchTopic(topic){
      const {data} = await axios.post(`${process.env.VUE_APP_BACKEND}/forum/topic/${topic.id}/watch`);
      this.board.topics.find(t => t.id === topic.id).watched = !!data;
      this.tableKey++;
    },
    updateTopicContent(value){
      this.newTopic.content = value;
    },
    toggleTopicForm(){
      this.showNewTopicForm = !this.showNewTopicForm;
    },
    async createNewTopic(){
      if(!this.newTopic.title.length || !this.newTopic.content.length){
        return;
      }
      const {data} = await axios.post(`${process.env.VUE_APP_BACKEND}/forum/board/${this.board.id}/topic`, {
        title: this.newTopic.title,
        firstPostContent: this.newTopic.content,
        ownerUsername: store.getters.user?.displayName,
        boardId: this.board.id
      });
      data.postCount = 1;
      data.lastPostDate = data.createdAt;
      this.board.topics.push(data);
      this.tableKey++;
      this.newTopic.title = '';
      this.newTopic.content = '';
      this.toggleTopicForm();
    },
  },
  provide: {
    grid: [Sort, Toolbar, Search]
  }
}
</script>

<style scoped>

</style>