<template>
  <div class="forum-container">
    <h1>Forum</h1>
    <div id="formContainer" v-if="showForm" class="row mt-2 mb-3 justify-content-center">
      <div class="col-6 justify-content-center">
        <h2>{{selectedBoard.id ? 'Edit board' : 'Add board'}}</h2>
        <div class="row">
          <div class="col">
            <ejs-textbox :value="newBoard.title"
                         v-model="newBoard.title"
                         :floatLabelType="'Auto'"
                         placeholder="Title*"
                         ref="titleRef"
                         :multiline="false"
                         maxLength="3000"
                         required>
            </ejs-textbox>
          </div>
        </div>
        <div class="row">
          <div class="col">
            <ejs-textbox :value="newBoard.description"
                         v-model="newBoard.description"
                         :floatLabelType="'Auto'"
                         placeholder="Description*"
                         ref="descriptionRef"
                         :multiline="true"
                         maxLength="3000"
                         required>
            </ejs-textbox>
          </div>
        </div>
        <div class="row mt-2">
          <div class="col">
            <ejs-button :disabled="!isBoardValid()"
                        isPrimary="true"
                        @click="submitBoard"
                        :content="selectedBoard.id ? 'Edit board' : 'Add board'">
            </ejs-button>
          </div>
        </div>
      </div>
    </div>
    <div class="row">
      <div class="col">
        <ejs-grid :key="boardKey" ref="grid" :dataSource="boards"
                  :allowResizing="true"
                  :allowFiltering="true" :toolbar="toolbar"
                  :allowTextWrap="true" :editSettings="isAdmin ? editSettings : ''"
                  :filterSettings="{type: 'Menu'}"
                  :allowSorting="true"
                  :beginEdit="beforeEdit"
                  :actionBegin="actionBegin">
          <e-columns>
            <e-column field="title" headerText="Board" width="80" :template="'titleTemplate'" textAlign="Center"></e-column>
            <e-column field="description" :visible="false"></e-column>
            <e-column headerText='Recent active topics' textAlign='Center' :template="'topicsTemplate'" width=100></e-column>
          </e-columns>
          <template v-slot:titleTemplate="{data}">
            <div style="font-size: 16px;" class="row">
              <div class="col">
                <div class="row">
                  <div class="col">
                    <h2>
                      <a class="custom-link" :href="`/forum/board/${data.id}`">{{ data.title }}</a>
                    </h2>
                  </div>
                </div>
                <div class="row">
                  <div class="col">
                    <div>
                      {{ data.description }}
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </template>
          <template v-slot:topicsTemplate="{data}">
            <div style="font-size: 16px;" v-for="(topic, i) in data.topics" :key="i" class="row">
              <div class="col-2 d-flex justify-content-end">
                <a v-if="topic.ownerEnabled" :href="`/profile/${topic.ownerUsername}`">
                  <img width="50" height="50" :src="topic.ownerImageUrl || '/placeholder.jpg'" :alt="topic.ownerUsername" />
                </a>
                <img v-else width="50" height="50" :src="'/placeholder.jpg'" alt="removed" />
              </div>
              <div class="col justify-content-start d-grid">
                <div class="row">
                  <div class="col d-flex">
                    <a class="custom-link"
                       :href="getTopicUrl(topic.boardId, topic.id, topic.marvelEntityId)">
                      {{ topic.title }}
                    </a>
                  </div>
                </div>
                <div class="row">
                  <div class="col">
                    <div class="topic-details">
                      <i>{{ topic.createdAt }}, by <a v-if="topic.ownerEnabled" class="custom-link" :href="`/profile/${topic.ownerUsername}`">
                        {{topic.ownerUsername}}</a><span v-else>[removed user]</span></i>
                    </div>
                  </div>
                </div>
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
  Edit,
  Filter,
  GridComponent,
  Resize,
  Toolbar
} from "@syncfusion/ej2-vue-grids";
import {getIdTokenResult, onIdTokenChanged} from "firebase/auth";
import {auth} from "@/firebaseServices/firebaseConfig";
import {DialogUtility} from "@syncfusion/ej2-vue-popups";
import {TextBoxComponent} from "@syncfusion/ej2-vue-inputs";
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import {ToastUtility} from "@syncfusion/ej2-vue-notifications";
import {getStorage, listAll, ref, deleteObject} from "firebase/storage";

