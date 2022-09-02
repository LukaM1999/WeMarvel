<template>
  <div class="row">
    <div class="col">
      <ejs-grid ref="grid" :dataSource='notifications' :allowPaging='true' :pageSettings="pageSettings"
                :toolbar="toolbar" height='350px'
                :allowFiltering='true' :filterSettings="filterSettings" :allowSorting="true"
                :allowResizing="true" :allowTextWrap="true"
                :toolbarClick="toolbarClicked"
                :selectionSettings="selectionSettings"
                :rowDataBound='rowDataBound'>
        <e-columns>
          <e-column type='checkbox' width='50' textAlign="Center"></e-column>
          <e-column field="senderUsername" headerText="From" width="70" textAlign="Center" :template="'imageTemplate'"></e-column>
          <e-column field='type' headerText='Type' textAlign='Center' width="70" :template="'typeTemplate'"></e-column>
          <e-column field='message' headerText='Content' textAlign="Center" width=120 :template="'messageTemplate'"></e-column>
          <e-column field='read' :filter="{type: 'CheckBox'}" textAlign="Center" headerText='Status' width="100" :template="'statusTemplate'"></e-column>
          <e-column field='receivedAt' textAlign="Center" headerText='Date received' width="80"></e-column>
        </e-columns>
        <template v-slot:imageTemplate="{data}">
          <div class="row">
            <div class="col">
              <div class="row">
                <div class="col">
                  <a style="font-size: 18px;" class="custom-link" :href="`/profile/${data.senderUsername}`">{{data.senderUsername}}</a>
                </div>
              </div>
              <div class="row mt-2">
                <div class="col">
                  <a :href="`/profile/${data.senderUsername}`" style="max-width: inherit;">
                    <img width="100" height="100" style="box-shadow: 0px 0px 10px 1px black"
                         :src="data.senderImageUrl || '/placeholder.jpg'"
                         :alt="data.senderUsername" :title="data.senderUsername"/>
                  </a>
                </div>
              </div>
            </div>
          </div>
        </template>
        <template v-slot:typeTemplate="{data}">
          <span style="font-size: 18px;">{{capitalize(data.type).replaceAll('_', ' ')}}</span>
        </template>
        <template v-slot:messageTemplate="{data}">
          <span style="font-size: 18px;" v-if="data.type !== 'new_topic_post'">{{data.message}}</span>
          <span style="font-size: 18px;" v-else>
            New post in topic <a class="custom-link" :href="`/forum/board/${data.boardId}/topic/${data.topicId}`">{{data.topicTitle}}</a>
          </span>
        </template>
        <template v-slot:statusTemplate="{data}">
          <span style="font-size: 18px;">{{data.read ? 'Read' : 'Not read'}}</span>
        </template>
      </ejs-grid>
    </div>
  </div>

</template>

<script>
import {
  ColumnDirective,
  ColumnsDirective,
  Filter,
  GridComponent,
  Page, Resize,
  Sort,
  Toolbar
} from "@syncfusion/ej2-vue-grids";

import {capitalize} from "eslint-plugin-vue/lib/utils/casing";
import axios from "axios";
import {ToastUtility} from "@syncfusion/ej2-vue-notifications";

export default {
  name: "Notifications",
  components: {
    'ejs-grid': GridComponent,
    'e-columns': ColumnsDirective,
    'e-column': ColumnDirective,
  },
  data(){
    return {
      notifications: [],
      toolbar: ['Search', {text: 'Mark as read', id: 'read', prefixIcon: 'e-check'}],
      pageSettings: {
        pageCount: 5,
        pageSize: 20,
        pageSizes: [10, 20, 50, 100]
      },
      filterSettings: {type: 'Menu'},
      selectedRow: {},
      showForm: false,
      comicProgressKey: 0,
      isAuthorized: false,
      capitalize,
      selectionSettings: { checkboxMode: 'Default'}
    }
  },
  async mounted(){
    await this.getNotifications();
  },
  methods: {
    async getNotifications(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/notification`);
      for(let n of data){
        if(n.type === 'new_topic_post'){
          n.message = 'New post in topic ' + n.topicTitle;
        }
        else if(n.type === 'new_friend_request'){
          n.message = 'New friend request from ' + n.senderUsername;
        }
        else if(n.type === 'accepted_friend_request'){
          n.message = 'Friend request accepted from ' + n.senderUsername;
        }
      }
      this.notifications = data;
    },
    async toolbarClicked(e){
      if (e.item.id !== 'read') return;
      const selectedIds = this.$refs.grid.getSelectedRecords().map(r => r.id);
      if(selectedIds.length === 0) return;
      await axios.patch(`${process.env.VUE_APP_BACKEND}/notification/read`, {toMarkAsRead: selectedIds})
      await this.getNotifications();
      this.$refs.grid.clearSelection();
      ToastUtility.show({
        icon: 'e-icons e-check',
        title: 'Notifications read',
        content: 'Successfully marked selected notifications as read',
        cssClass: 'e-toast-success',
        position: {X: document.body.offsetWidth - 360, Y: 80},
      });
    },
    rowDataBound(args){
      if(args.data.read){
        args.isSelectable = false;
        args.row.style.opacity = 0.5;
      }
    }
  },
  provide: {
    grid: [Page, Toolbar, Filter, Sort, Resize]
  },
}
</script>

<style scoped>

</style>