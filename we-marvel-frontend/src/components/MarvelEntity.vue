<template>
<div id="entityContainer" style="overflow-x: hidden">
  <h1>{{entity.title}}</h1>
  <p>{{entity.description}}</p>
  <ejs-tab v-if="entity.id" ref="tabs" :selected="tabSelected" class="e-fill">
    <e-tabitems>
      <e-tabitem :header="{text: 'Overview'}" :content="'overviewTemplate'">
        <template v-slot:overviewTemplate="{}">
          <MarvelEntityOverview :entity="entity"/>
        </template>
      </e-tabitem>
      <e-tabitem :header="{text: 'Reviews'}" :content="'reviewsTemplate'">
        <template v-slot:reviewsTemplate="{}">
          <div v-if="myReview" class="row mb-3 mt-3 justify-content-center">
            <h1>My Review</h1>
            <div class="col-10 quoted-post justify-content-center">
              <div class="row mb-3">
                <div class="col d-flex ms-1">
                  <b style="font-size: 18px;"><a class="custom-link"
                                                 :href="`/${myReview.type.toLowerCase()}/${myReview.marvelEntityId}`">
                    {{myReview.marvelEntityTitle}}
                  </a></b>
                </div>
              </div>
              <div style="font-size: 16px;" class="row">
                <div class="col-1">
                  <a :href="`/profile/${myReview.ownerUsername}`">
                    <img :src="myReview.ownerImageUrl || '/placeholder.jpg'"
                         :alt="myReview.ownerUsername"
                         :title="myReview.ownerUsername"
                         width="100" height="100">
                  </a>
                </div>
                <div class="col-1">
                  <div class="row">
                    <div class="col">
                      <a class="custom-link" :href="`/profile/${myReview.ownerUsername}`">
                        {{myReview.ownerUsername}}
                      </a>
                    </div>
                  </div>
                  <div v-if="myReview.rating" class="row">
                    <div class="col">
                      <span>{{myReview.rating}}/10</span>
                    </div>
                  </div>
                  <div class="row">
                    <div class="col">
                      <div class="row">
                        <div class="col">
                          <span style="font-size:20px; color: black !important;" :class="'e-icons ' + icons.get(myReview.recommendation)"></span>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col">
                          <span>{{capitalize(myReview.recommendation.toLowerCase()).replace('_', ' ')}}</span>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="col d-flex ms-2">
                  <span>{{myReview.text}}</span>
                </div>
                <div class="col-1">
                  <div class="row">
                    <div class="col">
                      <span>{{myReview.createdAt}}</span>
                    </div>
                  </div>
                  <div class="row mt-2">
                    <div class="col">
                      <a class="custom-link"
                         :href="`/${myReview.type.toLowerCase()}/${myReview.marvelEntityId}`">
                        <img :src="myReview.marvelEntityThumbnail"
                             :alt="myReview.marvelEntityTitle"
                             :title="myReview.marvelEntityTitle">
                      </a>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row mt-2 justify-content-end">
                <div class="col-2 me-2 d-flex justify-content-end">
                  <ejs-button :iconCss="'e-icons e-delete-1'" :isPrimary="true" @click="deleteReview(myReview)">Delete review</ejs-button>
                </div>
              </div>
            </div>
          </div>
          <Reviews :key="reviewsKey" v-if="entity.id" :entity="entity"
                   :edit-settings="editSettings"
                   :reviews="reviews"
                   :toolbar="toolbar"
                    @review-submitted="reviewSubmitted"/>
        </template>
      </e-tabitem>
      <e-tabitem v-if="entity.type !== 'comic'" :header="{text: 'Comics'}" :content="'comicsTemplate'">
        <template v-slot:comicsTemplate="{}">
          <Comics v-if="comics.length > 0" :comics-prop="comics"/>
          <div v-else class="row mt-3 justify-content-center">
            <h1>No comics found</h1>
          </div>
        </template>
      </e-tabitem>
      <e-tabitem v-else :header="{text: 'Progress'}" :content="'progressTemplate'">
        <template v-slot:progressTemplate="{}">
          <div class="row mb-3">
            <div class="col" v-if="isAuthorized">
              <ComicProgressForm :key="progressKey"
                                 @comic-progress-created="comicProgressCreated"
                                 v-if="comicProgress"
                                 :comic-progress="comicProgress"/>
            </div>
            <div class="col" v-else>
              <div class="row mt-2 mb-3">
                <div class="col">
                  <h1>Sign in to track your progress</h1>
                </div>
              </div>
            </div>
          </div>
        </template>
      </e-tabitem>
      <e-tabitem v-if="entity.type !== 'character'" :header="{text: 'Characters'}" :content="'charactersTemplate'">
        <template v-slot:charactersTemplate="{}">
          <MarvelCharacters v-if="characters.length > 0" :characters-prop="characters"/>
          <div v-else class="row mt-3 justify-content-center">
            <h1>No characters found</h1>
          </div>
        </template>
      </e-tabitem>
      <e-tabitem v-if="entity.type === 'character'" :header="{text: 'Series'}" :content="'seriesTemplate'">
        <template v-slot:seriesTemplate="{}">
          <Series v-if="series.length > 0" :series-prop="series"/>
          <div v-else class="row mt-3 justify-content-center">
            <h1>No series found</h1>
          </div>
        </template>
      </e-tabitem>
    </e-tabitems>
  </ejs-tab>
