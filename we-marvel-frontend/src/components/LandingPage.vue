<template>
  <div id="signInDialogTarget">
    <ejs-toolbar id="toolbar" width="100%" height="60px">
      <e-items>
        <e-item :template="'menuTemplate'" align="Center">
          <template v-slot:menuTemplate="{}">
            <ejs-menu :items="menuItems"></ejs-menu>
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
              <ejs-tooltip :isSticky="false" position="TopLeft" :content="'tooltipTemplate'">
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
                        <div class="col justify-content-start">
                          <div class="row">
                            <div class="col">
                              <b v-if="data.type !== 'accepted_friend_request'">New {{data.type === 'new_topic_post' ? 'post' : 'friend request'}} from <a class="custom-link" :href="`/profile/${data.senderUsername}`"
                                                                                                            @click.prevent="openProfile(data.senderUsername)">
                                {{data.senderUsername}}</a>
                              </b>
                              <b v-else>Friend request accepted from <a class="custom-link" :href="`/profile/${data.senderUsername}`"
                                                                 @click.prevent="openProfile(data.senderUsername)">
                                {{data.senderUsername}}</a></b>
                            </div>
                          </div>
                          <div class="row">
                            <div class="col">
                              <a :href="`/profile/${data.senderUsername}`">
                                <img v-if="data.senderUsername" :src="data.senderImageUrl || '/placeholder.jpg'"
                                     width="50" height="50" :alt="data.senderUsername"/>
                              </a>
                            </div>
                          </div>
                        </div>
                        <div class="col justify-content-end">
                          <div class="row justify-content-end">
                            <div class="col">
                              <i>{{data.receivedAt}}</i>
                            </div>
                          </div>
                          <div v-if="data.boardId" class="row justify-content-end">
                            <div class="col">
                              <a class="custom-link" :href="`/forum/board/${data.boardId}/topic/${data.topicId}`">
                                {{data.topicTitle}}</a>
                            </div>
                          </div>
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
              <span :class="isShowIcon ? 'e-icons e-eye' : 'e-icons e-eye-slash'" style="color: gray"></span>
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
import {auth} from "@/firebaseConfig";
import {signInWithEmailAndPassword, createUserWithEmailAndPassword,
  sendEmailVerification, signOut, deleteUser, updateProfile} from "firebase/auth";
import axios from "axios";
import {topicsChannel} from "@/App";
import {ListViewComponent} from "@syncfusion/ej2-vue-lists";

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
            { text: 'Search', url: '/comic'},
            { text: 'Most popular', url: `/comic/popular` },
            { text: 'Top rated', url: `/comic/top-rated` },
          ]
        },
        {
          text: 'Characters',
          items: [
            { text: 'Search', url: `/character` },
            { text: 'Most popular', url: `/character/popular` },
            { text: 'Top rated', url: `/character/top-rated` },
          ]
        },
        {
          text: 'Series',
          items: [
            { text: 'Search', url: '/series'},
            { text: 'Most popular', url: `/series/popular` },
            { text: 'Top rated', url: `/series/top-rated` },
          ]
        },
        {
          text: 'Community',
          items: [
            { text: 'Forum', url: `/forum` },
            { text: 'Users', url: `/users` },
          ]
        }
      ],
      profileMenuItems: [
        {
          text: 'Profile',
          iconCss: 'profile-image',
          items: [
            {text: 'Profile'},
            {text: 'Comics'},
            {text: 'Reviews'},
            {text: 'Friends'},
            {text: 'Friend requests'},
            {text: 'Settings'},
            {separator: true},
            {text: 'Sign out'},
          ]
        }
      ]
    };
  },
  async mounted() {
    window.onscroll = function() {
      const currentScrollPos = document.documentElement.scrollTop;
      if (store.getters.scrollPosition > currentScrollPos) {
        const toolbar = document.getElementsByClassName("e-toolbar")[0];
        if(toolbar) toolbar.style.top = "0";
      } else {
        const toolbar = document.getElementsByClassName("e-toolbar")[0];
        if(toolbar) toolbar.style.top = "-80px";
      }
      store.commit('setScrollPosition', currentScrollPos);
    }
    this.signedInUser = store.getters.user;
    this.profileMenuItems[0].text = this.signedInUser?.displayName || 'Profile';
    await this.$nextTick(() => {
      const toolbarItems = document.querySelectorAll('.e-toolbar .e-toolbar-items .e-toolbar-item > *');
      for(let i of toolbarItems) {
        i.style.display = 'contents';
        console.log(i);
      }
      const dialogOverlay = document.getElementsByClassName("e-dlg-overlay")[0];
      dialogOverlay.style.position = 'fixed';
      const dialog = document.getElementsByClassName("e-dialog")[0];
      dialog.style.position = 'fixed';
      dialog.style.left = ''
      dialog.style.maxHeight = '100%';
      dialog.style.top = '';
      const imageSpan = document.getElementsByClassName("profile-image")[0];
      if(!imageSpan) return;
      imageSpan.style.backgroundImage = `url(${this.signedInUser?.photoURL || '/placeholder.jpg'})`;
      imageSpan.style.width = '50px';
      imageSpan.style.height = '50px';
      imageSpan.style.backgroundSize = 'contain';
      imageSpan.style.backgroundRepeat = 'no-repeat';
      imageSpan.classList.remove('e-menu-icon');
    });
  },
  methods: {
    async breadcrumbCreated(){
      for(let [i, item] of this.$refs.breadcrumb.ej2Instances.properties.items.entries()){
        if(item.text === '/' || i === 0) continue;
        item.text = item.text.split(new RegExp('[?#]'))[0];
        const prevText = this.$refs.breadcrumb.ej2Instances.properties.items[i - 1].text.toLowerCase();
        if(prevText === 'profile') continue;
        if(store.getters.breadcrumb(`${prevText}_${item.text}`)){
          item.text = store.getters.breadcrumb(`${prevText}_${item.text}`);
          continue;
        }
        if (Number.isInteger(parseInt(item.text))) {
          if(prevText === 'topic' || prevText === 'board'){
            const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/forum/${prevText}/${item.text}/name`)
            store.commit('setBreadcrumb', {id: `${prevText}_${item.text}`, name: data});
            item.text = data;
            return;
          } else {
            const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/${prevText}/${item.text}/name`)
            store.commit('setBreadcrumb', {id: `${prevText}_${item.text}`, name: data});
            item.text = data;
            return;
          }
        }
        item.text = this.$filters.capitalize(item.text);
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
      if(e.item.text === 'Sign out'){
        this.signOut();
        return;
      }
      this.openProfile(e.item.text.toLowerCase());
    },
    openTopic(topic){
      this.$router.push({name: 'topic', params: {boardId: topic.boardId, topicId: topic.topicId}});
    },
    openProfile(tabName){
      this.$router.push({name: 'profile', params: {username: auth.currentUser?.displayName}, query: {tab: tabName}});
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