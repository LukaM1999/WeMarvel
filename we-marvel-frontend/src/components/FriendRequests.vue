<template>
<div id="friendRequestsContainer">
  <div class="row">
    <div class="col">
      <ejs-listview v-if="myRequests.length > 0" :cssClass="'e-list-template'" :dataSource="myRequests"
                    :template="'userTemplate'" :fields="fields"
                    :headerTemplate="'headerTemplate'" :showHeader="true" ref="myRequestsList">
        <template v-slot:headerTemplate="{}">
          <div class="row">
            <div class="col align-self-center">
              <div class="e-list-header-text">
                <span class="e-list-header-text-content">Sent</span>
              </div>
            </div>
            <div class="col align-self-center">
              <input type="search" @keyup.enter="searchMyRequests" @click.stop v-model="myRequestsSearch" placeholder="Search..." class="e-input" />
            </div>
            <div class="col align-self-center">
              <label for="sort" class="e-label">Sort by</label>
            </div>
            <div class="col align-self-center" style="width: 50%; justify-self: left" @click.stop>
              <ejs-combobox id="sortBy" :value="myRequestsSort"
                            v-model="myRequestsSort"
                            :dataSource="sortOptions"
                            @change="searchMyRequests">

              </ejs-combobox>
            </div>
            <div class="col align-self-center" @click.stop>
              <span :class="[myRequestsSortOrder === 'ascending' ?  'e-icons e-sort-up' : 'e-icons e-sort-down']"
                    @click="changeMyRequestsSortOrder" :title="myRequestsSortOrder === 'ascending' ? 'Ascending' : 'Descending'"
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
                    <a class="custom-link" :href="`/profile/${data.username}`"
                       @click.prevent="openProfile(data.username)">
                      {{data.username}}
                    </a>
                  </div>
                </div>
              </div>
              <div class="e-card-content mt-2">
                <a :href="`/profile/${data.username}`" style="max-width: inherit;"
                   @click.prevent="openProfile(data.username)">
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
              <div class="row">
                <div class="col d-flex text-nowrap">
                  <b class="label">Sent at:</b>
                </div>
                <div class="col">
                  {{data.sentAt ? data.sentAt : 'Unknown'}}
                </div>
                <div class="row">
                  <div class="col">
                    <ejs-button iconCss="e-icons e-close"
                                :cssClass="'e-primary'"
                                @click="declineRequest(data.id)">Cancel</ejs-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </template>
      </ejs-listview>
      <h1 v-if="myRequests.length === 0">No sent requests</h1>
      <ejs-pager v-if="myRequests.length > 0" ref="myRequestsPager" :totalRecordsCount="myRequestsCount" :pageSize="20"
                 :pageCount="5" :click="changeMyRequestsPage"></ejs-pager>
    </div>
    <div class="col">
      <ejs-listview v-if="othersRequests.length > 0" :cssClass="'e-list-template'" :dataSource="othersRequests"
                    :template="'userTemplate'" :fields="fields"
                    :headerTemplate="'headerTemplate'" :showHeader="true" ref="othersRequestsList">
        <template v-slot:headerTemplate="{}">
          <div class="row">
            <div class="col align-self-center">
              <div class="e-list-header-text">
                <span class="e-list-header-text-content">Received</span>
              </div>
            </div>
            <div class="col align-self-center">
              <input type="search" @keyup.enter="searchOthersRequests" @click.stop v-model="othersRequestsSearch" placeholder="Search..." class="e-input" />
            </div>
            <div class="col align-self-center">
              <label for="sort" class="e-label">Sort by</label>
            </div>
            <div class="col align-self-center" style="width: 50%; justify-self: left" @click.stop>
              <ejs-combobox id="sortBy" :value="othersRequestsSort"
                            v-model="othersRequestsSort"
                            :dataSource="sortOptions"
                            @change="searchOthersRequests">

              </ejs-combobox>
            </div>
            <div class="col align-self-center" @click.stop>
              <span :class="[othersRequestsSortOrder === 'ascending' ?  'e-icons e-sort-up' : 'e-icons e-sort-down']"
                    @click="changeOthersRequestsSortOrder" :title="othersRequestsSortOrder === 'ascending' ? 'Ascending' : 'Descending'"
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
                    <a class="custom-link" :href="`/profile/${data.username}`"
                       @click.prevent="openProfile(data.username)">
                      {{data.username}}
                    </a>
                  </div>
                </div>
              </div>
              <div class="e-card-content mt-2">
                <a :href="`/profile/${data.username}`" style="max-width: inherit;"
                   @click.prevent="openProfile(data.username)">
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
              <div class="row">
                <div class="col d-flex text-nowrap">
                  <b class="label">Sent at:</b>
                </div>
                <div class="col">
                  {{data.sentAt ? data.sentAt : 'Unknown'}}
                </div>
              </div>
              <div class="row mt-2 mb-1">
                <div class="col">
                  <ejs-button iconCss="e-icons e-plus-2"
                              :cssClass="'e-primary'"
                              @click="acceptRequest(data)">Accept</ejs-button>
                </div>
                <div class="col">
                  <ejs-button iconCss="e-icons e-close"
                              :cssClass="'e-primary'"
                              @click="declineRequest(data.id)">Decline</ejs-button>
                </div>
              </div>
            </div>
          </div>
        </template>
      </ejs-listview>
      <h1 v-if="othersRequests.length === 0">No received requests</h1>
      <ejs-pager v-if="othersRequests.length > 0" ref="othersRequestsPager" :totalRecordsCount="othersRequestsCount" :pageSize="20"
                 :pageCount="5" :click="changeOthersRequestsPage"></ejs-pager>
    </div>
  </div>