</div>
</template>

<script>
import {TabComponent, TabItemDirective, TabItemsDirective} from "@syncfusion/ej2-vue-navigations";
import axios from "axios";
import MarvelEntityOverview from "@/components/MarvelEntityOverview";
import Reviews from "@/components/Reviews";
import {getIdTokenResult, onIdTokenChanged} from "firebase/auth";
import {auth} from "@/firebaseConfig";
import {capitalize} from "eslint-plugin-vue/lib/utils/casing";
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import {ToastUtility} from "@syncfusion/ej2-vue-notifications";
import Comics from "@/components/Comics";
import ComicProgress from "@/components/ComicProgress";
import MarvelCharacters from "@/components/MarvelCharacters";
import Series from "@/components/Series";
import ComicProgressForm from "@/components/ComicProgressForm";

export default {
  name: "MarvelEntity",
  components: {
    Comics,
    Reviews,
    MarvelEntityOverview,
    MarvelCharacters,
    Series,
    ComicProgressForm,
    "ejs-tab": TabComponent,
    "e-tabitems": TabItemsDirective,
    "e-tabitem": TabItemDirective,
    "ejs-button": ButtonComponent,
  },
  data(){
    return {
      entity: {},
      username: undefined,
      isAuthorized: false,
      isAdmin: false,
      reviews: [],
      tabs: new Map([
          ['overview', 0],
          ['reviews', 1],
      ]),
      editSettings: {
        allowAdding: false,
        mode: 'Dialog',
      },
      icons: new Map([
        ['RECOMMENDED', 'e-thumbs-up'],
        ['NOT_RECOMMENDED', 'e-thumbs-down-1'],
        ['MIXED_FEELINGS', 'e-intermediate-state'],
      ]),
      toolbar: ['Search'],
      reviewsKey: 0,
      myReview: null,
      capitalize,
      comics: [],
      comicProgress: null,
      characters: [],
      series: [],
      progressKey: 0,
    }
  },
  async mounted(){
    onIdTokenChanged(auth, async (user) => {
      if (user) {
        this.isAuthorized = true;
        this.editSettings = {
          allowAdding: true,
          mode: 'Dialog',
        };
        this.toolbar = ['Search', 'Add'];
        getIdTokenResult(user).then((idTokenResult) => {
          this.isAdmin = idTokenResult.claims.admin;
        });
      } else {
        this.isAuthorized = false;
        this.isAdmin = false;
        this.editSettings = {
          allowAdding: false,
          mode: 'Dialog',
        };
        this.toolbar = ['Search'];
      }
    });
    await this.getEntity();
    if(this.$route.query.tab){
      this.$refs.tabs.select(this.tabs.get(this.$route.query.tab));
    }
  },
  methods: {
    async getEntity(){
      const suffix = this.$route.params.entity === 'comic' ? '/withSeries' : '';
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/${this.$route.params.entity}/${this.$route.params.entityId}${suffix}`);
      this.entity = data;
      if(data.name) this.entity.title = data.name;
      this.entity.type = this.$route.params.entity;
      const lastDot = data.thumbnail.lastIndexOf(".");
      this.entity.thumbnail = data.thumbnail.substring(0, lastDot) + "/portrait_small" + data.thumbnail.substring(lastDot);
      this.entity = {...this.entity};
    },
    async getEntityReviews(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/review/${this.$route.params.entity}/${this.$route.params.entityId}`);
      for(let r of data){
        const lastDot = r.marvelEntityThumbnail.lastIndexOf(".");
        r.marvelEntityThumbnail = r.marvelEntityThumbnail.substring(0, lastDot) + "/portrait_small" + r.marvelEntityThumbnail.substring(lastDot);
      }
      this.reviews = data;
    },
    async getMyReview(){
      if(!this.isAuthorized) return;
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/review/${this.$route.params.entityId}`);
      if(!data){
        this.toolbar = ['Search', 'Add'];
        this.editSettings = {
          allowAdding: true,
          mode: 'Dialog',
        };
        return;
      }
      this.toolbar = ['Search'];
      this.editSettings = {
        allowAdding: false,
        mode: 'Dialog',
      };
      this.myReview = data;
      this.myReview.ownerUsername = auth.currentUser.displayName;
      this.myReview.ownerImageUrl = auth.currentUser.photoURL;
      this.myReview.marvelEntityTitle = this.entity.title;
      this.myReview.marvelEntityThumbnail = this.entity.thumbnail;
    },
    async getComicsInSeries(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/comic/series/${this.entity.id}`);
      for(let c of data){
        const lastDot = c.thumbnail.lastIndexOf(".");
        c.thumbnail = c.thumbnail.substring(0, lastDot) + "/portrait_small" + c.thumbnail.substring(lastDot);
      }
      this.comics = data;
    },
    async getCharactersInSeries(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/character/series/${this.entity.id}`);
      for(let c of data){
        const lastDot = c.thumbnail.lastIndexOf(".");
        c.thumbnail = c.thumbnail.substring(0, lastDot) + "/portrait_small" + c.thumbnail.substring(lastDot);
      }
      this.characters = data;
    },
    async getCharactersInComic(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/character/comic/${this.entity.id}`);
      for(let c of data){
        const lastDot = c.thumbnail.lastIndexOf(".");
        c.thumbnail = c.thumbnail.substring(0, lastDot) + "/portrait_small" + c.thumbnail.substring(lastDot);
      }
      this.characters = data;
    },
    async getComicProgress(){
      if(!this.isAuthorized) return;
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/comicProgress/user/${auth.currentUser?.displayName}/comic/${this.entity.id}`);
      if(!data){
        this.comicProgress = {
          seriesId: this.entity.seriesId,
          comicId: this.entity.id,
          pageCount: this.entity.pageCount,
        }
        return;
      }
      const lastDot = data.comicThumbnail.lastIndexOf(".");
      data.comicThumbnail = data.comicThumbnail.substring(0, lastDot) + "/portrait_small" + data.comicThumbnail.substring(lastDot);
      data.status = data.firstStatus;
      this.comicProgress = data;
    },
    async getComicsWithCharacter(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/comic/character/${this.entity.id}`);
      for(let c of data){
        const lastDot = c.thumbnail.lastIndexOf(".");
        c.thumbnail = c.thumbnail.substring(0, lastDot) + "/portrait_small" + c.thumbnail.substring(lastDot);
      }
      this.comics = data;
    },
    async getSeriesWithCharacter(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/series/character/${this.entity.id}`);
      for(let s of data){
        const lastDot = s.thumbnail.lastIndexOf(".");
        s.thumbnail = s.thumbnail.substring(0, lastDot) + "/portrait_small" + s.thumbnail.substring(lastDot);
      }
      this.series = data;
    },
    comicProgressCreated(){
      this.comicProgress = null;
      this.progressKey++;
      this.getComicProgress();
    },
    tabSelected(e){
      this.$router.push({query: {tab: e.selectedItem.innerText.toLowerCase()}});
      if (e.selectedIndex === 1){
        this.getEntityReviews();
        this.getMyReview();
      }
      if(e.selectedIndex === 2){
        if(this.entity.type === 'series') {
          this.getComicsInSeries();
        }
        else if(this.entity.type === 'character'){
          this.getComicsWithCharacter();
        }
        else if(this.entity.type === 'comic'){
          this.getComicProgress();
          this.username = auth.currentUser?.displayName;
        }
      }
      if(e.selectedIndex === 3){
        if(this.entity.type === 'series') {
          this.getCharactersInSeries();
        }
        else if(this.entity.type === 'character'){
          this.getSeriesWithCharacter();
        }
        else if(this.entity.type === 'comic'){
          this.getCharactersInComic();
        }
      }
    },
    reviewSubmitted(review){
      this.reviews.unshift(review);
      this.myReview = review;
      this.reviewsKey++;
    },
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
      this.myReview = null;
      this.toolbar = ['Search', 'Add'];
      this.editSettings = {
        allowAdding: true,
        mode: 'Dialog',
      };
      this.reviews = this.reviews.filter(r => r.id !== review.id);
    },
  }
}
</script>

<style scoped>

</style>