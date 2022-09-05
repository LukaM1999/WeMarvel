<template>
  <div id="topicContainer">
    <h1>{{ topic?.title }}</h1>
    <ejs-button @click="togglePostForm" class="mb-3">{{showNewPostForm ? 'Cancel' : 'New post'}}</ejs-button>
    <div id="quoteContent" class="row mt-5 justify-content-center" v-if="quotedPost">
      <h3>Quoted post</h3>
      <div class="col-8 quoted-post">
        <a class="custom-link mt-2 e-bold"
           :href="`/profile/${quotedPost.ownerUsername}`"
           @click.prevent="openProfile(quotedPost.ownerUsername)">
          {{ quotedPost.ownerUsername }} said:</a>
        <div class="row">
          <div class="col">
            <span v-html="quotedPost.content"></span>
          </div>
        </div>
        <div v-if="quotedPost.quotedPostId" class="quoted-post">
          <QuotedPost :quoted-post="getPostById(quotedPost.quotedPostId)"
                      :posts="topic.posts" :level="1"/>
        </div>
      </div>
    </div>
    <div id="newPostContainer" v-if="showNewPostForm" class="row mb-5">
      <div class="col">
        <h3>{{postToEdit ? 'Edit post' : 'New post'}}</h3>
        <RichTextEditor :initialValue="rteValue" @value-changed="updateRteValue"/>
        <ejs-button  v-if="!postToEdit" @click="createNewPost" class="mt-4"
                    :content="'Create post'"></ejs-button>
        <ejs-button  v-if="postToEdit" @click="saveEditedPost" class="mt-4"
                     :content="'Edit post'"></ejs-button>
      </div>
    </div>
    <ejs-listview :key="listKey" ref="postList" :cssClass="'e-list-template'"
                  :dataSource="pagedPosts"
                  :template="'template'">
      <template v-slot:template="{data}">
        <div :id="'post' + data.id" :class="[data.number === topic.posts.length ? '' : 'mb-5 border-bottom-gray']">
          <div class="row post-header">
            <div class="col d-flex justify-content-start ms-3">
              <h3>{{ data.createdAt }}</h3>
            </div>
            <div class="col d-flex justify-content-end me-3">
              <h3>#{{ data.number }}</h3>
            </div>
          </div>
          <div class="row">
            <div class="col-3 ms-3 p-3 profile-info">
              <div class="row">
                <div class="col">
                  <a class="custom-link e-bold" :href="`/profile/${data.ownerUsername}`"
                     @click.prevent="openProfile(data.ownerUsername)">{{ data.ownerUsername }}</a>
                </div>
              </div>
              <div class="row mt-3">
                <div class="col">
                  <a :href="`/profile/${data.ownerUsername}`" style="max-width: inherit;"
                  @click.prevent="openProfile(data.ownerUsername)">
                    <img style="max-width: inherit; box-shadow: 0px 0px 10px 1px black"
                       :src="data.ownerImageUrl ? data.ownerImageUrl : '/placeholder.jpg'"
                       :alt="data.ownerUsername"/>
                  </a>
                </div>
              </div>
            </div>
            <div class="col">
              <div class="row">
                <div class="col">
                  <div v-if="data.quotedPostId && !data.deleted" class="quoted-post">
                    <QuotedPost v-if="!getPostById(data.quotedPostId).deleted" :posts="this.topic.posts"
                                :quoted-post="getPostById(data.quotedPostId)" :level="1"/>
                    <p v-else>[This post has been deleted]</p>
                  </div>
                  <span :id="'postContent' + data.id" v-if="!data.deleted"
                        v-html="data.content"></span>
                  <p v-else>[This post has been deleted]</p>
                </div>
              </div>
              <div class="row me-2 mt-4 mb-4">
                <div v-if="data.modifiedByUsername" class="col justify-content-start d-flex">
                  <i>Last modified {{data.modifiedAt}}, by {{data.modifiedByUsername}} {{data.modifications > 1 ? '| Times modified: ' + data.modifications : '' }}</i>
                </div>
                <div class="col justify-content-end d-flex">
                  <ejs-button v-if="!data.deleted"
                              class="me-3" @click.stop="quotePost(data)"
                              :content="'Quote'"
                              :iconCss="'e-icons e-reply'" :iconPosition="'Right'"/>
                  <ejs-button v-if="!data.deleted && isAuthorized(data.ownerUsername)"
                              class="e-primary me-3" @click.stop="editPost(data)"
                              :content="'Edit'"
                              :iconCss="'e-icons e-edit'" :iconPosition="'Right'"/>
                  <ejs-button v-if="!data.deleted && isAuthorized(data.ownerUsername)"
                              class="e-primary" @click.stop="deletePost(data)"
                              :content="'Delete'"
                              :iconCss="'e-icons e-delete-1'" :iconPosition="'Right'"/>
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>
    </ejs-listview>

    <ejs-pager ref="pager" :click="changePage" :totalRecordsCount="topic.posts.length" :pageSize="10"
               :pageCount="5" :query="query"></ejs-pager>

  </div>
