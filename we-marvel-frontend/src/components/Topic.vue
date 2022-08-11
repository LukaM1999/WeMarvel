<template>
  <div id="topicContainer">
    <h1>{{ topic?.title }}</h1>
    <button @click="showNewPostForm = !showNewPostForm" class="btn btn-primary">New post</button>
    <div v-if="showNewPostForm" class="row mb-5">
      <div class="col">
        <h3>New post</h3>
        <RichTextEditor @value-changed="updateRteValue"/>
        <ejs-button @click="createNewPost"
                    :content="'Create post'"></ejs-button>
      </div>
    </div>
    <ejs-listview :key="listKey" ref="postList" :cssClass="'e-list-template'"
                  :dataSource="topic.posts"
                  :template="'template'">
      <template v-slot:template="{data}">
        <div :id="'post' + data.id" class="mb-5">
          <div class="row post-header">
            <div class="col d-flex justify-content-start ms-3">
              <h3>{{ data.createdAt }}</h3>
            </div>
            <div class="col d-flex justify-content-end me-3">
              <h3>#{{ data.number }}</h3>
            </div>
          </div>
          <div class="row">
            <div class="col-3 pt-2 ms-3 profile-info">
              <a class="custom-link e-bold" :href="`/profile/${data.ownerUsername}`"
                 @click.prevent="openProfile(data.ownerUsername)">{{ data.ownerUsername }}</a>
            </div>
            <div class="col">
              <div class="row">
                <div class="col">
                  <div v-if="data.quotedPostId && !data.deleted" class="quoted-post">
                    <QuotedPost v-if="!getPostById(data.quotedPostId).deleted" :posts="this.topic.posts"
                                :quoted-post="getPostById(data.quotedPostId)"/>
                    <p v-else>[This post has been deleted]</p>
                  </div>
                  <span :id="'postContent' + data.id" v-if="!data.deleted" v-html="data.content"></span>
                  <p v-else>[This post has been deleted]</p>
                </div>
              </div>
              <div class="row me-2 mt-4">
                <div class="col justify-content-end d-flex">
                  <ejs-button v-if="!data.deleted && isAuthorized(data.ownerUsername)"
                              class="e-primary" @click="deletePost(data)"
                              :content="'Delete post'"
                              :iconCss="'e-icons e-delete-1'" :iconPosition="'Right'"/>
                </div>
              </div>
            </div>
          </div>
        </div>
      </template>
    </ejs-listview>

    <ejs-pager ref="pager" :totalRecordsCount="topic.posts.length" :pageSize="20"
               :pageCount="5"></ejs-pager>

<!--    <ejs-dialog target="#topicContainer" width="50%" style="position: fixed"-->
<!--                cssClass="e-fixed" isModal="true" ref="newPostDialog" header="New post"-->
<!--                :content="getRteTemplate" :showCloseIcon="true" :visible="false"-->
<!--                :buttons="newPostButtons">-->
<!--    </ejs-dialog>-->
  </div>
</template>

<script>
import axios from "axios";
import {ListViewComponent} from "@syncfusion/ej2-vue-lists";
import {PagerComponent} from "@syncfusion/ej2-vue-grids";
import {auth} from "@/firebaseConfig";
import RichTextEditor from "@/components/RichTextEditor";
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import QuotedPost from "@/components/QuotedPost";
import {deleteObject, getStorage, ref} from "firebase/storage";
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
      frontend: process.env.VUE_APP_FRONTEND,
      showNewPostForm: false,
      rteValue: '',
      listKey: 0,
    };
  },
  async mounted() {
    await this.getTopicWithPosts();
  },
  methods: {
    async getTopicWithPosts(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/forum/topic/${this.$route.params.id}`);
      this.topic = data;
      for(let [i, post] of this.topic.posts.entries()){
        post.number = i + 1;
      }
    },
    openProfile(username){
      this.$router.push({name: 'profile', params: {username: username}});
    },
    async createNewPost(){
      const {data} = await axios.post(`${process.env.VUE_APP_BACKEND}/forum/post`, {
        ownerUsername: auth.currentUser.displayName,
        topicId: this.topic.id,
        content: this.rteValue,
      });
      data.topicTitle = this.topic.title;
      data.number = this.topic.posts.length + 1;
      this.topic.posts.push(data);
      this.$refs.postList.updated();
      this.$refs.pager.refresh();
      this.showNewPostForm = false;
      this.listKey += 1;
      this.rteValue = '';
      await this.$nextTick(() => {
        document.getElementsByClassName('e-last')[0]?.click();
        document.getElementById('post' + data.id).scrollIntoView({behavior: 'smooth'});
      });
    },
    updateRteValue(value){
      this.rteValue = value;
      console.log('rteValue', this.rteValue);
    },
    getPostById(id){
      return this.topic.posts.find(post => post.id === id);
    },
    isAuthorized(username){
      return this.$store.getters.user?.displayName === username;
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

.e-icons.e-delete-1:before{
  color: white;
}

</style>