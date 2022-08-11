<template>
  <a class="custom-link mt-2 e-bold"
     :href="`/profile/${quotedPost.ownerUsername}`"
     @click.prevent="openProfile(quotedPost.ownerUsername)">
    {{ quotedPost.ownerUsername }} said:</a>
  <span v-html="quotedPost.content"></span>
  <div v-if="quotedPost.quotedPostId" class="quoted-post">
    <QuotedPost v-if="!getPostById(quotedPost.quotedPostId).deleted"
                :quoted-post="getPostById(quotedPost.quotedPostId)"
                :posts="posts"/>
    <p v-else>[This post has been deleted]</p>
  </div>
</template>

<script>
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
  },
  methods: {
    getPostById(id){
      return this.posts.find(post => post.id === id);
    },
    openProfile(username){
      this.$router.push({name: 'profile', params: {username: username}});
    },
  },
}
</script>

<style scoped>

</style>