</template>

<script>
import axios from "axios";
import {ListViewComponent} from "@syncfusion/ej2-vue-lists";
import {PagerComponent} from "@syncfusion/ej2-vue-grids";
import {auth} from "@/firebaseServices/firebaseConfig";
import RichTextEditor from "@/components/RichTextEditor";
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import QuotedPost from "@/components/QuotedPost";
import {deleteObject, getStorage, ref} from "firebase/storage";
import {Query} from "@syncfusion/ej2-data";
import {store} from "@/main";
import {onAuthStateChanged} from "firebase/auth";
export default {
  name: "Topic",
  components: {
    QuotedPost,
    RichTextEditor,
    'ejs-listview': ListViewComponent,
    'ejs-pager': PagerComponent,
    'ejs-button': ButtonComponent,
  },
  data() {
    return {
      topic: { posts: [] },
      pagedPosts: [],
      query: new Query().range(0, 10),
      showNewPostForm: false,
      rteValue: '',
      listKey: 0,
      quotedPost: null,
      postToEdit: null,
      admin: false,
    };
  },
  async mounted() {
    onAuthStateChanged(auth, async user => {
      if (user) {
        await user.getIdTokenResult(true).then(idTokenResult => {
          this.admin = !!idTokenResult.claims.admin;
        });
      }
    });
    await this.getTopicWithPosts();
    this.pagedPosts = this.topic.posts.slice(0, 10);
    if(this.$route.query.page === 'last') {
      const lastPageButton = document.querySelectorAll('[aria-label="Go to last page"]')[0];
      lastPageButton?.click();
    }
  },
  methods: {
    async getTopicWithPosts(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/forum/topic/${this.$route.params.id}`);
      this.topic = data;
      for(let [i, post] of this.topic.posts.entries()){
        post.number = i + 1;
      }
    },
    changePage(e){
      this.pagedPosts = this.topic.posts.slice((e.currentPage - 1)  * 10, e.currentPage * 10);
      this.query = new Query().range((e.currentPage - 1)  * 10, e.currentPage * 10);
      window.scroll({top: 0, behavior: 'smooth'});
    },
    goToLastPage(postId){
      this.pagedPosts = this.topic.posts.slice((this.$refs.pager.ej2Instances.totalPages - 1)  * 10);
      this.query = new Query().page((this.$refs.pager.ej2Instances.totalPages - 1), 10);
      this.$refs.pager.goToPage(this.$refs.pager.ej2Instances.totalPages);
      this.$nextTick(() => {
        const postOffset = document.getElementById('post' + postId)?.offsetTop
        window.scroll({top: postOffset, behavior: 'smooth'});
      });
    },
    openProfile(username){
      this.$router.push({name: 'profile', params: {username: username}});
    },
    togglePostForm(){
      this.quotedPost = null;
      this.postToEdit = null;
      this.rteValue = '';
      this.showNewPostForm = !this.showNewPostForm;
    },
    async createNewPost(){
      const {data} = await axios.post(`${process.env.VUE_APP_BACKEND}/forum/post`, {
        ownerUsername: auth.currentUser.displayName,
        topicId: this.topic.id,
        content: this.rteValue,
        quotedPostId: this.quotedPost?.id,
      });
      data.topicTitle = this.topic.title;
      data.number = this.topic.posts.length + 1;
      data.quotedPostId = this.quotedPost?.id;
      data.ownerUsername = auth.currentUser.displayName;
      data.ownerImageUrl = auth.currentUser.photoURL;
      this.topic.posts.push(data);
      this.$refs.postList.updated();
      this.$refs.pager.refresh();
      this.showNewPostForm = false;
      this.listKey += 1;
      this.rteValue = '';
      this.quotedPost = null;
      this.goToLastPage(data.id);
      await axios.post(`${process.env.VUE_APP_BACKEND}/notification/topic`, {
        type: 'new_topic_post',
        boardId: this.topic.boardId,
        topicId: this.topic.id,
        topicTitle: this.topic.title,
        senderUsername: data.ownerUsername,
        socketId: store.getters.socketId,
      });
    },
    updateRteValue(value){
      this.rteValue = value;
    },
    getPostById(id){
      return this.topic.posts.find(post => post.id === id);
    },
    isAuthorized(username){
      return this.$store.getters.user?.displayName === username || this.admin;
    },
    async deletePost(post){
      await axios.delete(`${process.env.VUE_APP_BACKEND}/forum/post/${post.id}`);
      const storage = getStorage();
      const imageElements = document.querySelectorAll(`#postContent${post.id} img`);
      for(let image of imageElements){
        if(image.src.indexOf('firebasestorage') === -1) continue;
        const path = decodeURIComponent(image.src.split("o/")[1].split("?")[0]);
        const imageRef = ref(storage, path)
        await deleteObject(imageRef);
      }
      post.deleted = true;
      this.$refs.postList.updated();
      this.$refs.pager.refresh();
      this.listKey += 1;
    },
    quotePost(post){
      this.quotedPost = post;
      this.showNewPostForm = true;
      window.scroll({
        top: 0,
        left: 0,
        behavior: 'smooth'
      });
    },
    editPost(post){
      this.rteValue = document.getElementById('postContent' + post.id).innerHTML;
      this.postToEdit = post;
      this.quotedPost = null;
      this.showNewPostForm = true;
      window.scroll({
        top: 0,
        left: 0,
        behavior: 'smooth'
      });
    },
    async saveEditedPost(){
      const {data} = await axios.patch(`${process.env.VUE_APP_BACKEND}/forum/post/${this.postToEdit.id}`, {
        content: this.rteValue,
        modifiedByUsername: auth.currentUser.displayName,
      });
      this.postToEdit.content = data.content;
      this.postToEdit.modifiedByUsername = data.modifiedByUsername;
      this.postToEdit.modifiedAt = data.modifiedAt;
      this.postToEdit.modifications = data.modifications;
      this.$refs.postList.updated();
      this.$refs.pager.refresh();
      this.listKey += 1;
      this.rteValue = '';
      this.showNewPostForm = false;
      const postOffset = document.getElementById('post' + this.postToEdit.id)?.offsetTop
      window.scroll({
        top: postOffset,
        behavior: 'smooth'
      });
      this.postToEdit = null;
    }
  },
}
</script>

<style>

.post-header{
  background-color: #e54e4e;
  color: white;
}

.profile-info{
  background-color: #f2f2f2;
  border-right: 1px solid #e5e5e5;
}

.e-icons.e-delete-1:before, .e-icons .e-edit:before {
  color: white;
}

.e-listview .e-icons {
  color: unset;
}

.right-0 {
  right: 0;
}

</style>