export default {
  name: "ForumOverview",
  components: {
    'ejs-grid' : GridComponent,
    'e-columns' : ColumnsDirective,
    'e-column' : ColumnDirective,
    'ejs-textbox' : TextBoxComponent,
    'ejs-button' : ButtonComponent,
  },
  data(){
    return {
      boards: [],
      toolbar: ['Search'],
      isAdmin: false,
      editSettings: {
        allowEditing: true,
        allowAdding: true,
        allowDeleting: true,
        mode: 'Dialog',
      },
      showForm: false,
      newBoard: {
        title: '',
        description: '',
      },
      selectedBoard: {
        id: '',
        title: '',
        description: '',
      },
      boardKey: 0,
    }
  },
  async mounted() {
    onIdTokenChanged(auth, async (user) => {
      if (user) {
        const tokenResult = await getIdTokenResult(user)
        this.isAdmin = tokenResult.claims.admin;
      }
      if(this.isAdmin) this.toolbar = ['Search', 'Add', 'Edit', 'Delete'];
      else this.toolbar = ['Search'];
    });
    await this.getBoards();
  },
  methods: {
    async getBoards(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/forum/boards`);
      this.boards = data;
    },
    getTopicUrl(boardId, topicId, marvelEntityId){
      if(!marvelEntityId) return `/forum/board/${boardId}/topic/${topicId}`;
      if(boardId === 1) return `/forum/board/${boardId}/character/${marvelEntityId}/topic/${topicId}`;
      if(boardId === 2) return `/forum/board/${boardId}/comic/${marvelEntityId}/topic/${topicId}`;
      return `/forum/board/${boardId}/series/${marvelEntityId}/topic/${topicId}`;
    },
    isBoardValid(){
      return this.newBoard.title.length > 0 && this.newBoard.description.length > 0;
    },
    async submitBoard(){
      await axios.post(`${process.env.VUE_APP_BACKEND}/forum/board`, this.newBoard);
      ToastUtility.show({
        content: `Board ${this.selectedBoard.title} ${this.selectedBoard.id ? 'updated' : 'created'} successfully`,
        title: `Board ${this.selectedBoard.id ? 'updated' : 'created'}`,
        position: {X: document.body.offsetWidth - 360, Y: 80},
        cssClass: 'e-toast-success',
        timeOut: 5000,
        extendedTimeout: 5000,
      });
      this.showForm = false;
      this.newBoard = {
        title: '',
        description: '',
      };
      this.selectedBoard = {
        id: '',
        title: '',
        description: '',
      };
      await this.getBoards();
    },
    async deleteBoard(boardId){
      await axios.delete(`${process.env.VUE_APP_BACKEND}/forum/board/${boardId}`);
      try {
        const filesDeleted = await this.deleteFolderRecursive(`board/${boardId}`);
        console.log(`${filesDeleted} files has been deleted`);
      } catch(err){
        console.error(err);
      }
      ToastUtility.show({
        title: 'Board deleted',
        content: 'Board deleted successfully',
        position: {X: document.body.offsetWidth - 360, Y: 80},
        cssClass: 'e-toast-success',
        timeOut: 5000,
        extendedTimeout: 5000,
      });
      this.boards = this.boards.filter(board => board.id !== boardId);
    },
    async deleteFile(filePath){
      const reference = ref(getStorage(), filePath);
      return await deleteObject(reference);
    },
    async deleteFolderRecursive(folderPath){
      const list = await listAll(ref(getStorage(), folderPath));
      let filesDeleted = 0;

      for await (const fileRef of list.items) {
        await this.deleteFile(fileRef.fullPath);
        filesDeleted++;
      }
      for await (const folderRef of list.prefixes) {
        filesDeleted += await this.deleteFolderRecursive(folderRef.fullPath);
      }
      return filesDeleted;
    },
    beforeEdit(e){
      e.cancel = true;
      this.showForm = false;
      this.boardKey++;
      this.selectedBoard = {
        id: e.rowData.id,
        title: e.rowData.title,
        description: e.rowData.description,
      }
      this.newBoard = {
        id: e.rowData.id,
        title: e.rowData.title,
        description: e.rowData.description,
      }
      this.showForm = true;
      this.$nextTick(() => {
        document.getElementById('formContainer').scrollIntoView({behavior: 'smooth'});
      })
    },
    actionBegin(e){
      if(e.requestType === 'delete'){
        e.cancel = true;
        let dialog = DialogUtility.confirm({
          title: 'Delete board',
          showCloseIcon: true,
          closeOnEscape: true,
          position: {X: 'center', Y: 'center'},
          target: document.body,
          content: 'Are you sure you want to delete this board?',
          okButton: {
            text: 'Yes',
            click: async () => {
              await this.deleteBoard(e.data[0]?.id);
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
        this.showForm = false;
        this.selectedBoard = {
          id: '',
          title: '',
          description: '',
        };
        this.newBoard = {
          id: '',
          title: '',
          description: '',
        };
        this.boardKey++;
        this.showForm = true
        this.$nextTick(() => {
          document.getElementById('formContainer').scrollIntoView({behavior: 'smooth'});
        })
      }
    },
    rowDeselected(e){
      if(!this.selectedBoard.id) return;
      this.selectedBoard = {
        id: '',
        title: '',
        description: '',
      }
      this.showForm = false;
    },
  },
  provide: {
    grid: [Toolbar, Resize, Filter, Edit]
  }
}
</script>

<style scoped>
.forum-container{
  overflow-x: hidden;
}

.title-row {
  margin-bottom: -1.5rem;
}

.topic-title-row {
  margin-top: -3.5rem;
}

h4 {
  margin-block: 0;
}

h2 {
  margin-block-start: 0;
}

.topic-details {
  margin-top: -1rem;
}

tr {
  cursor: auto;
}

tr:nth-child(even) {
  background-color: #f2f2f2;
}

tr:nth-child(even):hover {
  background-color: #d9d9d9;
}

</style>