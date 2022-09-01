<template>
  <div class="mt-3">
    <h1>{{comicProgress.firstStatus ? 'Edit comic progress' : 'Add comic progress'}}</h1>
    <div v-if="!comicProgress.comicId" id="addContainer" class="row justify-content-center">
      <div class="col-5">
        <h2>Select series</h2>
        <ejs-grid ref="seriesGrid" :dataSource='series' :enableInfiniteScrolling="true"
                  :pageSettings="pageSettings" height="323" :toolbar="toolbar"
                  :rowSelected="seriesRowSelected" :rowDeselected="seriesRowDeselected">
          <e-columns>
            <e-column field="seriesThumbnail" headerText="Image" width="70" textAlign="Center" :template="'seriesImageTemplate'"></e-column>
            <e-column field='seriesTitle' headerText='Title' textAlign='Center' width="150"></e-column>
          </e-columns>
          <template v-slot:seriesImageTemplate="{data}">
              <img style="box-shadow: 0px 0px 10px 1px black"
                   :src="data.seriesThumbnail"
                   :alt="data.seriesTitle" :title="data.seriesTitle"/>
          </template>
        </ejs-grid>
      </div>
      <div class="col-5">
        <h2>Select comic*</h2>
        <ejs-grid ref="comicsGrid" :dataSource='filteredComics' :enableInfiniteScrolling="true"
                  :pageSettings="pageSettings" height="323" :toolbar="toolbar"
                  :query="query" :rowSelected="comicsRowSelected" :rowDeselected="comicsRowDeselected">
          <e-columns>
            <e-column field="thumbnail" headerText="Image" width="70" textAlign="Center" :template="'comicImageTemplate'"></e-column>
            <e-column field='title' headerText='Title' textAlign='Center' width="150"></e-column>
          </e-columns>
          <template v-slot:comicImageTemplate="{data}">
            <img style="box-shadow: 0px 0px 10px 1px black"
                 :src="data.thumbnail"
                 :alt="data.title" :title="data.title"/>
          </template>
        </ejs-grid>
      </div>
    </div>
    <div id="editContainer" class="row mt-3 justify-content-center">
      <div class="col-2">
        <ejs-combobox :value="progress.status"
                      v-model="progress.status"
                      :fields="{text: 'text', value: 'value'}"
                      :dataSource="comicStatuses"
                      :floatLabelType="'Auto'"
                      :enabled="progress.comicId"
                      placeholder="Status*"
                      :readonly="!progress.comicId"
                       ref="statusesCombo"
                      :change="statusChanged"
                      required>
        </ejs-combobox>
      </div>
      <div class="col-1">
        <div class="e-control e-lib e-primary">
          <div :class="[progress.comicId && !completed ? 'e-input-group e-float-input' : 'e-input-group e-float-input e-disabled']">
            <input type="number" min="0" :readonly="!progress.comicId || completed" :max="progress ? progress.pageCount : ''" v-model="progress.firstPagesRead" />
            <label class="e-float-text e-label">Pages read</label>
            <div class="align-self-center">
              <span>/{{progress.comicId ? progress.pageCount > 0 ? progress.pageCount : '-' : progress.comicPages > 0 ? progress.comicPages : '-' }}</span>
            </div>
          </div>
        </div>
      </div>
      <div class="col-1">
        <div class="">
          <div :class="[progress.comicId ? 'e-input-group e-float-input' : 'e-input-group e-float-input e-disabled']">
            <input type="number" min="0" :disabled="!progress.comicId" max="10" v-model="progress.firstRating" />
            <label class="e-float-text e-label">Rating</label>
            <div class="align-self-center">
              <span>/10</span>
            </div>
          </div>
        </div>
      </div>
      <div class="col-2">
        <div class="e-control e-lib e-primary">
            <ejs-datepicker :value="progress.firstStartDate" v-model="progress.firstStartDate"
                            :format="'dd.MM.yyyy.'" :floatLabelType="'Auto'" placeholder="Start date"
                            :enabled="progress.comicId && !startDateDisabled"
                            :readonly="!progress.comicId || startDateDisabled" :enableMask="true"
                            :maskPlaceholder="maskPlaceholder">
            </ejs-datepicker>
        </div>
      </div>
      <div class="col-2">
        <div class="e-control e-lib e-primary">
          <ejs-datepicker :value="progress.firstEndDate" v-model="progress.firstEndDate"
                          :format="'dd.MM.yyyy.'" :floatLabelType="'Auto'" placeholder="End date"
                          :enabled="progress.comicId && !endDateDisabled"
                          :readonly="!progress.comicId || endDateDisabled" :enableMask="true"
                          :maskPlaceholder="maskPlaceholder">
          </ejs-datepicker>
        </div>
      </div>
    </div>
    <div class="row mt-3 justify-content-center">
      <div class="col">
        <ejs-button @click="createComicProgress">{{comicProgress.status ? 'Edit comic progress' : 'Add comic progress'}}</ejs-button>
      </div>
    </div>
  </div>
