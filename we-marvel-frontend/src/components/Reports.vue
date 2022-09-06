<template>
  <div class="row">
    <div class="col">
      <h1>User reports</h1>
      <ejs-grid ref="userReportsGrid" :dataSource='userReports' :allowPaging='true' :pageSettings="pageSettings"
                :toolbar="toolbar" height='350px'
                :allowFiltering='true' :filterSettings="filterSettings" :allowSorting="true"
                :allowResizing="true" :allowTextWrap="true"
                :toolbarClick="userToolbarClicked"
                :selectionSettings="selectionSettings"
                :rowDataBound='rowDataBound'>
        <e-columns>
          <e-column type='checkbox' width='30' textAlign="Center"></e-column>
          <e-column field="reportingUsername" headerText="From" width="60" textAlign="Center" :template="'reportingTemplate'"></e-column>
          <e-column field="reportedUsername" headerText="Reported user" width="60" textAlign="Center" :template="'reportedTemplate'"></e-column>
          <e-column field="explanation" headerText="Explanation" width="120" textAlign="Center"></e-column>
          <e-column field="resolved" headerText="Resolved?" width="70" textAlign="Center" :template="'resolvedTemplate'"></e-column>
          <e-column field="sentAt" headerText="Date reported" width="70" textAlign="Center"></e-column>
          </e-columns>
        <template v-slot:reportingTemplate="{data}">
          <div class="row">
            <div class="col">
              <div class="row">
                <div class="col">
                  <a style="font-size: 18px;" class="custom-link" :href="`/profile/${data.reportingUsername}`">{{data.reportingUsername}}</a>
                </div>
              </div>
              <div class="row mt-2">
                <div class="col">
                  <a :href="`/profile/${data.reportingUsername}`" style="max-width: inherit;">
                    <img width="100" height="100" style="box-shadow: 0px 0px 10px 1px black"
                         :src="data.reportingUserImageUrl || '/placeholder.jpg'"
                         :alt="data.reportingUsername" :title="data.reportingUsername"/>
                  </a>
                </div>
              </div>
            </div>
          </div>
        </template>
        <template v-slot:reportedTemplate="{data}">
          <div class="row">
            <div class="col">
              <div class="row">
                <div class="col">
                  <a style="font-size: 18px;" class="custom-link" :href="`/profile/${data.reportedUsername}`">{{data.reportedUsername}}</a>
                </div>
              </div>
              <div class="row mt-2">
                <div class="col">
                  <a :href="`/profile/${data.reportedUsername}`" style="max-width: inherit;">
                    <img width="100" height="100" style="box-shadow: 0px 0px 10px 1px black"
                         :src="data.reportedUserImageUrl || '/placeholder.jpg'"
                         :alt="data.reportedUsername" :title="data.reportedUsername"/>
                  </a>
                </div>
              </div>
            </div>
          </div>
        </template>
        <template v-slot:resolvedTemplate="{data}">
          <span style="font-size: 18px;">{{data.resolved ? 'Resolved' : 'Unresolved'}}</span>
        </template>
      </ejs-grid>
    </div>
  </div>
  <div class="row">
    <div class="col">
      <h1>Post reports</h1>
      <ejs-grid ref="postReportsGrid" :dataSource='postReports' :allowPaging='true' :pageSettings="pageSettings"
                :toolbar="toolbar" height='350px'
                :allowFiltering='true' :filterSettings="filterSettings" :allowSorting="true"
                :allowResizing="true" :allowTextWrap="true"
                :toolbarClick="postToolbarClicked"
                :selectionSettings="selectionSettings"
                :rowDataBound='rowDataBound' :detailTemplate="'detailTemplate'">
        <template v-slot:detailTemplate="{data}">
          <span v-html="data.postContent"></span>
        </template>
        <e-columns>
          <e-column type='checkbox' width='30' textAlign="Center"></e-column>
          <e-column field="reportingUsername" headerText="From" width="60" textAlign="Center" :template="'reportingTemplate'"></e-column>
          <e-column field="topicTitle" headerText="Topic" width="70" textAlign="Center" :template="'topicTemplate'"></e-column>
          <e-column field="explanation" headerText="Explanation" width="120" textAlign="Center"></e-column>
          <e-column field="resolved" headerText="Resolved?" width="70" textAlign="Center" :template="'resolvedTemplate'"></e-column>
          <e-column field="sentAt" headerText="Date reported" width="70" textAlign="Center"></e-column>
          <e-column :visible="false" field="postContent"></e-column>
        </e-columns>
        <template v-slot:reportingTemplate="{data}">
          <div class="row">
            <div class="col">
              <div class="row">
                <div class="col">
                  <a style="font-size: 18px;" class="custom-link" :href="`/profile/${data.reportingUsername}`">{{data.reportingUsername}}</a>
                </div>
              </div>
              <div class="row mt-2">
                <div class="col">
                  <a :href="`/profile/${data.reportingUsername}`" style="max-width: inherit;">
                    <img width="100" height="100" style="box-shadow: 0px 0px 10px 1px black"
                         :src="data.reportingUserImageUrl || '/placeholder.jpg'"
                         :alt="data.reportingUsername" :title="data.reportingUsername"/>
                  </a>
                </div>
              </div>
            </div>
          </div>
        </template>
        <template v-slot:topicTemplate="{data}">
          <div class="row">
            <div class="col">
              <a style="font-size: 18px;"
                 class="custom-link"
                 :href="`/forum/${data.boardId}/topic/${data.topicId}`">
                {{data.topicTitle}}</a>
            </div>
          </div>
        </template>
        <template v-slot:resolvedTemplate="{data}">
          <span style="font-size: 18px;">{{data.resolved ? 'Resolved' : 'Unresolved'}}</span>
        </template>
      </ejs-grid>
    </div>
  </div>


