<template>
<div id="comicProgressContainer">
  <ComicProgressForm ref="comicProgress" v-if="showForm" :key="comicProgressKey" :comic-progress="selectedRow"
  @comic-progress-created="comicProgressCreated"/>
  <ejs-grid :dataSource='comics' :allowPaging='true' :pageSettings="pageSettings"
            :editSettings='editSettings' :toolbar="authorized ? toolbar : ''" height='273px'
            :allowFiltering='true' :filterSettings="filterSettings" :allowSorting="true"
            :beginEdit="beforeEdit" :beforeDelete="beforeDelete" :actionBegin="actionBegin"
            :allowResizing="true">
    <e-columns>
      <e-column field="index" :allowFiltering="false" :allowSorting="false" headerText="#" width="40" textAlign="Center" :template="'indexTemplate'"></e-column>
      <e-column field="comicUrl" :allowFiltering="false" :allowSorting="false" headerText="Image" width="70" textAlign="Center" :template="'imageTemplate'"></e-column>
      <e-column field='comicTitle' headerText='Title' textAlign='Center' width="150"></e-column>
      <e-column field='firstRating' headerText='Rating' textAlign="Center" width=80></e-column>
      <e-column field='firstPagesRead' headerText='Pages read' textAlign="Center" width=100></e-column>
      <e-column field='firstStatus' :filter="{type: 'CheckBox'}" textAlign="Center" headerText='Status' width="100" :template="'statusTemplate'"></e-column>
    </e-columns>
    <template v-slot:imageTemplate="{data}">
      <a :href="data.comicUrl" target="_blank" style="max-width: inherit;">
        <img style="box-shadow: 0px 0px 10px 1px black"
             :src="data.comicThumbnail"
             :alt="data.comicTitle" :title="data.comicTitle"/>
      </a>
    </template>
    <template v-slot:indexTemplate="{data}">
      {{getRowIndex(data)}}
    </template>
    <template v-slot:statusTemplate="{data}">
      {{formatStatus(data.firstStatus)}}
    </template>
  </ejs-grid>
</div>
</template>

<script>
import {
  ColumnDirective,
  ColumnsDirective,
  GridComponent,
  Page,
  Toolbar,
  Edit,
  Filter,
  Sort, Resize
} from "@syncfusion/ej2-vue-grids";
import axios from "axios";
import {auth} from "@/firebaseConfig";
import {DialogUtility} from "@syncfusion/ej2-vue-popups";
import ComicProgressForm from "@/components/ComicProgressForm";

export default {
  name: "ComicProgress",
  components: {
    ComicProgressForm,
    'ejs-grid': GridComponent,
    'e-columns': ColumnsDirective,
    'e-column': ColumnDirective,
  },
  props: {
    authorized: {
      type: Boolean,
      default: false,
      required: true,
    },
    username: {
      type: String,
      default: "",
      required: true,
    },
  },
  data(){
    return {
      comics: [],
      toolbar: ['Add', 'Edit', 'Delete'],
      pageSettings: {pageCount: 5, pageSize: 20, pageSizes: [10, 20, 50, 100]},
      filterSettings: {type: 'Menu'},
      editSettings: {
        allowEditing: true,
        allowAdding: true,
        allowDeleting: true,
        mode: 'Dialog',
      },
      selectedRow: {},
      showForm: false,
      comicProgressKey: 0,
      isAuthorized: false,
    }
  },
  async mounted() {
    await this.getUserComics();
  },
  methods: {
    async getUserComics () {
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/comicProgress/user/${this.username}`);
      this.comics = data;
      for (let c of this.comics){
        if(!c.comicThumbnail) continue;
        const lastDot = c.comicThumbnail.lastIndexOf(".");
        c.comicThumbnail = c.comicThumbnail.substring(0, lastDot) + "/portrait_small" + c.comicThumbnail.substring(lastDot);
      }
    },
    getRowIndex(data){
      return parseInt(data.index) + 1;
    },
    formatStatus(status){
      let lowerStatus = status.toLowerCase();
      return lowerStatus.charAt(0).toUpperCase() + lowerStatus.slice(1)
    },
    beforeEdit(e){
      e.cancel = true;
      if(!this.authorized) return;
      this.showForm = false;
      this.comicProgressKey++;
      this.selectedRow = e.rowData;
      this.selectedRow.title = e.rowData.comicTitle;
      this.selectedRow.comicId = e.rowData.comicId;
      this.selectedRow.thumbnail = e.rowData.comicThumbnail;
      this.selectedRow.pagesRead = e.rowData.firstPagesRead;
      this.selectedRow.rating = e.rowData.firstRating;
      this.selectedRow.status = e.rowData.firstStatus;
      this.selectedRow.pageCount = e.rowData.comicPages;
      this.showForm = true;
      this.$nextTick(() => {
        document.getElementById('editContainer').scrollIntoView({behavior: 'smooth'});
      })
    },
    beforeDelete(e){
      DialogUtility.alert({
        content: 'Are you sure you want to delete this comic?',
        okButton: {
          click: () => {
            e.cancel = false;
            this.deleteComic(e.rowData.comicId);
          }
        },
        cancelButton: {
          click: () => {
            e.cancel = true;
          }
        }
      });
    },
    actionBegin(e){
      if(e.requestType === 'delete'){
        e.cancel = true;
        let dialog = DialogUtility.confirm({
          title: 'Delete comic',
          showCloseIcon: true,
          closeOnEscape: true,
          position: { X: 'center', Y: 'center' },
          content: 'Are you sure you want to delete this comic?',
          okButton: {
            text: 'Yes',
            click: async () => {
              await this.deleteComic(e.data[0]?.comicId);
              e.cancel = false;
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
      }
      if(e.requestType === 'add'){
        e.cancel = true;
        this.showForm = false;
        this.selectedRow = {};
        this.comicProgressKey++;
        this.showForm = true
        this.$nextTick(() => {
          document.getElementById('addContainer').scrollIntoView({behavior: 'smooth'});
        })
      }
    },
    async comicProgressCreated(){
      await this.getUserComics();
      this.showForm = false;
    },
    async deleteComic(comicId) {
      await axios.delete(`${process.env.VUE_APP_BACKEND}/comicProgress/${comicId}`)
        .then(() => {
          this.getUserComics();
        }).catch(err => {
        console.log(err);
      })
    }
  },
  provide: {
    grid: [Page, Edit, Toolbar, Filter, Sort, Resize]
  },
}
</script>

<style>
#comicProgressContainer .e-toolbar {
  background: #e54e4e;
}

#comicProgressContainer .e-grid .e-gridheader {
  background: #e54e4e;
}

#comicProgressContainer .e-grid .e-headercontent {
  border: none;
}

</style>