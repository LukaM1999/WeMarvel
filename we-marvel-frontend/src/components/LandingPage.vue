<template>
  <div id="signInDialogTarget">
    <ejs-toolbar id="toolbar" width="100%" height="60px">
      <e-items>
        <e-item :template="'menuTemplate'" align="Center">
          <template v-slot:menuTemplate="{}">
            <ejs-menu :items="menuItems" @select="menuItemSelected"></ejs-menu>
          </template>
        </e-item>
        <e-item v-if="!signedInUser" :template="'signInTemplate'" align="Right">
          <template v-slot:signInTemplate="{}">
            <ejs-button @click="openSignIn" class="e-control e-lib e-warning">Sign in</ejs-button>
          </template>
        </e-item>
        <e-item v-if="signedInUser" :template="'notificationsTemplate'" align="Right">
          <template v-slot:notificationsTemplate="{}">
            <div id="notifications" title="View notifications" class="row h-100 align-items-center">
              <ejs-tooltip isSticky="true" position="TopLeft" :content="'tooltipTemplate'"
                           opensOn="Click" width="30%">
                <template v-slot:tooltipTemplate="{}">
                  <ejs-listview id="notificationsList" :dataSource="notifications"
                                :headerTemplate="'notificationsHeaderTemplate'"
                                :template="'notificationsTemplate'"
                                :showHeader="true"
                                :height="notifications.length > 0 ? 400 : 100"
                                width="100%">
                    <template v-slot:notificationsHeaderTemplate="{}">
                      <div class="row justify-content-start">
                        <div class="col">
                          <div class="e-list-header-text">
                            <span class="e-list-header-text-content">Notifications</span>
                          </div>
                        </div>
                        <div v-if="notifications.length > 0" class="col d-flex justify-content-end">
                          <span title="Mark all as read" class="e-icons e-check-box" @click="markAllAsRead"></span>
                        </div>
                      </div>
                    </template>
                    <template v-slot:notificationsTemplate="{data}">
                      <div class="row">
                        <div class="col d-flex justify-content-start">
                          <b v-if="data.type === 'new_topic_post'">New post</b>
                        </div>
                        <div class="col d-flex justify-content-end">
                          <i>{{data.receivedAt}}</i>
                        </div>
                      </div>
                      <div class="row">
                        <div class="col justify-content-start">
                          <a class="custom-link" :href="`/profile/${data.posterUsername}`"
                                                            @click.prevent="openProfile(data.posterUsername)">
                            {{data.posterUsername}}</a>
                        </div>
                        <div class="col justify-content-end">
                          <a class="custom-link" :href="`/forum/board/${data.boardId}/topic/${data.topicId}`"
                                                             @click.prevent="openTopic(data)">
                            {{data.topicTitle}}</a>
                        </div>
                      </div>
                    </template>
                  </ejs-listview>
                </template>
                <div class="col">
                  <i class="e-icons e-comments e-large"></i>
                  <i v-if="notifications.length > 0" class="e-badge e-badge-info e-badge-notification e-badge-circle">
                    {{notifications.length}}</i>
                </div>
              </ejs-tooltip>
            </div>
          </template>
        </e-item>
        <e-item v-if="signedInUser" :template="'profileTemplate'" align="Right">
          <template v-slot:profileTemplate="{}">
            <ejs-menu :items="profileMenuItems" @select="profileMenuItemSelected"></ejs-menu>
          </template>
        </e-item>
      </e-items>
    </ejs-toolbar>
    <ejs-dialog target="#signInDialogTarget" width="30%" style="position: fixed"
                cssClass="e-fixed" isModal="true" ref="signInDialog" header="Sign in"
                :content="'template'" :showCloseIcon="true" :visible="false"
                :buttons="signInDialogButtons">
      <template v-slot:template="{}">
        <div class="e-control e-lib e-primary">
          <div class="e-input-group e-float-input">
            <input type="text" v-model="email" required />
            <label class="e-float-text e-label">Email</label>
          </div>
        </div>
        <div class="e-control e-lib e-primary">
          <div class="e-input-group e-float-input">
            <input type="text" v-model="username" required />
            <label class="e-float-text e-label">Username</label>
          </div>
        </div>
        <div class="e-control e-lib e-primary">
          <div class="e-input-group e-float-input">
            <input :type="isTypePassword ? 'password' : 'text'"
                   v-model="password" required />
            <label class="e-float-text e-label">Password</label>
            <span class="e-input-group-icon e-password" @click="changeIcon">
              <box-icon :name="isShowIcon ? 'show' : 'hide'" color="gray"></box-icon>
            </span>
          </div>
        </div>
      </template>
    </ejs-dialog>
    <e-breadcrumb ref="breadcrumb" :url="breadcrumbUrl" :key="breadcrumbKey"
                  @created="breadcrumbCreated"
                  enableNavigation="false" style="margin-top: 30px"></e-breadcrumb>
    <div id="container">
      <router-view></router-view>
    </div>
    <footer class="footer">
      <a href="https://marvel.com/" class="e-vertical-align-center" target="_blank"><h3>Data provided by Marvel. © 2022 MARVEL</h3></a>
    </footer>
  </div>
