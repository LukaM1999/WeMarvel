<template>
  <div class="forum-container">
    <h1>Forum</h1>
    <div class="row">
      <div class="col-8 gx-1">
        <div class="e-card">
          <ejs-grid :key="tableKey" ref="grid" :dataSource="boards"
                    :rowTemplate="'rowTemplate'">
            <e-columns>
              <e-column headerText="Board" width="80" textAlign="Center"></e-column>
              <e-column headerText='Recent active topics' textAlign='Left' width=100></e-column>
            </e-columns>
            <template v-slot:rowTemplate="{data}">
              <tr>
                <td>
                  <div class="row title-row gy-1">
                    <div class="col">
                      <h2>
                       <a class="custom-link" :href="`/forum/board/${data.id}`"
                          @click.prevent="openBoard(data.id)">{{ data.title }}</a>
                      </h2>
                    </div>
                  </div>
                  <div class="row gy-1">
                    <div class="col">
                      <div>
                        {{ data.description }}
                      </div>
                    </div>
                  </div>
                </td>
                <td v-if="data.firstTopicId">
                  <div class="row">
                    <div class="col-2 d-flex justify-content-end">
                      <a :href="`/profile/${data.firstTopicUsername}`"
                         @click.prevent="openProfile(data.firstTopicUsername)">
                        <img width="50" height="50" :src="data.firstTopicUserImageUrl || '/placeholder.jpg'" :alt="data.firstTopicUsername" />
                      </a>
                    </div>
                    <div class="col justify-content-start d-grid">
                      <div class="row">
                        <div class="col d-flex">
                          <a class="custom-link"
                             :href="`/forum/topic/${data.firstTopicId}`"
                             @click.prevent="openRecentTopic(data.firstTopicId, data.id)">
                            {{ data.firstTopicTitle }}
                          </a>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col">
                          <div class="topic-details">
                            <i>{{ data.firstTopicDate }}, by <a class="custom-link" :href="`/profile/${data.firstTopicUsername}`"
                                                                @click.prevent="openProfile(data.firstTopicUsername)">
                              {{data.firstTopicUsername}}</a></i>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div v-if="data.secondTopicId" class="row">
                    <div class="col-2 d-flex justify-content-end">
                      <a :href="`/profile/${data.secondTopicUsername}`"
                         @click.prevent="openProfile(data.secondTopicUsername)">
                        <img width="50" height="50" :src="data.secondTopicUserImageUrl || '/placeholder.jpg'"
                             :alt="data.secondTopicUsername" />
                      </a>
                    </div>
                    <div class="col justify-content-start d-grid">
                      <div class="row">
                        <div class="col d-flex">
                          <a class="custom-link"
                             :href="`/forum/topic/${data.secondTopicId}`"
                             @click.prevent="openRecentTopic(data.secondTopicId, data.id)">
                            {{ data.secondTopicTitle }}
                          </a>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col">
                          <div class="topic-details">
                            <i>{{ data.secondTopicDate }}, by <a class="custom-link" :href="`/profile/${data.secondTopicUsername}`"
                                                                @click.prevent="openProfile(data.secondTopicUsername)">
                              {{data.secondTopicUsername}}</a></i>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </td>
                <td v-else>
                  <div class="row">
                    <div class="col">
                      <h4>No topics</h4>
                    </div>
                  </div>
                </td>
              </tr>
            </template>
          </ejs-grid>
        </div>
      </div>
      <div class="col-4 gx-1">
        <div class="e-card">

        </div>
      </div>
    </div>
  </div>
  <router-view></router-view>
</template>

<script>
import axios from "axios";
import {ColumnDirective, ColumnsDirective, GridComponent} from "@syncfusion/ej2-vue-grids";

export default {
  name: "ForumOverview",
  components: {
    'ejs-grid' : GridComponent,
    'e-columns' : ColumnsDirective,
    'e-column' : ColumnDirective,
  },
  data(){
    return {
      boards: [],
      tableKey: 0,
    }
  },
  async mounted() {
    await this.getBoards();
  },
  methods: {
    async getBoards(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/forum/boards`);
      this.boards = data;
      this.boards = [...data];
    },
    openRecentTopic(topicId, boardId){
      this.$router.push({name: 'topic', params: {id: topicId, boardId: boardId}});
    },
    openProfile(username){
      this.$router.push({name: 'profile', params: {username: username}});
    },
    openBoard(boardId){
      this.$router.push({name: 'board', params: {id: boardId}});
    }
  },
}
</script>

<style scoped>
.forum-container{
  overflow-x: hidden;
  padding: 0 2rem 0 2rem;
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

.topic-details {
  margin-top: -1rem;
}

tr {
  cursor: auto;
}

tr:hover {
  background-color: initial;
}

tr:nth-child(even) {
  background-color: #f2f2f2;
}

</style>