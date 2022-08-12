<template>
  <a class="custom-link mt-2 e-bold"
     :href="`/profile/${quotedPost.ownerUsername}`"
     @click.prevent="openProfile(quotedPost.ownerUsername)">
    {{ quotedPost.ownerUsername }} said:</a>
  <div v-if="level > 2" id="toggleQuote" class="row mt-3 mb-3">
    <div class="col">
      <ejs-button @click="changeQuoteVisibility"
                  :iconCss="[hideQuote ? 'e-icons e-eye' : 'e-icons e-eye-slash']"
                  :iconPosition="'Right'"
                  class="e-primary"
                  :content="hideQuote ? 'Show quote' : 'Hide quote'"></ejs-button>
    </div>
  </div>
  <span v-if="!hideQuote" v-html="quotedPost.content"></span>
  <div v-if="quotedPost.quotedPostId" class="quoted-post">
    <QuotedPost v-if="!getPostById(quotedPost.quotedPostId).deleted"
                :quoted-post="getPostById(quotedPost.quotedPostId)"
                :posts="posts" :level="level + 1"/>
    <p v-else>[This post has been deleted]</p>
  </div>
</template>

<script>
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";

export default {
  name: "QuotedPost",
  props: {
    posts: {
      type: Array,
      required: true,
    },
    quotedPost: {
      type: Object,
      required: true,
    },
    level: {
      type: Number,
      required: true,
    },
  },
  components: {
    'ejs-button': ButtonComponent,
  },
  data() {
    return {
      hideQuote: false,
    };
  },
  methods: {
    getPostById(id){
      return this.posts.find(post => post.id === id);
    },
    openProfile(username){
      this.$router.push({name: 'profile', params: {username: username}});
    },
    changeQuoteVisibility(){
      this.hideQuote = !this.hideQuote;
    },
  },
}
</script>

<style>
#toggleQuote .e-icons .e-eye:before, #toggleQuote .e-icons .e-eye-slash:before {
  color: white;
}
</style>