</div>

</template>

<script>

import {ListViewComponent} from "@syncfusion/ej2-vue-lists";
import {PagerComponent} from "@syncfusion/ej2-vue-grids";
import {ComboBoxComponent} from "@syncfusion/ej2-vue-dropdowns";
import {capitalize} from "eslint-plugin-vue/lib/utils/casing";
import axios from "axios";
import {auth} from "@/firebaseServices/firebaseConfig";
import {DataManager, Query} from "@syncfusion/ej2-data";
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import {ToastUtility} from "@syncfusion/ej2-vue-notifications";
import {router, store} from "@/main";

export default {
  name: "FriendRequests",
  components: {
    'ejs-listview': ListViewComponent,
    'ejs-pager': PagerComponent,
    'ejs-combobox': ComboBoxComponent,
    'ejs-button': ButtonComponent
  },
  data(){
    return {
      myRequests: [],
      othersRequests: [],
      capitalize,
      fields: {
        id: 'id',
        username: 'username',
        location: 'location',
        gender: 'gender',
        birthday: 'birthday',
        sentAt: 'sentAt',
      },
      myRequestsCount: 0,
      othersRequestsCount: 0,
      myRequestsSearch: '',
      othersRequestsSearch: '',
      myRequestsSort: 'username',
      othersRequestsSort: 'username',
      myRequestsSortOrder: 'ascending',
      othersRequestsSortOrder: 'ascending',
      sortOptions: [
        {text: 'Username', value: 'username'},
        {text: 'Location', value: 'location'},
        {text: 'Gender', value: 'gender'},
        {text: 'Birthday', value: 'birthday'},
        {text: 'Sent at', value: 'sentAt'},
      ]
    }
  },
  async mounted() {
    await this.getPendingRequests();
    if(this.myRequests.length > 0){
      this.$refs.myRequestsList.ej2Instances.dataSource = new DataManager(this.myRequests).executeLocal(
          new Query().search(this.myRequestsSearch, ['username', 'location', 'gender', 'birthday', 'sentAt'], 'contains', true).
          sortBy(this.myRequestsSort, this.myRequestsSortOrder).range(0, 20));
      this.myRequestsCount = this.myRequests.length;
    }
    if(this.othersRequests.length === 0) return;
    this.$refs.othersRequestsList.ej2Instances.dataSource = new DataManager(this.othersRequests).executeLocal(
        new Query().search(this.othersRequestsSearch, ['username', 'location', 'gender', 'birthday', 'sentAt'], 'contains', true).
        sortBy(this.othersRequestsSort, this.othersRequestsSortOrder).range(0, 20));
    this.othersRequestsCount = this.othersRequests.length;
  },
  methods: {
    async getPendingRequests(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/friend/pending`);
      this.myRequests = data.filter(r => r.senderUsername === auth.currentUser.displayName);
      this.othersRequests = data.filter(r => r.senderUsername !== auth.currentUser.displayName);
    },
    openProfile(username){
      router.push({name: 'profile', params: {username: username}});
    },
    searchMyRequests(){
      this.$refs.myRequestsList.ej2Instances.dataSource = new DataManager(this.myRequests).executeLocal(
          new Query().search(this.myRequestsSearch, ['username', 'location', 'gender', 'birthday', 'sentAt'], 'contains', true)
              .sortBy(this.myRequestsSort, this.myRequestsSortOrder));
      this.myRequestsCount = this.$refs.myRequestsList.ej2Instances.dataSource.length;
      this.$refs.myRequestsList.ej2Instances.dataSource = new DataManager(this.myRequests).executeLocal(
          new Query().search(this.myRequestsSearch, ['username', 'location', 'gender', 'birthday', 'sentAt'], 'contains', true)
              .sortBy(this.myRequestsSort, this.myRequestsSortOrder));
      this.$refs.myRequestsPager.goToPage(1);
    },
    searchOthersRequests(){
      this.$refs.othersRequestsList.ej2Instances.dataSource = new DataManager(this.othersRequests).executeLocal(
          new Query().search(this.othersRequestsSearch, ['username', 'location', 'gender', 'birthday', 'sentAt'], 'contains', true)
              .sortBy(this.othersRequestsSort, this.othersRequestsSortOrder));
      this.othersRequestsCount = this.$refs.othersRequestsList.ej2Instances.dataSource.length;
      this.$refs.othersRequestsList.ej2Instances.dataSource = new DataManager(this.othersRequests).executeLocal(
          new Query().search(this.othersRequestsSearch, ['username', 'location', 'gender', 'birthday', 'sentAt'], 'contains', true)
              .sortBy(this.othersRequestsSort, this.othersRequestsSortOrder));
      this.$refs.othersRequestsPager.goToPage(1);
    },
    changeMyRequestsPage(e){
      this.$refs.myRequestsList.ej2Instances.dataSource = new DataManager(this.myRequests).executeLocal(
          new Query().search(this.myRequestsSearch, ['username', 'location', 'gender', 'birthday', 'sentAt'], 'contains', true)
              .sortBy(this.myRequestsSort, this.myRequestsSortOrder).range((e.currentPage - 1)  * 20, e.currentPage * 20));
      window.scroll({top: 0, behavior: 'smooth'});
    },
    changeOthersRequestsPage(e){
      this.$refs.othersRequestsList.ej2Instances.dataSource = new DataManager(this.othersRequests).executeLocal(
          new Query().search(this.othersRequestsSearch, ['username', 'location', 'gender', 'birthday', 'sentAt'], 'contains', true)
              .sortBy(this.othersRequestsSort, this.othersRequestsSortOrder).range((e.currentPage - 1)  * 20, e.currentPage * 20));
      window.scroll({top: 0, behavior: 'smooth'});
    },
    changeMyRequestsSortOrder(){
      this.myRequestsSortOrder = this.myRequestsSortOrder === 'ascending' ? 'descending' : 'ascending';
      this.searchMyRequests();
    },
    changeOthersRequestsSortOrder(){
      this.othersRequestsSortOrder = this.othersRequestsSortOrder === 'ascending' ? 'descending' : 'ascending';
      this.searchOthersRequests();
    },
    async acceptRequest(request){
      await axios.patch(`${process.env.VUE_APP_BACKEND}/friend/${request.id}`);
      ToastUtility.show({
        cssClass: 'e-toast-success',
        title: 'Request accepted',
        content: 'Friend request successfully accepted',
        position: {X: document.body.offsetWidth - 360, Y: 80},
        showCloseButton: true,
        timeOut: 5000,
        extendedTimeout: 5000,
      });
      this.othersRequests = this.othersRequests.filter(r => r.id !== request.id);
      await axios.post(`${process.env.VUE_APP_BACKEND}/notification/friend`, {
        type: 'accepted_friend_request',
        recipientId: request.senderId,
        recipientUsername: request.senderUsername,
        socketId: store.getters.socketId,
      });
    },
    async declineRequest(id){
      await axios.delete(`${process.env.VUE_APP_BACKEND}/friend/${id}`);
      ToastUtility.show({
        cssClass: 'e-toast-success',
        title: 'Friend request cancelled',
        content: 'Friend request successfully cancelled',
        position: {X: document.body.offsetWidth - 360, Y: 80},
        showCloseButton: true,
        timeOut: 5000,
        extendedTimeout: 5000,
      });
      this.othersRequests = this.othersRequests.filter(r => r.id !== id);
      this.myRequests = this.myRequests.filter(r => r.id !== id);
    }
  }
}
</script>

<style>
#friendRequestsContainer .e-listview .e-list-item {
  max-width: 300px;
  max-height: 290px;
  float: left;
  margin: 1% 1% 0;
}

#friendRequestsContainer img {
  max-width: 100px;
  max-height: 100px;
  width: inherit;
}

#friendRequestsContainer .e-list-wrapper {
  border: 2px solid darkgray;
}
</style>