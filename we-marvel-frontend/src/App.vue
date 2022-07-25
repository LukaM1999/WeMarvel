<template>
  <router-view></router-view>
</template>

<script>
export default {
  name: 'App',
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

#toolbar {
  position: sticky;
  top: 0;
  background-color: #e54e4e;
  color: #fff;
  z-index: 10000;
  box-shadow: black -7px -6px 14px 5px;
  transition: top 0.3s;
}

.e-toolbar .e-toolbar-items {
  background: #e54e4e;
}

.e-toolbar .e-tbar-btn {
  background: #e54e4e;
  color: #fff;
}

.e-toolbar .e-toolbar-items .e-toolbar-item .e-tbar-btn.e-btn {
  height: 100%;
  margin: 0;
}

.e-toolbar .e-toolbar-items .e-toolbar-item .e-tbar-btn-text {
  color: #fff;
  font-weight: bold;
}

.e-toolbar .e-toolbar-items .e-toolbar-item:not(.e-separator) {
  padding: 0 3.5px;
}

.e-fixed {
  position: fixed;
}

.e-dialog .e-btn .e-btn-icon.e-icon-dlg-close {
  color: #fff;
}

.e-dialog .e-btn.e-dlg-closeicon-btn:hover {
  background-color: black;
}

.e-dialog .e-dlg-header-content {
  background: #e54e4e;
  text-align: -webkit-center;
}

.e-dialog .e-dlg-header {
  color: #fff;
  font-weight: bold;
  text-align: center;
}

.e-dialog .e-dlg-header-content + .e-dlg-content {
  padding-top: 18px;
}

.e-dialog .e-btn.e-dlg-closeicon-btn:hover span {
  color: #fff;
}

label.e-float-text {
  text-align: left;
}

span .e-input-group-icon .e-password:after {
  content: '\e345';
}

</style>
