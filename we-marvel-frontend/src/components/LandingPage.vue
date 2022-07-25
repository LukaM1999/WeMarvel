<template>
  <div id="signInDialogTarget">
    <ejs-toolbar id="toolbar" width="100%" height="60px">
      <e-items>
        <e-item text="Cut"></e-item>
        <e-item text="Copy"></e-item>
        <e-item type="Separator"></e-item>
        <e-item text="Bold"></e-item>
        <e-item text="Underline"></e-item>
        <e-item text="Italic"></e-item>
        <e-item text="Color-Picker"></e-item>
        <e-item type="Separator"></e-item>
        <e-item text="A-Z Sort"></e-item>
        <e-item text="Z-A Sort"></e-item>
        <e-item text="Clear"></e-item>
        <e-item :template="'signInTemplate'" align="Right">
          <template v-slot:signInTemplate="{}">
            <ejs-button @click="openSignIn" class="e-control e-lib e-warning">Sign in</ejs-button>
          </template>
        </e-item>
        <e-item :template="'profileTemplate'" align="Right">
          <template v-slot:profileTemplate="{}">
            <ejs-menu :items="menuItems" @select="menuItemSelected"></ejs-menu>
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
    <router-view style="margin-top: 60px"></router-view>
  </div>
</template>

<script>
import {ToolbarComponent, ItemDirective, ItemsDirective, MenuComponent, MenuItem, MenuItemDirective} from '@syncfusion/ej2-vue-navigations'
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import { store } from '@/main'
import { DialogComponent} from '@syncfusion/ej2-vue-popups';
import {TextBoxComponent} from '@syncfusion/ej2-vue-inputs';
import {auth} from "@/firebaseConfig";
import {signInWithEmailAndPassword, onIdTokenChanged, createUserWithEmailAndPassword,
  sendEmailVerification, signOut, deleteUser} from "firebase/auth";
import axios from "axios";

export default {
  name: "LandingPage",
  components: {
    'ejs-toolbar': ToolbarComponent,
    'e-item': ItemDirective,
    'e-items': ItemsDirective,
    'ejs-button': ButtonComponent,
    'ejs-dialog': DialogComponent,
    'ejs-menu': MenuComponent,
  },
  data() {
    return {
      isShowIcon: true,
      isTypePassword: true,
      email: '',
      password: '',
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
          text: 'Profile',
          items: [
            {
              text: 'Sign out',
              click: this.signOut
            },
            {
              text: 'Verify email',
              click: this.verifyEmail
            }
          ]
        }
      ]
    };
  },
  mounted() {
    window.onscroll = function() {
      const currentScrollPos = document.documentElement.scrollTop;
      if (store.getters.scrollPosition > currentScrollPos) {
        document.getElementsByClassName("e-toolbar")[0].style.top = "0px";
      } else {
        document.getElementsByClassName("e-toolbar")[0].style.top = "-80px";
      }
      store.commit('setScrollPosition', currentScrollPos);
    }
    this.$nextTick(() => {
      const dialogOverlay = document.getElementsByClassName("e-dlg-overlay")[0];
      dialogOverlay.style.position = 'fixed';
      const dialog = document.getElementsByClassName("e-dialog")[0];
      dialog.style.position = 'fixed';
      dialog.style.left = ''
      dialog.style.maxHeight = '100%';
      dialog.style.top = '';
      console.log(dialog);
    });
    onIdTokenChanged(auth, async (user) => {
      user?.getIdToken().then(token => {
        store.commit('setToken', token);
      });
    });
  },
  methods: {
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
      }).catch(error => {
        alert(error.message)
      })
    },
    signUp(){
      createUserWithEmailAndPassword(auth, this.email, this.password).then(async (userCredentials) => {
        await axios.post(`${process.env.VUE_APP_BACKEND}/user`, {email: this.email}).catch(error => {
          deleteUser(userCredentials.user);
        });
        await sendEmailVerification(userCredentials.user)
        userCredentials.user.getIdToken().then((token) => {
          store.commit('setToken', token);
        });
        this.$refs.signInDialog.hide();
      }).catch(error => {
        alert(error.message)
      })
    },
    signOut(){
      signOut(auth).then(() => {
        store.commit('setToken', null);
      }).catch(error => {
        alert(error.message)
      })
    },
    menuItemSelected(e){
      if(e.item.text === 'Sign out'){
        this.signOut();
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

</style>