</template>

<script>
import {DialogComponent} from "@syncfusion/ej2-vue-popups";
import {ComboBoxComponent} from "@syncfusion/ej2-vue-dropdowns";
import axios from "axios";
import {Virtualization} from "@syncfusion/ej2-vue-lists";
import {DataManager, Query} from "@syncfusion/ej2-data";
import {
  ColumnDirective,
  ColumnsDirective,
  GridComponent, InfiniteScroll,
  Page, Pager,
  Toolbar,
  VirtualScroll
} from "@syncfusion/ej2-vue-grids";
import {DatePickerComponent, MaskedDateTime} from "@syncfusion/ej2-vue-calendars";
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import moment from "moment";

export default {
  name: "ComicProgressForm",
  components: {
    'ejs-combobox': ComboBoxComponent,
    'ejs-grid': GridComponent,
    'e-columns': ColumnsDirective,
    'e-column': ColumnDirective,
    'ejs-datepicker': DatePickerComponent,
    'ejs-button': ButtonComponent,
  },
  props: {
    comicProgress: {
      type: Object,
      default: () => {}
    }
  },
  data(){
    return {
      progress: {},
      series: [],
      comics: [],
      filteredComics: [],
      comicStatuses: [
        {text: 'Reading', value: 'READING'},
        {text: 'Completed', value: 'COMPLETED'},
        {text: 'On-hold', value: 'ON_HOLD'},
        {text: 'Plan to read', value: 'PLAN_TO_READ'},
        {text: 'Dropped', value: 'DROPPED'}
      ],
      toolbar: ['Search'],
      query: new Query(),
      pageSettings: {pageSize: 8, pageCount: 5},
      maskPlaceholder: {day: 'dd', month: 'mm', year: 'yyyy'},
      completed: false,
      startDateDisabled: false,
      endDateDisabled: false,
    }
  },
  async mounted() {
    this.progress = Object.assign({}, this.comicProgress);
    if(this.progress.seriesId) return;
    this.$refs.seriesGrid.showSpinner();
    await this.getSeries();
    this.$refs.comicsGrid.showSpinner();
    await this.getComics();
  },
  methods: {
    async getSeries(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/series/simple`);
      this.series = data;
      for(let s of this.series){
        const lastDot = s.thumbnail.lastIndexOf(".");
        s.seriesThumbnail = s.thumbnail.substring(0, lastDot) + "/portrait_small" + s.thumbnail.substring(lastDot);
        s.seriesId = s.id;
        s.seriesTitle = s.title;
      }
    },
    async getComics(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/comic?limit=1000000&offset=0`);
      this.comics = data;
      for(let c of this.comics){
        const lastDot = c.thumbnail.lastIndexOf(".");
        c.thumbnail = c.thumbnail.substring(0, lastDot) + "/portrait_small" + c.thumbnail.substring(lastDot);
      }
      this.filteredComics = data;
    },
    async createComicProgress(){
      const comicProgress = {
        comicId: this.progress.comicId,
        firstStatus: this.progress.status,
        firstPagesRead: this.progress.firstPagesRead,
        firstRating: this.progress.firstRating,
        firstStartDate: this.progress.firstStartDate,
        firstEndDate: this.progress.firstEndDate,
      }
      if(comicProgress.firstStartDate) {
        comicProgress.firstStartDate = moment(comicProgress.firstStartDate).format('DD.MM.yyyy.');
      }
      if(comicProgress.firstEndDate) {
        comicProgress.firstEndDate = moment(comicProgress.firstEndDate).format('DD.MM.yyyy.');
      }
      const {data} = await axios.post(`${process.env.VUE_APP_BACKEND}/comicProgress`, comicProgress);
      this.$emit('comic-progress-created');
    },
    statusChanged(e){
      if(e.value === 'COMPLETED'){
        if(this.progress?.pageCount > 0) {
          this.progress.firstPagesRead = this.progress.pageCount;
        }
        this.completed = true;
        this.endDateDisabled = false;
        this.startDateDisabled = false;
        return;
      } else {
        this.progress.firstEndDate = null;
        this.endDateDisabled = true;
      }
      if(e.value === 'PLAN_TO_READ'){
        this.progress.firstPagesRead = 0;
        this.completed = true;
        return;
      }
      this.completed = false;
    },
    seriesRowSelected(e){
      this.filteredComics = this.comics.filter(c => c.seriesId === e.data.seriesId);
    },
    seriesRowDeselected(e){
      this.filteredComics = new DataManager(this.comics);
    },
    comicsRowSelected(e){
      this.progress.comicId = e.data.id;
      this.progress.pageCount = e.data.pageCount;
    },
    comicsRowDeselected(e){
      this.progress.comicId = null;
      this.progress.pageCount = 0;
    }
  },
  provide: {
    grid: [Page, InfiniteScroll, Toolbar],
    datepicker: [MaskedDateTime]
  }
}
</script>

<style scoped>

</style>