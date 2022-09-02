<template>
  <div :key="reviewFormKey" v-if="showReviewForm" class="row justify-content-center">
    <div class="col-8">
      <MarvelEntityReview :marvel-entity="entity" @review-submitted="reviewSubmitted"/>
    </div>
  </div>
  <div class="row">
    <div class="col">
      <ejs-grid :key="tableKey" :allowSorting="true"
                :dataSource="reviews"
                :allowPaging="true"
                :pageSettings="{pageSize: 20, pageSizes: [10, 20, 50, 100]}"
                :allowSelection="true"
                :allowFiltering="true"
                :filterSettings="filterSettings"
                :rowTemplate="'rowTemplate'"
                :toolbar="toolbar" ref="grid"
                :editSettings="editSettings"
                :actionBegin="actionBegin">
        <e-columns>
          <e-column field="marvelEntityTitle" headerText="Review" textAlign="Center"></e-column>
          <e-column field="ownerUsername" :visible="false"></e-column>
          <e-column field="type" :visible="false"></e-column>
          <e-column field="recommendation" :visible="false"></e-column>
          <e-column field="rating" :visible="false"></e-column>
          <e-column field="createdAt" :visible="false"></e-column>
        </e-columns>
        <template v-slot:rowTemplate="{data}">
          <tr>
            <td>
              <div class="row">
                <div class="col">
                  <div class="row mb-3">
                    <div class="col d-flex ms-1">
                      <b style="font-size: 18px;"><a class="custom-link"
                                                     :href="`/${data.type.toLowerCase()}/${data.marvelEntityId}`">
                        {{data.marvelEntityTitle}}
                      </a></b>({{capitalize(data.type.toLowerCase())}})  <a class="custom-link"
                                                                            :href="`/${data.type.toLowerCase()}/${data.marvelEntityId}?tab=reviews`">
                      View all reviews
                    </a>
                    </div>
                  </div>
                  <div style="font-size: 16px;" class="row">
                    <div class="col-1">
                      <a :href="`/profile/${data.ownerUsername}`">
                        <img :src="data.ownerImageUrl || '/placeholder.jpg'"
                             :alt="data.ownerUsername"
                             :title="data.ownerUsername"
                             width="100" height="100">
                      </a>
                    </div>
                    <div class="col-1">
                      <div class="row">
                        <div class="col">
                          <a class="custom-link" :href="`/profile/${data.ownerUsername}`">
                            {{data.ownerUsername}}
                          </a>
                        </div>
                      </div>
                      <div v-if="data.rating" class="row">
                        <div class="col">
                          <span>{{data.rating}}/10</span>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col">
                          <div class="row">
                            <div class="col">
                              <span style="font-size:20px; color: black !important;" :class="'e-icons ' + icons.get(data.recommendation)"></span>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col">
                              <span>{{capitalize(data.recommendation.toLowerCase()).replace('_', ' ')}}</span>
                            </div>
                          </div>
                        </div>
                      </div>
                    </div>
                    <div class="col d-flex ms-2">
                      <span>{{data.text}}</span>
                    </div>
                    <div class="col-1">
                      <div class="row">
                        <div class="col">
                          <span>{{data.createdAt}}</span>
                        </div>
                      </div>
                      <div class="row mt-2">
                        <div class="col">
                          <a class="custom-link"
                             :href="`/${data.type.toLowerCase()}/${data.marvelEntityId}`">
                            <img :src="data.marvelEntityThumbnail"
                                 :alt="data.marvelEntityTitle"
                                 :title="data.marvelEntityTitle">
                          </a>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div v-if="isAdmin || username === data.ownerUsername" class="row mt-2 justify-content-end">
                    <div class="col-2 me-2 d-flex justify-content-end">
                      <ejs-button :iconCss="'e-icons e-delete-1'" :isPrimary="true" @click="deleteReview(data)">Delete review</ejs-button>
                    </div>
                  </div>
                </div>
              </div>
            </td>
          </tr>
        </template>
      </ejs-grid>
    </div>
  </div>
</template>

<script>
import {
  ColumnDirective,
  ColumnsDirective, Edit,
  Filter,
  GridComponent, Page, Resize,
  Search,
  Sort,
  Toolbar
} from "@syncfusion/ej2-vue-grids";
import {capitalize} from "eslint-plugin-vue/lib/utils/casing";
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import axios from "axios";
import {ToastUtility} from "@syncfusion/ej2-vue-notifications";
import MarvelEntityReview from "@/components/MarvelEntityReview";
import {onIdTokenChanged, getIdTokenResult} from "firebase/auth";
import {auth} from "@/firebaseServices/firebaseConfig";

export default {
  name: "Reviews",
  props: {
    reviews: {
      type: Array,
      required: true,
    },
    toolbar: {
      type: Array,
      required: true,
    },
    editSettings: {
      type: Object,
      required: true,
    },
    entity: {
      type: Object,
    }
  },
  components: {
    MarvelEntityReview,
    'ejs-grid' : GridComponent,
    'e-columns' : ColumnsDirective,
    'e-column' : ColumnDirective,
    'ejs-button' : ButtonComponent,
  },
  emits: ['reviewSubmitted'],
  data(){
    return {
      tableKey: 0,
      filterSettings: {
        type: 'Menu',
      },
      capitalize,
      icons: new Map([
        ['RECOMMENDED', 'e-thumbs-up'],
        ['NOT_RECOMMENDED', 'e-thumbs-down-1'],
        ['MIXED_FEELINGS', 'e-intermediate-state'],
      ]),
      showReviewForm: false,
      isAdmin: false,
      username: '',
      reviewFormKey: 0,
    }
  },
  mounted() {
    onIdTokenChanged(auth, (user) => {
      this.username = user?.displayName;
      if (user) {
        getIdTokenResult(user).then((idTokenResult) => {
          this.isAdmin = idTokenResult.claims.admin;
        });
      }
    });
  },
  methods: {
    async deleteReview(review){
      await axios.delete(`${process.env.VUE_APP_BACKEND}/review/${review.id}`);
      ToastUtility.show({
        title: 'Review deleted',
        content: 'Review deleted successfully',
        position: {
          X: 'Right',
          Y: 'Top'
        },
        showCloseButton: true,
        cssClass: 'e-toast-success',
        timeOut: 5000,
        extendedTimeout: 5000,
      });
    },
    actionBegin(e){
      if(e.requestType !== 'add') return;
      e.cancel = true;
      this.showReviewForm = false;
      this.reviewFormKey++;
      this.showReviewForm = true;
    },
    reviewSubmitted(review){
      this.showReviewForm = false;
      this.$emit('review-submitted', review);
    }
  },
  provide: {
    grid: [Sort, Toolbar, Page, Search, Filter, Resize, Edit]
  },
}
</script>

<style scoped>

</style>