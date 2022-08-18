<template>
  <div id="boardContainer">
    <h1>{{ board?.title }}</h1>
    <p>{{board.description}}</p>
    <ejs-button @click="toggleTopicForm" class="mb-3 mt-2">{{showNewTopicForm ? 'Cancel' : 'New topic'}}</ejs-button>
    <div id="newTopicContainer" v-if="showNewTopicForm" class="row mb-5">
      <div class="col">
        <h3>New topic</h3>
        <div class="row mb-3 justify-content-center">
          <div class="col">
            <div class="e-control e-lib e-primary">
              <div class="e-input-group w-50 e-float-input">
                <input type="text" v-model="newTopic.title" required />
                <label class="e-float-text e-label">Title</label>
              </div>
            </div>
          </div>
        </div>
        <RichTextEditor @value-changed="updateTopicContent"/>
        <ejs-button  @click="createNewTopic" class="mt-4"
                     :content="'Create topic'"></ejs-button>
      </div>
    </div>
    <div class="row">
      <div class="col">
        <ejs-grid :key="tableKey" allowSorting="true" ref="grid" :dataSource="board.topics"
                  :rowTemplate="'rowTemplate'" :toolbar="toolbarOptions">
          <e-columns>
            <e-column headerText="Topic title" field="title" width="200" textAlign="Center"></e-column>
            <e-column headerText='Posts' field="postCount" textAlign='Center' width=80></e-column>
            <e-column headerText='Last post' field="lastPostDate" textAlign='Center' width=80></e-column>
          </e-columns>
          <template v-slot:rowTemplate="{data}">
            <tr :id="'topic' + data.id">
              <td>
                <div class="row">
                  <div class="col-1 align-self-center">
                    <ejs-button :iconCss="[data.watched ? 'e-icons e-eye-slash' : 'e-icons e-eye']"
                                iconPosition="Right" @click.stop="toggleWatchTopic(data)"
                                :title="data.watched ? 'Unwatch topic' : 'Watch topic'">{{data.watched ? 'Unwatch' : 'Watch'}}</ejs-button>

                  </div>
                  <div class="col">
                    <div class="row">
                      <div class="col">
                        <h3><a class="custom-link" :href="`/forum/board/${$route.params.id}/topic/${data.id}`"
                               @click.prevent="openTopic(data.id)">{{ data.title }}</a></h3>
                      </div>
                    </div>
                    <div class="row">
                      <div class="col">
                          <a class="custom-link" :href="`/profile/${data.ownerUsername}`"
                                                            @click.prevent="openProfile(data.ownerUsername)">
                            {{data.ownerUsername}}</a> - {{data.createdAt}}
                      </div>
                    </div>
                  </div>
                </div>
              </td>
              <td>
                {{ data.postCount }}
              </td>
              <td>
                {{ data.lastPostDate }}
              </td>
            </tr>
          </template>
        </ejs-grid>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {ColumnDirective, ColumnsDirective, GridComponent, Sort, Toolbar, Search} from "@syncfusion/ej2-vue-grids";
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import RichTextEditor from "@/components/RichTextEditor";
import {store} from "@/main";

export default {
  name: "Board",
  components: {
    'ejs-grid' : GridComponent,
    'e-columns' : ColumnsDirective,
    'e-column' : ColumnDirective,
    'ejs-button' : ButtonComponent,
    RichTextEditor,
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
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/forum/board/${this.$route.params.id}`);
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