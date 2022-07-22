<template>
  <HelloWorld msg="Welcome to Your Vue.js App"/>
</template>

<script>
import HelloWorld from './components/HelloWorld.vue'

export default {
  name: 'App',
  components: {
    HelloWorld
  }
}
</script>

<style lang="scss">
@import "public/styles/material.scss";
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

.grid-container {
  display: grid;
  grid-template-columns: auto auto;
  grid-gap: 10px;
  padding: 10px;
  justify-content: center;
}

.top-right {
  top: 0;
  text-align: right;
  bottom: auto !important;
}

.text-left {
  text-align: left;
}

.e-listview .e-list-item{
  border-bottom: 1px solid rgba(0,0,0,0.1);
}

.e-listview li:last-child {
  border-bottom: none;
}

.e-list-wrapper {
  border-left: 15px solid #e54e4e;
  border-right: 15px solid #e54e4e;
}

.e-listview .e-list-header {
  background-color: #e54e4e;
  color: #fff;
}

.e-headertemplate-text{
  width: 100% !important;
}

.e-list-header .grid-item .e-input-value {
  color: white;
}
</style>
