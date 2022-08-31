<template>
  <ejs-grid :key="tableKey" :allowSorting="true"
            :allowResizing="true"
            :dataSource="topics"
            :allowFiltering="true"
            :filterSettings="filterSettings"
            :rowTemplate="'rowTemplate'"
            :toolbar="toolbar" ref="grid">
    <e-columns>
      <e-column field="marvelEntityName" :visible="false"></e-column>
      <e-column field="ownerUsername" :visible="false"></e-column>
      <e-column headerText="Watched" field="watched" width="50" textAlign="Right"></e-column>
      <e-column headerText="Topic title" field="title" width="200" textAlign="Center"></e-column>
      <e-column headerText='Posts' field="postCount" textAlign='Center' width=80></e-column>
      <e-column headerText='Last post' field="lastPostDate" textAlign='Center' width=80></e-column>
    </e-columns>
    <template v-slot:rowTemplate="{data}">
      <tr :id="'topic' + data.id">
        <td>
          <div v-if="isAuthorized" class="row">
            <div class="col align-self-center">
              <ejs-button :iconCss="[data.watched ? 'e-icons e-eye-slash' : 'e-icons e-eye']"
                          iconPosition="Right" :isPrimary="!data.watched" @click.stop="toggleWatchTopic(data)"
                          :title="data.watched ? 'Unwatch topic' : 'Watch topic'">{{data.watched ? 'Unwatch' : 'Watch'}}</ejs-button>

            </div>
          </div>
        </td>
        <td>
          <div class="row">
            <div class="col">
              <div class="row mb-2">
                <div class="col">
                  <b style="font-size: 16px;"><a class="custom-link" :href="`/forum/board/${type.id}/${type.name}/${data.marvelEntityId}/topic/${data.id}`"
                                                 @click.prevent="openTopic(data.marvelEntityId, data.id)">{{ data.title }}</a></b>
                </div>
              </div>
              <div class="row">
                <div class="col">
                  <a class="custom-link" :href="`/${type.name}/${data.marvelEntityId}`"
                     @click.prevent="openMarvelEntity(data.marvelEntityId)">
                    {{data.marvelEntityName}}</a> - <a class="custom-link" :href="`/profile/${data.ownerUsername}`"
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
</template>

<script>
import {
  ColumnDirective,
  ColumnsDirective,
  Filter,
  GridComponent, Resize,
  Search,
  Sort,
  Toolbar
} from "@syncfusion/ej2-vue-grids";
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import axios from "axios";
import {onIdTokenChanged} from "firebase/auth";
import {auth} from "@/firebaseConfig";
import {router} from "@/main";

export default {
  name: "RecentMarvelEntityTopics",
  components: {
    'ejs-grid' : GridComponent,
    'e-columns' : ColumnsDirective,
    'e-column' : ColumnDirective,
    'ejs-button' : ButtonComponent,
  },
  props: {
    topicsProp: {
      type: Array,
      required: true,
    },
    type: {
      type: Object,
      required: true,
    },
  },
  data(){
    return {
      topics: [],
      isAuthorized: false,
      toolbar: ['Search'],
      filterSettings: {type: 'Menu'},
      tableKey: 0,
    }
  },
  async mounted(){
    this.topics = this.topicsProp;
    onIdTokenChanged(auth, (user) => {
      this.isAuthorized = user != null;
    })
  },
  methods: {
    async toggleWatchTopic(topic){
      const {data} = await axios.post(`${process.env.VUE_APP_BACKEND}/forum/topic/${topic.id}/watch`);
      this.topics.find(t => t.id === topic.id).watched = !!data;
      this.tableKey++;
    },
    openTopic(characterId, topicId){
      router.push(`/forum/board/${this.type.id}/${this.type.name}/${characterId}/topic/${topicId}`);
    },
    openMarvelEntity(id){
      router.push(`/forum/board/${this.type.id}/${this.type.name}/${id}`);
    },
    openProfile(username){
      router.push(`/profile/${username}`);
    },
  },
  provide: {
    grid: [Sort, Toolbar, Search, Filter, Resize]
  },
}
</script>

<style scoped>

</style>