</template>

<script>
import {
  ToolbarComponent,
  ItemDirective,
  ItemsDirective,
  MenuComponent,
  BreadcrumbComponent
} from '@syncfusion/ej2-vue-navigations'
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import { store } from '@/main'
import {DialogComponent, TooltipComponent} from '@syncfusion/ej2-vue-popups';
import {TextBoxComponent} from '@syncfusion/ej2-vue-inputs';
import {auth} from "@/firebaseConfig";
import {signInWithEmailAndPassword, createUserWithEmailAndPassword,
  sendEmailVerification, signOut, deleteUser, updateProfile} from "firebase/auth";
import axios from "axios";
import Pusher from "pusher-js";
import {topicsChannel} from "@/App";
import {ListViewComponent, Virtualization} from "@syncfusion/ej2-vue-lists";

export default {
  name: "LandingPage",
  components: {
    'ejs-toolbar': ToolbarComponent,
    'e-item': ItemDirective,
    'e-items': ItemsDirective,
    'ejs-button': ButtonComponent,
    'ejs-dialog': DialogComponent,
    'ejs-menu': MenuComponent,
    'e-breadcrumb': BreadcrumbComponent,
    'ejs-tooltip': TooltipComponent,
    'ejs-listview': ListViewComponent,
  },
  props: {
    notifications: {
      type: Array,
      default: () => []
    },
  },
  data() {
    return {
      breadcrumbKey: 0,
      breadcrumbUrl: '',
      isShowIcon: true,
      isTypePassword: true,
      email: '',
      username: '',
      password: '',
      signedInUser: null,
      signInDialogButtons: [
        {
          buttonModel: {content: 'Sign in', isPrimary: true},
          click: this.signIn
        },
        {
          buttonModel: {content: 'Sign up', isPrimary: false},
          click: this.signUp
        }
      ],
      menuItems: [
        {
          text: 'Comics',
          items: [
            { text: 'Search', url: `${process.env.VUE_APP_FRONTEND}/comics/search` },
            { text: 'Most popular', url: `${process.env.VUE_APP_FRONTEND}/comics/popular` },
            { text: 'Top rated', url: `${process.env.VUE_APP_FRONTEND}/comics/top-rated` },
          ]
        },
        {
          text: 'Characters',
          items: [
            { text: 'Search', url: `${process.env.VUE_APP_FRONTEND}/characters/search` },
            { text: 'Most popular', url: `${process.env.VUE_APP_FRONTEND}/characters/popular` },
            { text: 'Top rated', url: `${process.env.VUE_APP_FRONTEND}/characters/top-rated` },
          ]
        },
        {
          text: 'Community',
          items: [
            { text: 'Forum', url: `${process.env.VUE_APP_FRONTEND}/forum` },
            { text: 'Clubs', url: `${process.env.VUE_APP_FRONTEND}/clubs` },
            { text: 'Users', url: `${process.env.VUE_APP_FRONTEND}/users` },
          ]
        }
      ],
      profileMenuItems: [
        {
          text: 'Profile',
          iconCss: 'e-icons e-profile',
          items: [
            {text: 'View', iconCss: 'e-icons e-view'},
            {separator: true},
            {text: 'Sign out', iconCss: 'e-icons e-signout'},
          ]
        }
      ]
    };
  },
  async mounted() {
    window.onscroll = function() {
      const currentScrollPos = document.documentElement.scrollTop;
      if (store.getters.scrollPosition > currentScrollPos) {
        document.getElementsByClassName("e-toolbar")[0].style.top = "0px";
      } else {
        document.getElementsByClassName("e-toolbar")[0].style.top = "-80px";
      }
      store.commit('setScrollPosition', currentScrollPos);
    }
    this.signedInUser = store.getters.user;
    await this.$nextTick(() => {
      const dialogOverlay = document.getElementsByClassName("e-dlg-overlay")[0];
      dialogOverlay.style.position = 'fixed';
      const dialog = document.getElementsByClassName("e-dialog")[0];
      dialog.style.position = 'fixed';
      dialog.style.left = ''
      dialog.style.maxHeight = '100%';
      dialog.style.top = '';
    });
  },
  methods: {
    async breadcrumbCreated(){
      for(let [i, item] of this.$refs.breadcrumb.ej2Instances.properties.items.entries()){
        if(item.text === '/' || i === 0) continue;
        item.text = this.$filters.capitalize(item.text).split(new RegExp('[?#]'))[0];
        if (Number.isInteger(parseInt(item.text))) {
          const prevText = this.$refs.breadcrumb.ej2Instances.properties.items[i - 1].text.toLowerCase();
          if(prevText === 'topic' || prevText === 'board'){
            await axios.get(`${process.env.VUE_APP_BACKEND}/forum/${prevText}/${item.text}/name`)
              .then(({data}) => {
                item.text = data;
              })
          }
        }
      }
    },
    openSignIn() {
      this.$refs.signInDialog.show();
      const dialogOverlay = document.getElementsByClassName("e-dlg-overlay")[0];
      dialogOverlay.style.position = 'fixed';
      const dialog = document.getElementsByClassName("e-dialog")[0];
      dialog.style.position = 'fixed';
      dialog.style.maxHeight = '100%';
      dialog.style.left = ''
      dialog.style.top = '';
    },
    changeIcon() {
      this.isShowIcon = !this.isShowIcon;
      this.isTypePassword = !this.isTypePassword;
    },
    signIn(){
      signInWithEmailAndPassword(auth, this.email, this.password).then((userCredentials) => {
        userCredentials.user.getIdToken().then((token) => {
          store.commit('setToken', token);
        });
        this.$refs.signInDialog.hide();
        this.signedInUser = userCredentials.user;
      }).catch(error => {
        alert(error.message)
      })
    },
    signUp(){
      createUserWithEmailAndPassword(auth, this.email, this.password).then(async (userCredentials) => {
        await axios.post(`${process.env.VUE_APP_BACKEND}/user`, {email: this.email, username: this.username}).catch(error => {
          deleteUser(userCredentials.user);
        });
        await updateProfile(userCredentials.user, {displayName: this.username}).catch(error => {
          deleteUser(userCredentials.user);
        });
        await sendEmailVerification(userCredentials.user)
        userCredentials.user.getIdToken().then((token) => {
          store.commit('setToken', token);
        });
        this.$refs.signInDialog.hide();
        this.signedInUser = auth.currentUser;
      }).catch(error => {
        alert(error.message)
      })
    },
    signOut(){
      let self = this;
      signOut(auth).then(() => {
        store.commit('setToken', null);
        store.commit('setUser', null);
        self.signedInUser = null;
      }).catch(error => {
        alert(error.message)
      })
    },
    profileMenuItemSelected(e){
      console.log(e);
      if(e.item.text === 'Sign out'){
        this.signOut();
      }
      if(e.item.text === 'View'){
        this.$router.push({name: 'profile', params: {username: auth.currentUser.displayName}});
      }
    },
    openTopic(topic){
      this.$router.push({name: 'topic', params: {boardId: topic.boardId, topicId: topic.topicId}});
    },
    openProfile(username){
      this.$router.push({name: 'profile', params: {username: username}});
    },
    async markAllAsRead(){
      await axios.patch(`${process.env.VUE_APP_BACKEND}/notification/read`)
        .then(() => {
          this.$emit('all-read');
        }).catch(error => {
          alert(error.message)
        })
    }
  },
  watch: {
    $route: {
      deep: true,
      handler(newVal, oldVal) {
        this.breadcrumbUrl = newVal.path;
        this.breadcrumbKey++;
      }
    },
  },
}
</script>
<style scoped lang="scss">
$btn-warning-bgcolor: #504a4a;
@import "public/styles/material.scss";

.e-dlg-overlay {
  z-index: 10001;
}

.footer {
  width: 100%;
  height: 100px;
  background-color: #f5f5f5;
  border-top: 1px solid #e7e7e7;
  display: flex;
  align-items: center;
  justify-content: center;
}

</style>