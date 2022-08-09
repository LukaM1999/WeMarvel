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
              <e-column headerText='Recent active topics' textAlign='Center' width=100></e-column>
            </e-columns>
            <template v-slot:rowTemplate="{data}">
              <tr>
                <td>
                  <div class="row title-row gy-1">
                    <div class="col">
                      <h2>
                        {{ data.title }}
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
                    <div class="col">
                      <a class="custom-link"
                         :href="`${frontend}/forum/topic/${data.firstTopicId}`"
                         @click.prevent="openRecentTopic(data.firstTopicId)">
                        {{ data.firstTopicTitle }}
                      </a>
                      <div class="topic-details">
                        <i>{{ data.firstTopicDate }}, by <a class="custom-link" :href="`${frontend}/profile/${data.firstTopicUsername}`"
                                                             @click.prevent="openProfile(data.firstTopicUsername)">
                          {{data.firstTopicUsername}}</a></i>
                      </div>
                    </div>
                  </div>
                  <div v-if="data.secondTopicId" class="row">
                    <div class="col">
                      <a class="custom-link" :href="`${frontend}/forum/topic/${data.secondTopicId}`" @click.prevent="openRecentTopic(data.secondTopicId)">{{ data.secondTopicTitle }}</a>
                      <div class="topic-details">
                        <i>{{ data.secondTopicDate }}, by <a class="custom-link" :href="`${frontend}/profile/${data.secondTopicUsername}`"
                                                             @click.prevent="openProfile(data.secondTopicUsername)">
                          {{data.secondTopicUsername}}</a></i>
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
      frontend: process.env.VUE_APP_FRONTEND,
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
      console.log(this.boards)
    },
    openRecentTopic(topicId){
      this.$router.push({name: 'topic', params: {id: topicId}});
    },
    openProfile(username){
      this.$router.push({name: 'profile', params: {username: username}});
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

.custom-link {
  font-weight: bold;
  text-decoration: none;
}

.custom-link:hover {
  text-decoration: underline;
  /*color: rgba(81, 140, 202, 0.8);*/
}

.custom-link:visited {
  /*color: rgba(81, 140, 202, 0.8);*/
}
</style>