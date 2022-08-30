<template>
  <router-view :notifications="notifications" @all-read="notifications = []"></router-view>
</template>

<script>

import {onAuthStateChanged, onIdTokenChanged} from "firebase/auth";
import {auth} from "@/firebaseConfig";
import {pusher} from "@/main";
import axios from "axios";
import {store} from "@/main";
import {ToastUtility} from "@syncfusion/ej2-vue-notifications";

let ToastObj = undefined;
let topicsChannel = undefined;
let messagesChannel = undefined;
let friendRequestsChannel = undefined;
let notificationSettings = undefined;

export default {
  name: 'App',
  data(){
    return {
      notifications: [],
    }
  },
  async mounted() {
    onIdTokenChanged(auth, async (user) => {
      console.log('onIdTokenChanged');
      user?.getIdToken().then(token => {
        store.commit('setToken', token);
      });
      store.commit('setUser', user);
      if(!user?.displayName) {
        pusher.connection.disconnect();
        return;
      }
      if(pusher.connection.state === 'disconnected') {
        pusher.connect();
      }
      await this.getNotificationSettings();
      await this.getAllUnreadNotifications();
      if(!notificationSettings.topics) topicsChannel = pusher.unsubscribe('topics');
      if(!notificationSettings.messages) messagesChannel = pusher.unsubscribe('messages');
      if(!notificationSettings.friendRequests) friendRequestsChannel = pusher.unsubscribe('friend-requests');
      if(notificationSettings.topics) {
        topicsChannel = pusher.subscribe('topics');
        topicsChannel.bind('new_topic_post', (data) => {
          this.notifications.unshift(data);
          if(this.$route.path === `/forum/board/${data.boardId}/topic/${data.topicId}`) {
            return;
          }
          ToastObj = ToastUtility.show({
            title: `New post in topic ${data.topicTitle}`,
            content: 'Click button below to see it!',
            cssClass: 'e-toast-info',
            icon: 'e-comment-add e-icons',
            position: {X: 'Right', Y: 'Top'},
            showCloseButton: true,
            buttons: [{
              click: this.goToTopic(data.boardId, data.topicId),
              model: {
                content: 'Go to topic',
              }
            }],
            timeOut: 7000,
            extendedTimeout: 5000,
            target: '#container',
            animation: {show: {effect: 'SlideRightIn'}, hide: {effect: 'SlideRightOut'}},
            click: arg => {
              console.log(arg);
            }
          });
        });
      }
      if(notificationSettings.friendRequests){
        friendRequestsChannel = pusher.subscribe('friends');
        friendRequestsChannel.bind('new_friend_request', (data) => {
          this.notifications.unshift(data);
          ToastObj = ToastUtility.show({
            title: `New friend request from ${data.senderUsername}`,
            content: 'Click button below to see it!',
            cssClass: 'e-toast-info',
            icon: 'e-people e-icons',
            position: {X: 'Right', Y: 'Top'},
            showCloseButton: true,
            buttons: [{
              click: this.goToProfile(data.senderUsername),
              model: {
                content: 'Go to profile',
              }
            }],
            timeOut: 7000,
            extendedTimeout: 5000,
            target: '#container',
            animation: {show: {effect: 'SlideRightIn'}, hide: {effect: 'SlideRightOut'}},
            click: arg => {
              console.log(arg);
            }
          });
        });
        friendRequestsChannel.bind('accepted_friend_request', (data) => {
          this.notifications.unshift(data);
          ToastObj = ToastUtility.show({
            title: `${data.senderUsername} accepted your friend request`,
            content: 'Click button below to see their profile!',
            cssClass: 'e-toast-info',
            icon: 'e-check-circle e-icons',
            position: {X: 'Right', Y: 'Top'},
            showCloseButton: true,
            buttons: [{
              click: this.goToProfile(data.senderUsername),
              model: {
                content: 'Go to profile',
              }
            }],
            timeOut: 7000,
            extendedTimeout: 5000,
            target: '#container',
            animation: {show: {effect: 'SlideRightIn'}, hide: {effect: 'SlideRightOut'}},
            click: arg => {
              console.log(arg);
            }
          });
        });
      }
    })
  },
  methods: {
    goToTopic(boardId, topicId) {
      return () => {
        ToastObj.hide();
        this.$router.push({name: 'topic', params: {boardId: boardId, id: topicId}, query: {page: 'last'}});
      }
    },
    async getNotificationSettings() {
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/notification/settings`);
      notificationSettings = data;
    },
    async getAllUnreadNotifications(){
      await axios.get(`${process.env.VUE_APP_BACKEND}/notification/unread`).then(({data}) => {
        this.notifications = data;
      }).catch(error => {
        alert(error.message)
      })
    },
    goToProfile(senderUsername) {
      return () => {
        ToastObj.hide();
        this.$router.push({name: 'profile', params: {username: senderUsername}});
      }
    }
  },
}
</script>

<style lang="scss">
@import "public/styles/material.scss";
@import "../public/styles/bootstrap-grid.min.css";
@import "../public/styles/bootstrap-utilities.min.css";
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  height: 100%;
}

html {
  height: 100%;
}

body {
  height: 100%;
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
  //border-bottom: 1px solid rgba(0,0,0,0.1);
  cursor: auto;
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

.e-grid .e-content {
  overflow-x: hidden;
}

.e-grid .e-gridheader {
  padding-right: 0px !important;
}

.e-grid .e-toolbar {
  border-top: 0;
}

.e-grid .e-filtered::before {
  color: black;
}

.e-grid .e-gridheader .e-icons:not(.e-icon-hide):not(.e-check):not(.e-stop):not(.e-icon-reorderuparrow):not(.e-icon-reorderdownarrow) {
  color: #fff;
}

.e-grid .e-icons:not(.e-btn-icon) {
  color: #fff;
}

.custom-link {
  font-weight: bold;
  text-decoration: none;
}

.custom-link:hover {
  text-decoration: underline;
}

.e-hover {
  background-color: unset!important;
}

.text-left {
  text-align: left;
}

.quoted-post{
  border: 1px solid #e5e5e5;
  margin: 2%;
  padding: 2%;
}

.border-bottom-gray {
  border-bottom: 1px solid #e5e5e5;
}

.e-toast-container .e-toast .e-toast-actions .e-btn {
  background-color: rgba(229, 78, 78, 0.7);
  color: #fff;
}

.e-toast-container .e-toast .e-toast-actions .e-btn:hover {
  background-color: rgba(229, 78, 78, 0.9);
  color: #fff;
}

.e-badge[data-v-54f74f74] {
  position: relative !important;
  top: -15px !important;
  left: -4px !important;
}

.e-listview:not(.e-list-template) .e-list-item {
  height: auto;
  max-height: 11em;
  line-height: 24px;
  padding: 0 14px;
  position: relative;
  border-bottom: 1px solid #e5e5e5;
  min-height: 6em;
}

.e-tooltip-wrap.e-popup {
  background-color: white;
  border: none;
  box-shadow: 0px 0px 6px 0px black;
}

.e-tooltip-wrap .e-arrow-tip-inner.e-tip-top {
  color: white;
}

.e-listview[data-v-54f74f74] {
  overflow: initial !important;
}

.e-icons.e-check-box[data-v-54f74f74]:before {
  color: white;
}

.e-listview .e-icons[data-v-54f74f74]:hover {
  cursor: pointer;
}

</style>
