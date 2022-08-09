<template>
<h1></h1>
</template>

<script>
import axios from "axios";

export default {
  name: "Topic",
  data() {
    return {
      topic: null,
      pageSettings: {
        pageSize: 10,
        pageCount: 0,
        currentPage: 1,
        totalRecordsCount: 0
      }
    };
  },
  mounted() {
    this.getTopicWithPosts();
  },
  methods: {
    async getTopicWithPosts(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/forum/topic/${this.$route.params.id}`);
      this.topic = data;
      console.log(data);
    }
  }
}
</script>

<style scoped>

</style>