<template>
<div id="usersContainer">
  <ejs-listview v-if="users.length > 0" :cssClass="'e-list-template'" :dataSource="users"
                :template="'userTemplate'" :fields="fields"
                :headerTemplate="'headerTemplate'"
                :showHeader="true" ref="list"
                @select="userSelected">
    <template v-slot:headerTemplate="{}">
      <div class="row">
        <div class="col align-self-center">
          <div class="e-list-header-text">
            <span class="e-list-header-text-content">{{username ? 'Friends' : 'Users'}}</span>
          </div>
        </div>
        <div class="col align-self-center">
          <input type="search" @keyup.enter="searchUsers" @click.stop v-model="search" placeholder="Search..." class="e-input" />
        </div>
        <div class="col align-self-center">
          <label for="sort" class="e-label">Sort by</label>
        </div>
        <div class="col align-self-center" style="width: 50%; justify-self: left" @click.stop>
          <ejs-combobox id="sortBy" :value="sort"
                        v-model="sort"
                        :dataSource="sortOptions"
                        @change="searchUsers">
          </ejs-combobox>
        </div>
        <div class="col align-self-center" @click.stop>
          <span :class="[sortOrder === 'ascending' ?  'e-icons e-sort-up' : 'e-icons e-sort-down']"
                @click="changeSortOrder" :title="sortOrder === 'ascending' ? 'Ascending' : 'Descending'"
                style="cursor: pointer;">
          </span>
        </div>
      </div>
    </template>
    <template v-slot:userTemplate="{data}">
      <div class="e-list-wrapper">
        <div>
          <div class="e-card-header">
            <div class="e-card-header-text">
              <div class="e-card-header-title">
                <a class="custom-link" :href="`/profile/${data.username}`">
                  {{data.username}}
                </a>
              </div>
            </div>
          </div>
          <div class="e-card-content mt-2">
            <a :href="`/profile/${data.username}`" style="max-width: inherit;">
              <img style="box-shadow: 0px 0px 10px 1px black"
                   :src="data.imageUrl ? data.imageUrl : '/placeholder.jpg'"
                   :alt="data.username" :title="data.username"/>
            </a>
          </div>
          <div class="row mt-2">
            <div class="col d-flex">
              <b class="label">Location:</b>
            </div>
            <div class="col text-nowrap">
              {{data.location ? data.location : 'Unknown'}}
            </div>
          </div>
          <div class="row">
            <div class="col d-flex">
              <b class="label">Gender:</b>
            </div>
            <div class="col">
              {{data.gender ? capitalize(data.gender.toLowerCase().replace('_', '-')) : 'Unknown'}}
            </div>
          </div>
          <div class="row">
            <div class="col d-flex">
              <b class="label">Birthday:</b>
            </div>
            <div class="col">
              {{data.birthday ? data.birthday : 'Unknown'}}
            </div>
          </div>
          <div v-if="username" class="row">
            <div class="col d-flex text-nowrap">
              <b class="label">Friends since:</b>
            </div>
            <div class="col">
              {{data.friendsSince ? data.friendsSince : 'Unknown'}}
            </div>
          </div>
        </div>
      </div>
    </template>
  </ejs-listview>
  <h1 v-if="users.length === 0">No users found</h1>
  <ejs-pager v-if="users.length > 0" ref="pager" :totalRecordsCount="totalRecordsCount" :pageSize="20"
             :pageCount="5" :click="changePage"></ejs-pager>
</div>
</template>

<script>
import {ListViewComponent} from "@syncfusion/ej2-vue-lists";
import {DataManager, Query} from '@syncfusion/ej2-data';
import {ComboBoxComponent} from "@syncfusion/ej2-vue-dropdowns";
import axios from "axios";
import {PagerComponent} from "@syncfusion/ej2-vue-grids";
import {capitalize} from "eslint-plugin-vue/lib/utils/casing";
import {router} from "@/main";

export default {
  name: "Users",
  components: {
    'ejs-listview': ListViewComponent,
    'ejs-pager': PagerComponent,
    'ejs-combobox': ComboBoxComponent
  },
  emits: ['user-selected'],
  props: {
    username: {
      type: String,
      default: ''
    }
  },
  data(){
    return {
      users: [],
      capitalize,
      fields: {
        id: 'id',
        username: 'username',
        location: 'location',
        gender: 'gender',
        birthday: 'birthday',
      },
      totalRecordsCount: 0,
      search: '',
      sort: 'Username',
      sortOrder: 'ascending',
      sortOptions: ['Username', 'Location', 'Gender', 'Birthday']
    }
  },
  async mounted() {
    if(this.username !== ''){
      await this.getFriends();
    } else {
      await this.getUsers();
    }
    if(!this.$refs.list) return;
    this.$refs.list.ej2Instances.dataSource = new DataManager(this.users).executeLocal(
        new Query().search(this.search, ['username', 'location', 'gender', 'birthday'], 'contains', true).
        sortBy(this.sort.toLowerCase(), this.sortOrder).range(0, 20));
    this.totalRecordsCount = this.users.length;
  },
  methods: {
    async getUsers(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/user/profile`);
      this.users = data;
    },
    async getFriends(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/friend/${this.username}/accepted`);
      this.users = data;
    },
    openProfile(username){
      router.push({name: 'profile', params: {username: username}});
    },
    searchUsers(){
      this.$refs.list.ej2Instances.dataSource = new DataManager(this.users).executeLocal(
          new Query().search(this.search, ['username', 'location', 'gender', 'birthday'], 'contains', true)
              .sortBy(this.sort.toLowerCase(), this.sortOrder));
      this.totalRecordsCount = this.$refs.list.ej2Instances.dataSource.length;
      this.$refs.list.ej2Instances.dataSource = new DataManager(this.users).executeLocal(
          new Query().search(this.search, ['username', 'location', 'gender', 'birthday'], 'contains', true)
              .sortBy(this.sort.toLowerCase(), this.sortOrder).range(0, 20));
      this.$refs.pager.goToPage(1);
    },
    changePage(e){
      this.$refs.list.ej2Instances.dataSource = new DataManager(this.users).executeLocal(
          new Query().search(this.search, ['username', 'location', 'gender', 'birthday'], 'contains', true)
              .sortBy(this.sort.toLowerCase(), this.sortOrder).range((e.currentPage - 1)  * 20, e.currentPage * 20));
      window.scroll({top: 0, behavior: 'smooth'});
    },
    changeSortOrder(){
      this.sortOrder = this.sortOrder === 'ascending' ? 'descending' : 'ascending';
      this.searchUsers();
    },
    userSelected(e){
      this.$emit('user-selected', e.data);
    }
  },
}
</script>

<style>
#usersContainer .e-listview .e-list-item {
  width: 250px;
  max-height: 250px;
  float: left;
  margin: 1% 1% 0;
}

#usersContainer img {
  max-width: 100px;
  max-height: 100px;
  width: inherit;
}

#usersContainer .e-list-wrapper {
  border: 2px solid darkgray;
}

</style>