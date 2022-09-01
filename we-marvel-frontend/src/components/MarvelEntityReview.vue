<template>
<div>
  <div class="row">
    <div class="col">
      <div class="row mb-3">
        <div v-if="!marvelEntity" class="col-6">
          <h2>Select series, comic or character to review</h2>
          <ejs-combobox :value="review.type"
                        v-model="review.type"
                        :fields="{text: 'text', value: 'value'}"
                        :dataSource="types"
                        :floatLabelType="'Auto'"
                        placeholder="Review for*"
                        ref="typeCombo"
                        :change="typeChanged"
                        required>
          </ejs-combobox>
          <ejs-grid ref="seriesGrid" :dataSource='entities' :enableInfiniteScrolling="true"
                    height="323" :toolbar="toolbar"
                    :rowSelected="rowSelected" :rowDeselected="rowDeselected">
            <e-columns>
              <e-column field="thumbnail" headerText="Image" width="70" textAlign="Center" :template="'imageTemplate'"></e-column>
              <e-column field='title' headerText='Title' textAlign='Center' width="150"></e-column>
            </e-columns>
            <template v-slot:imageTemplate="{data}">
              <img style="box-shadow: 0px 0px 10px 1px black"
                   :src="data.thumbnail"
                   :alt="data.title" :title="data.title"/>
            </template>
          </ejs-grid>
        </div>
        <div class="col align-self-center mt-5">
          <div class="row">
            <div class="col">
              <div :class="[selectedEntity ? 'e-input-group e-float-input' : 'e-input-group e-float-input e-disabled']">
                <input type="number" min="0" :disabled="!selectedEntity" max="10" v-model="review.rating" />
                <label class="e-float-text e-label">Rating</label>
                <div class="align-self-center">
                  <span>/10</span>
                </div>
              </div>
            </div>
            <div class="col">
              <ejs-combobox id="recCombo" :value="review.recommendation"
                            v-model="review.recommendation"
                            :fields="{text: 'text', value: 'value'}"
                            :dataSource="recommendations"
                            :floatLabelType="'Auto'"
                            placeholder="Recommendation*"
                            ref="recommendationCombo"
                            :enabled="selectedEntity"
                            :disabled="!selectedEntity"
                            :readonly="!selectedEntity"
                            required>
              </ejs-combobox>
            </div>
          </div>
          <div class="row">
            <div class="col">
              <ejs-textbox :value="review.text"
                           v-model="review.text"
                           :floatLabelType="'Auto'"
                           placeholder="Review*"
                           ref="reviewTextBox"
                           :disabled="!selectedEntity"
                           :enabled="selectedEntity"
                           :readonly="!selectedEntity"
                           :multiline="true"
                           maxLength="3000"
                           required>
              </ejs-textbox>
            </div>
          </div>
          <div class="row mt-2">
            <div class="col">
              <ejs-button :disabled="!isReviewValid()"
                          isPrimary="true"
                          @click="submitReview"
                          :content="'Submit Review'">
              </ejs-button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import {ComboBoxComponent} from "@syncfusion/ej2-vue-dropdowns";
import {
  ColumnDirective,
  ColumnsDirective,
  GridComponent,
  InfiniteScroll,
  Toolbar
} from "@syncfusion/ej2-vue-grids";
import axios from "axios";
import {auth} from "@/firebaseConfig";
import {TextBoxComponent} from "@syncfusion/ej2-vue-inputs";
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import {ToastUtility} from "@syncfusion/ej2-vue-notifications";

export default {
  name: "MarvelEntityReview",
  props: {
    marvelEntity: {
      type: Object,
    }
  },
  components: {
    "ejs-combobox":ComboBoxComponent,
    'ejs-grid': GridComponent,
    'e-columns': ColumnsDirective,
    'e-column': ColumnDirective,
    'ejs-textbox': TextBoxComponent,
    'ejs-button': ButtonComponent
  },
  data(){
    return {
      types: [
        {value: '', text: ''},
        {value: 'SERIES', text: 'Series'},
        {value: 'COMIC', text: 'Comic'},
        {value: 'CHARACTER', text: 'Character'},
      ],
      recommendations: [
        {value: '', text: ''},
        {value: 'RECOMMENDED', text: 'Recommended'},
        {value: 'NOT_RECOMMENDED', text: 'Not Recommended'},
        {value: 'MIXED_FEELINGS', text: 'Mixed Feelings'},
      ],
      entities: [],
      selectedEntity: null,
      toolbar: ['Search'],
      review: {
        marvelEntityId: '',
        type: '',
        rating: '',
        recommendation: '',
        text: '',
      },
    }
  },
  mounted() {
    if(this.marvelEntity) {
      this.selectedEntity = this.marvelEntity;
      this.review.marvelEntityId = this.marvelEntity.id;
      this.review.type = this.marvelEntity.type?.toUpperCase();
      return;
    }
    this.$nextTick(() => this.disableCombo());
  },
  methods: {
    disableCombo(){
      document.getElementById('recCombo')?.parentElement?.classList.add('e-disabled');
    },
    enableCombo(){
      document.getElementById('recCombo')?.parentElement?.classList.remove('e-disabled');
    },
    async getEntities(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/comicProgress/${this.review.type.toLowerCase()}`);
      this.entities = data;
      for(let e of this.entities){
        if(e.name) e.title = e.name;
        if(e.comicId) e.id = e.comicId;
        if(e.comicTitle) e.title = e.comicTitle;
        if(e.comicThumbnail) e.thumbnail = e.comicThumbnail;
        if(e.firstRating) e.rating = e.firstRating;
        const lastDot = e.thumbnail.lastIndexOf(".");
        e.thumbnail = e.thumbnail.substring(0, lastDot) + "/portrait_small" + e.thumbnail.substring(lastDot);
      }
    },
    isReviewValid(){
      return this.review.rating && this.review.recommendation && this.review.text;
    },
    async submitReview(){
      if(!this.isReviewValid()) return;
      const {data} = await axios.post(`${process.env.VUE_APP_BACKEND}/review`, this.review);
      data.ownerUsername = auth.currentUser.displayName;
      data.ownerImageUrl = auth.currentUser.photoURL;
      data.marvelEntityTitle = this.selectedEntity.title;
      data.marvelEntityThumbnail = this.selectedEntity.thumbnail;
      this.$emit('review-submitted', data);
      this.selectedEntity = null;
      this.review = {
        marvelEntityId: '',
        type: '',
        rating: '',
        recommendation: '',
        text: '',
      };
      ToastUtility.show({
        title: 'Review Submitted',
        content: 'Review submitted successfully!',
        position: {X: 'Right', Y: 'Top'},
        cssClass: 'e-toast-success',
        timeOut: 5000,
        extendedTimeout: 5000,
      });
    },
    typeChanged(){
      if(!this.review.type) return;
      this.getEntities();
    },
    rowSelected(args){
      this.enableCombo();
      this.selectedEntity = args.data;
      this.review.rating = args.data.rating;
      this.review.marvelEntityId = args.data.id;
    },
    rowDeselected(args){
      this.selectedEntity = null;
      this.disableCombo();
      this.review.rating = '';
      this.review.recommendation = '';
      this.review.text = '';
      this.review.marvelEntityId = '';
    },
  },
  provide: {
    grid: [InfiniteScroll, Toolbar],
  }
}
</script>

<style scoped>

</style>