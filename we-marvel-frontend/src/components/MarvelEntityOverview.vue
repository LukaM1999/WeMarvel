<template>
<div class="row">
  <div class="col">
    <div class="row">
      <div class="col">
        <a :href="entity.url" target="_blank">
          <img :src="entity.thumbnail.replace('portrait_small', 'portrait_incredible')"
               :alt="entity.title" :title="entity.title"/>
        </a>
      </div>
    </div>
    <div v-if="entity.seriesTitle" class="row justify-content-center mt-2">
      <div class="col-1 d-flex justify-content-start">
        <b class="label">Series:</b>
      </div>
      <div class="col-1 align-self-center">
        <a class="custom-link" :href="`/series/${entity.seriesId}`">
          {{entity.seriesTitle}}
        </a>
      </div>
    </div>
    <div v-if="entity.format" class="row justify-content-center mt-2">
      <div class="col-1 d-flex justify-content-start">
        <b class="label">Format:</b>
      </div>
      <div class="col-1 align-self-center">
        {{entity.format}}
      </div>
    </div>
    <div v-if="entity.pageCount > 0" class="row justify-content-center mt-2">
      <div class="col-1 d-flex justify-content-start">
        <b class="label">Pages:</b>
      </div>
      <div class="col-1 align-self-center">
        {{entity.pageCount}}
      </div>
    </div>
    <div v-if="entity.issueNumber > 0" class="row justify-content-center mt-2">
      <div class="col-1 d-flex justify-content-start">
        <b class="label">Issue:</b>
      </div>
      <div class="col-1 align-self-center">
        {{entity.issueNumber}}
      </div>
    </div>
    <div v-if="entity.startYear" class="row justify-content-center mt-2">
      <div class="col-1 d-flex justify-content-start">
        <b class="label">Start year:</b>
      </div>
      <div class="col-1 align-self-center">
        {{entity.startYear}}
      </div>
    </div>
    <div v-if="entity.endYear" class="row justify-content-center mt-2">
      <div class="col-1 d-flex justify-content-start">
        <b class="label">End year:</b>
      </div>
      <div class="col-1 align-self-center">
        {{entity.endYear}}
      </div>
    </div>
    <div class="row justify-content-center mt-2">
      <div class="col-1 d-flex justify-content-start">
        <b class="label text-nowrap">Average rating:</b>
      </div>
      <div class="col-1 align-self-center">
        {{entity.averageRating || 0}}/10
      </div>
    </div>
    <div v-if="entity.ratingCount" class="row justify-content-center mt-2">
      <div class="col-1 d-flex justify-content-start">
        <b class="label">Raters:</b>
      </div>
      <div class="col-1 align-self-center">
        {{entity.ratingCount}}
      </div>
    </div>
    <div v-if="entity.readingCount" class="row justify-content-center mt-2">
      <div class="col-1 d-flex justify-content-start">
        <b class="label">Readers:</b>
      </div>
      <div class="col-1 align-self-center">
        {{entity.readingCount}}
      </div>
    </div>
  </div>
</div>
</template>

<script>
export default {
  name: "MarvelEntityOverview",
  props: {
    entity: {
      type: Object,
      required: true
    }
  }
}
</script>

<style scoped>

</style>