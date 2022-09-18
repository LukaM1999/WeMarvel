<template>
  <div id="boardContainer">
    <h1>{{marvelEntity.title || board.title}}</h1>
    <div v-if="marvelEntity.description || board.description" class="row mb-2 justify-content-center">
      <div class="col-8">
        <p>{{marvelEntity.description || board.description}}</p>
      </div>
    </div>
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
        <ejs-button :disabled="!isNewTopicValid()" @click="createNewTopic" class="mt-4"
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
                  :allowTextWrap="true"
                  :filterSettings="filterSettings"
                  :dataSource="topics"
                  :editSettings="editSettings"
                  :pageSettings="pageSettings"
                  :toolbar="toolbarOptions"
                  :actionBegin="actionBegin"
                  :rowDataBound="rowDataBound"
                  :toolbarClick="toolbarClicked">
          <e-columns>
            <e-column headerText="Watched" field="watched" width="80" :template="'watchedTemplate'" textAlign="Center"></e-column>
            <e-column headerText="Topic" field="title" :template="'titleTemplate'" textAlign="Center"></e-column>
            <e-column headerText='Posts' field="postCount" textAlign='Center' width=80></e-column>
            <e-column headerText='Last post' field="lastPostDate" textAlign='Center' width=80></e-column>
          </e-columns>
          <template v-slot:watchedTemplate="{data}">
            <div v-if="isAuthorized" class="row">
              <div class="col-1 align-self-center">
                <ejs-button :iconCss="[data.watched ? 'e-icons e-eye-slash' : 'e-icons e-eye']"
                            iconPosition="Right" :isPrimary="!data.watched" @click.stop="toggleWatchTopic(data)"
                            :title="data.watched ? 'Unwatch topic' : 'Watch topic'">
                  {{data.watched ? 'Unwatch' : 'Watch'}}
                </ejs-button>
              </div>
            </div>
          </template>
          <template v-slot:titleTemplate="{data}">
            <div class="row">
              <div class="col">
                <b v-if="data.sticky">Sticky</b><h3><a class="custom-link" 
                :href="`./${data.marvelEntityId ? data.marvelEntityId : $route.params.boardId}/topic/${data.id}`">
                {{ data.title }}</a></h3>
              </div>
            </div>
            <div class="row">
              <div class="col">
                <a v-if="data.ownerEnabled" class="custom-link" :href="`/profile/${data.ownerUsername}`">
                  {{data.ownerUsername}}</a><span v-else>[removed user]</span> - {{data.createdAt}}
              </div>
            </div>
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
import {getIdTokenResult, onIdTokenChanged} from "firebase/auth";
import {auth} from "@/firebaseServices/firebaseConfig";
import {DialogUtility} from "@syncfusion/ej2-vue-popups";
import {ToastUtility} from "@syncfusion/ej2-vue-notifications";

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
        allowAdding: false,
        allowDeleting: false,
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
      isAdmin: false,
      newTopicKey: 0,
      toastUtility: ToastUtility,
    }
  },
  async mounted() {
    if(this.$route.params.entity){
      await this.getEntity();
      await this.getEntityTopics();
    }
    else if(this.$route.params.boardId) {
      await this.getBoard();
    }
    onIdTokenChanged(auth, async (user) => {
      this.isAuthorized = !!user;
      this.toolbarOptions = ['Search'];
      if (this.isAuthorized && !this.toolbarOptions[1]) {
        this.toolbarOptions = ['Search', 'Add'];
        this.editSettings = {
          allowAdding: true,
          allowDeleting: false,
          mode: 'Dialog',
        };
      }
      const tokenResult = await getIdTokenResult(user);
      this.isAdmin = tokenResult.claims.admin;
      if(!this.isAdmin) return;
      this.toolbarOptions = ['Search', 'Add', 'Delete', {
          text: 'Toggle stick to top', 
          disabled: false, 
          tooltipText: 'Stick to top', 
          prefixIcon: 'e-icons e-chevron-up-double', 
          id: 'stickToTop'
        }
      ];
      this.editSettings = {
        allowAdding: true,
        allowDeleting: true,
        mode: 'Dialog',
      };
    })
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
    async toggleWatchTopic(topic){
      const {data} = await axios.post(`${process.env.VUE_APP_BACKEND}/forum/topic/${topic.id}/watch`);
      this.topics.find(t => t.id === topic.id).watched = !!data;
      this.tableKey++;
    },
    updateTopicContent(value){
      console.log(value);
      this.newTopic = {
        title: this.newTopic.title,
        content: value
      }
    },
    toggleTopicForm(){
      this.showNewTopicForm = !this.showNewTopicForm;
    },
    isNewTopicValid(){
      console.log(this.newTopic);
      return this.newTopic.title.length > 0;
    },
    async createNewTopic(){
      if(!this.isNewTopicValid()) return;
      const newTopic = {
        title: this.newTopic.title,
        firstPostContent: this.newTopic.content,
        ownerUsername: store.getters.user?.displayName,
        boardId: this.$route.params.boardId,
        marvelEntityId: this.$route.params.entityId,
      }
      const {data} = await axios.post(`${process.env.VUE_APP_BACKEND}/forum/board/${this.$route.params.boardId}/topic`, newTopic);
      const toast = ToastUtility.show({
        title: 'Topic successfully created!',
        content: `New topic '${data.title}' created successfully!`,
        position: {X: document.body.offsetWidth - 360, Y: 80},
        cssClass: 'e-toast-success',
        showCloseButton: true,
        timeOut: 5000,
        extendedTimeout: 5000,
        buttons: [{
          click: () => {
            this.$router.push(`/forum/board/${data.boardId}/topic/${data.id}`);
            toast.hide();
          },
          model: {
            content: 'Open new topic',
          }
        }],
      });
      data.postCount = 1;
      data.lastPostDate = data.createdAt;
      this.topics.push(data);
      this.tableKey++;
      this.newTopic.title = '';
      this.newTopic.content = '';
      this.toggleTopicForm();
    },
    async deleteTopic(topicId){
      await axios.delete(`${process.env.VUE_APP_BACKEND}/forum/topic/${topicId}`);
      ToastUtility.show({
        title: 'Topic deleted',
        content: 'Topic deleted successfully',
        position: {X: document.body.offsetWidth - 360, Y: 80},
        cssClass: 'e-toast-success',
        showCloseButton: true,
        timeOut: 5000,
        extendedTimeout: 5000,
      });
      this.topics = this.topics.filter(t => t.id !== topicId);
    },
    async actionBegin(e){
      if(e.requestType === 'delete'){
        e.cancel = true;
        let dialog = DialogUtility.confirm({
          title: 'Delete topic',
          showCloseIcon: true,
          closeOnEscape: true,
          position: {X: 'center', Y: 'center'},
          target: document.body,
          content: 'Are you sure you want to delete this topic?',
          okButton: {
            text: 'Yes',
            click: async () => {
              await this.deleteTopic(e.data[0]?.id);
              dialog.hide();
            }
          },
          cancelButton: {
            text: 'No',
            click: function() {
              this.hide();
            }
          }
        });
        return;
      }
      if(e.requestType === 'add'){
        e.cancel = true;
        this.newTopic.content = '';
        this.newTopic.title = '';
        this.showNewTopicForm = false;
        this.newTopicKey++;
        this.showNewTopicForm = true
        await this.$nextTick;
        document.getElementById('newTopicContainer').scrollIntoView({behavior: 'smooth'});
      }
    },
    rowDataBound(e){
      if(e.data.sticky){
        e.row.classList.add('sticky');
      }
    },
    async toolbarClicked(e){
      if(e.item.id !== 'stickToTop') return;
      const selectedTopic =  this.$refs.grid.getSelectedRecords()[0];
      if(!selectedTopic) return;
      await axios.patch(`${process.env.VUE_APP_BACKEND}/forum/topic/${selectedTopic.id}/sticky`);
      if(this.$route.params.entity){
        await this.getEntityTopics();
      }
      else if(this.$route.params.boardId) {
        await this.getBoard();
      }
    }
  },
  provide: {
    grid: [Sort, Toolbar, Search, Filter, Page, Edit, Resize]
  }
}
</script>

<style scoped>

</style>