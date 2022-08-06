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
@import "../public/styles/bootstrap-grid.min.css";
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

@font-face {
  font-family: 'e-menu';
  src:
      url(data:application/x-font-ttf;charset=utf-8;base64,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) format('truetype');
  font-weight: normal;
  font-style: normal;
}

.em-icons {
  font-family: 'e-menu';
  font-style: normal;
  font-variant: normal;
  font-weight: normal;
  line-height: 1;
  text-transform: none;
}

.e-menu-wrapper ul .e-menu-item .e-profile::before {
  content: '\e424';
}

.e-menu-wrapper .e-menu .e-menu-item .e-menu-icon {
  color: #fff;
}

.e-menu-wrapper .e-menu .e-menu-item.e-selected .e-menu-icon {
  color: black;
}

.e-view::before {
  content: '\e358';
}

.e-signout::before {
  color: #e54e4e;
  content: '\e610';
}

.e-menu-wrapper .e-ul .e-menu-item .e-menu-icon {
  font-size: 22px;
}

.e-menu-wrapper ul .e-menu-item .e-menu-icon {
  font-size: 22px;
}

div[class="e-menu-wrapper e-lib e-keyboard"]{
  background-color: transparent;
  height: 100%;
}

.e-toolbar .e-toolbar-items .e-toolbar-item > * {
  height: 100%;
}

.e-menu-wrapper ul.e-menu {
  height: 100%;
}

ul[data-v-54f74f74] {
  color: #fff !important;
  font-weight: bold !important;
  font-size: 15px !important;
}

.e-menu-wrapper ul .e-menu-item {
  height: 100%;
  align-items: center;
}

div :has(div .e-menu-wrapper) {
  height: 100%;
}

.e-menu-wrapper .e-menu .e-menu-item .e-caret {
  top: unset;
  color: #fff;
}

.e-menu-wrapper ul {
  font-weight: bold;
}

td {
  padding: 10px 2px 10px 4px;
}

tr:nth-child(even) {
  background-color: #f2f2f2;
}

tr:hover {
  background-color: #d9d9d9;
  cursor: pointer;
}

.e-grid .e-headercell {
  background-color: #e54e4e;
  color: #fff;
}

.e-grid .e-headercelldiv {
  font-weight: bold;
  font-size: 20px;
}

</style>