</template>


<script>
import {
  ColumnDirective,
  ColumnsDirective, DetailRow,
  Filter,
  GridComponent,
  Page,
  Resize,
  Sort,
  Toolbar
} from "@syncfusion/ej2-vue-grids";
import {capitalize} from "eslint-plugin-vue/lib/utils/casing";
import axios from "axios";
import {ToastUtility} from "@syncfusion/ej2-vue-notifications";

export default {
  name: "Reports",
  components: {
    'ejs-grid': GridComponent,
    'e-columns': ColumnsDirective,
    'e-column': ColumnDirective,
  },
  data(){
    return {
      userReports: [],
      postReports: [],
      toolbar: ['Search', {text: 'Mark as resolved', id: 'resolved', prefixIcon: 'e-check'}],
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
    await this.getReports();
  },
  methods: {
    async getReports(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/report`);
      this.userReports = data.filter(report => !!report.reportedUserId);
      this.postReports = data.filter(report => !!report.postId);
    },
    async userToolbarClicked(e){
      if (e.item.id !== 'resolved') return;
      const selectedIds = this.$refs.userReportsGrid.getSelectedRecords().map(r => r.id);
      if(selectedIds.length === 0) return;
      await axios.patch(`${process.env.VUE_APP_BACKEND}/report/resolved`, {reportIds: selectedIds})
      await this.getReports();
      this.$refs.userReportsGrid.clearSelection();
      ToastUtility.show({
        icon: 'e-icons e-check',
        title: 'Reports resolved',
        content: 'Successfully marked selected reports as resolved',
        cssClass: 'e-toast-success',
        showCloseButton: true,
        position: {X: document.body.offsetWidth - 360, Y: 80},
      });
    },
    async postToolbarClicked(e){
      if (e.item.id !== 'resolved') return;
      const selectedIds = this.$refs.postReportsGrid.getSelectedRecords().map(r => r.id);
      if(selectedIds.length === 0) return;
      await axios.patch(`${process.env.VUE_APP_BACKEND}/report/resolved`, {reportIds: selectedIds})
      await this.getReports();
      this.$refs.postReportsGrid.clearSelection();
      ToastUtility.show({
        icon: 'e-icons e-check',
        title: 'Reports resolved',
        content: 'Successfully marked selected reports as resolved',
        cssClass: 'e-toast-success',
        showCloseButton: true,
        position: {X: document.body.offsetWidth - 360, Y: 80},
      });
    },
    rowDataBound(args){
      if(!args.data.resolved) return
      args.isSelectable = false;
      args.row.style.opacity = 0.5;
    }
  },
  provide: {
    grid: [Page, Toolbar, Filter, Sort, Resize, DetailRow]
  },
}
</script>

<style>
.e-grid .e-icon-grightarrow::before {
  color: black;
}

.e-grid .e-icon-gdownarrow::before {
  color: black;
}
</style>