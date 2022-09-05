<template>
  <div id="boardContainer">
    <h1>{{marvelEntity.title || board.title}}</h1>
    <p>{{marvelEntity.description || board.description}}</p>
    <div :key="newTopicKey" id="newTopicContainer" v-if="showNewTopicForm" class="row mb-5">
      <div class="col">
        <h2>New topic</h2>
        <div class="row mb-3 justify-content-center">
          <div class="col">
            <div class="e-control e-lib e-primary">
              <div class="e-input-group w-50 e-float-input">
                <input type="text" v-model="newTopic.title" required />
                <label class="e-float-text e-label">Title*</label>
              </div>
            </div>
          </div>
        </div>
        <h3>First topic post*</h3>
        <RichTextEditor ref="rte" @value-changed="updateTopicContent"/>
        <ejs-button  @click="createNewTopic" class="mt-4"
                     :content="'Create topic'"></ejs-button>
      </div>
    </div>
    <div class="row">
      <div class="col">
        <ejs-grid ref="grid" :key="tableKey"
                  :allowSorting="true"
                  :allowPaging="true"
                  :allowResizing="true"
                  :allowFiltering="true"
                  :filterSettings="filterSettings"
                  :dataSource="topics"
                  :editSettings="editSettings"
                  :pageSettings="pageSettings"
                  :rowTemplate="'rowTemplate'"
                  :toolbar="toolbarOptions"
                  :actionBegin="actionBegin">
          <e-columns>
            <e-column headerText="Watched" field="watched" width="50" textAlign="Center"></e-column>
            <e-column headerText="Topic title" field="title" width="200" textAlign="Center"></e-column>
            <e-column headerText='Posts' field="postCount" textAlign='Center' width=80></e-column>
            <e-column headerText='Last post' field="lastPostDate" textAlign='Center' width=80></e-column>
          </e-columns>
          <template v-slot:rowTemplate="{data}">
            <tr :id="'topic' + data.id">
              <td>
                <div v-if="isAuthorized" class="row">
                  <div class="col-1 align-self-center">
                    <ejs-button :iconCss="[data.watched ? 'e-icons e-eye-slash' : 'e-icons e-eye']"
                                iconPosition="Right" :isPrimary="!data.watched" @click.stop="toggleWatchTopic(data)"
                                :title="data.watched ? 'Unwatch topic' : 'Watch topic'">
                      {{data.watched ? 'Unwatch' : 'Watch'}}
                    </ejs-button>
                  </div>
                </div>
              </td>
              <td>
                <div class="row">
                  <div class="col">
                    <h3><a class="custom-link" :href="`./${data.marvelEntityId ? data.marvelEntityId : $route.params.boardId}/topic/${data.id}`"
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
import {
  ColumnDirective,
  ColumnsDirective,
  GridComponent,
  Sort,
  Toolbar,
  Search,
  Filter, Page, Edit, Resize
} from "@syncfusion/ej2-vue-grids";
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import RichTextEditor from "@/components/RichTextEditor";
import {store} from "@/main";
import {onIdTokenChanged} from "firebase/auth";
import {auth} from "@/firebaseServices/firebaseConfig";

export default {
  name: "BoardTopics",
  components: {
    'ejs-grid' : GridComponent,
    'e-columns' : ColumnsDirective,
    'e-column' : ColumnDirective,
    'ejs-button' : ButtonComponent,
    RichTextEditor,
  },
  data() {
    return {
      board: {},
      topics: [],
      marvelEntity: {},
      tableKey: 0,
      toolbarOptions: ['Search'],
      editSettings: {
        allowAdding: true,
        mode: 'Dialog',
      },
      pageSettings: {
        pageCount: 5,
        pageSize: 20,
        pageSizes: [10, 20, 50, 100]
      },
      filterSettings: {type: 'Menu'},
      showNewTopicForm: false,
      newTopic: {
        title: '',
        content: ''
      },
      isAuthorized: false,
      newTopicKey: 0,
    }
  },
  async mounted() {
    onIdTokenChanged(auth, (user) => {
      this.isAuthorized = !!user;
      this.toolbarOptions = ['Search'];
      if (this.isAuthorized && !this.toolbarOptions[1]) {
        this.toolbarOptions.push('Add');
      }
    })
    if(this.$route.params.entity){
      await this.getEntity();
      await this.getEntityTopics();
    }
    else if(this.$route.params.boardId) {
      await this.getBoard();
    }
  },
  methods: {
    async getBoard(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/forum/board/${this.$route.params.boardId}`);
      const {topics, ...board} = data;
      this.board = board;
      this.topics = topics;
      this.tableKey++;
    },
    async getEntity(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/${this.$route.params.entity}/${this.$route.params.entityId}`);
      this.marvelEntity = data;
      this.marvelEntity.title = data.name || data.title;
    },
    async getEntityTopics(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/forum/${this.$route.params.entity}/${this.$route.params.entityId}/topic`);
      this.topics = data;
      this.tableKey++;
    },
    openTopic(topicId){
      this.$router.push(`./${this.marvelEntity.id ? this.marvelEntity.id : this.$route.params.boardId}/topic/${topicId}`);
    },
    openProfile(username){
      this.$router.push({name: 'profile', params: {username: username}});
    },
    async toggleWatchTopic(topic){
      const {data} = await axios.post(`${process.env.VUE_APP_BACKEND}/forum/topic/${topic.id}/watch`);
      this.topics.find(t => t.id === topic.id).watched = !!data;
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
      const newTopic = {
        title: this.newTopic.title,
        firstPostContent: this.newTopic.content,
        ownerUsername: store.getters.user?.displayName,
        boardId: this.$route.params.boardId,
        marvelEntityId: this.$route.params.entityId,
      }
      const {data} = await axios.post(`${process.env.VUE_APP_BACKEND}/forum/board/${this.$route.params.boardId}/topic`, newTopic);
      data.postCount = 1;
      data.lastPostDate = data.createdAt;
      this.topics.push(data);
      this.tableKey++;
      this.newTopic.title = '';
      this.newTopic.content = '';
      this.toggleTopicForm();
    },
    actionBegin(e){
      if(e.requestType === 'add'){
        e.cancel = true;
        this.newTopic.content = '';
        this.newTopic.title = '';
        this.showNewTopicForm = false;
        this.newTopicKey++;
        this.showNewTopicForm = true
        this.newTopicKey++;
        this.$nextTick(() => {
          document.getElementById('newTopicContainer').scrollIntoView({behavior: 'smooth'});
        })
      }
    },
  },
  provide: {
    grid: [Sort, Toolbar, Search, Filter, Page, Edit, Resize]
  }
}
</script>

<style scoped>

</style>