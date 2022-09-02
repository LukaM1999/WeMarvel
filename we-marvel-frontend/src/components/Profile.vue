<template>
<div id="profileContainer" style="overflow-x: hidden">
  <h1>{{profile.username}}'s Profile</h1>
  <ejs-tab v-if="profile.username" ref="tabs" :selected="tabSelected" class="e-fill">
    <e-tabitems>
      <e-tabitem :header="{text: 'Overview'}" :content="'overviewTemplate'">
        <template v-slot:overviewTemplate="{}">
          <ProfileOverview v-if="profile.username" :profile="profile" />
        </template>
      </e-tabitem>
      <e-tabitem :header="{text: 'Comics'}" :content="'comicsTemplate'">
        <template v-slot:comicsTemplate="{}">
          <ComicProgress :username="profile.username" :authorized="isAuthorized"/>
        </template>
      </e-tabitem>
      <e-tabitem :header="{text: 'Reviews'}" :content="'reviewsTemplate'">
        <template v-slot:reviewsTemplate="{}">
          <Reviews :key="reviewsKey" @review-submitted="reviewSubmitted"
                   v-if="editSettings"
                   :edit-settings="editSettings"
                   :toolbar="toolbar"
                   :reviews="reviews"/>
        </template>
      </e-tabitem>
      <e-tabitem :header="{text: 'Friends'}" :content="'friendsTemplate'">
        <template v-slot:friendsTemplate="{}">
          <Users v-if="profile?.username" :username="profile.username"></Users>
        </template>
      </e-tabitem>
      <e-tabitem v-if="isAuthorized" :header="{text: 'Friend requests'}" :content="'friendRequestsTemplate'">
        <template v-slot:friendRequestsTemplate="{}">
          <FriendRequests ref="friendRequestsRef"/>
        </template>
      </e-tabitem>
      <e-tabitem v-if="isAuthorized" :header="{text: 'Messages'}" :content="'messagesTemplate'">
        <template v-slot:messagesTemplate="{}">
          <Messages ref="messagesRef" :current-user-prop="profile"/>
        </template>
      </e-tabitem>
      <e-tabitem v-if="isAuthorized" :header="{text: 'Notifications'}" :content="'notificationsTemplate'">
        <template v-slot:notificationsTemplate="{}">
          <Notifications ref="notificationsRef"/>
        </template>
      </e-tabitem>
      <e-tabitem v-if="isAuthorized" :header="{text: 'Settings'}" :content="'settingsTemplate'">
        <template v-slot:settingsTemplate="{}">
          <div class="row">
            <div class="col">
              <h2>Account</h2>
              <div class="row mb-3">
                <div class="col">
                  <div class="row">
                    <div class="col">
                      <b class="label">Change password</b>
                    </div>
                    <div class="col">
                      <div v-if="showPasswordResetForm" class="e-control e-lib e-primary">
                        <div class="e-input-group e-float-input">
                          <input :type="isTypePassword ? 'password' : 'text'"
                                 v-model="password" required />
                          <label class="e-float-text e-label">Password</label>
                          <span class="e-input-group-icon e-password" @click="changeIcon">
                            <span :class="isShowIcon ? 'e-icons e-eye' : 'e-icons e-eye-slash'" style="color: gray"></span>
                          </span>
                        </div>
                      </div>
                      <div v-if="showPasswordResetForm" class="e-control e-lib e-primary mt-3">
                        <div class="e-input-group e-float-input">
                          <input :type="isTypePassword ? 'password' : 'text'"
                                 v-model="confirmPassword" required />
                          <label class="e-float-text e-label">Confirm password</label>
                        </div>
                      </div>
                      <ejs-button :disabled="!isPasswordFormValid()" class="mt-3" v-if="showPasswordResetForm" @click="changePassword">Change password</ejs-button>
                      <ejs-button v-if="!showPasswordResetForm" @click="sendPasswordResetEmail">Send password reset email</ejs-button>
                    </div>
                  </div>
                  <div class="row mt-3">
                    <div class="col">
                      <b class="label">Change username</b>
                    </div>
                    <div class="col">
                      <div class="e-control e-lib e-primary">
                        <div class="e-input-group e-float-input">
                          <input v-model="username" required />
                          <label class="e-float-text e-label">Username</label>
                        </div>
                      </div>
                      <ejs-button :disabled="!isUsernameFormValid()" class="mt-3" @click="changeUsername">Change username</ejs-button>
                    </div>
                  </div>
                </div>
              </div>
              <h2>Picture</h2>
              <div class="row mb-3">
                <div class="col">
                  <div class="row">
                    <div class="col">
                      <img width="300" height="300" :src="imageUrl ? imageUrl : '/placeholder.jpg'" alt="Profile picture">
                    </div>
                  </div>
                  <div class="row justify-content-center mt-3">
                    <div class="col-6">
                      <ejs-uploader ref="uploader"
                                    allowedExtensions=".jpg, .jpeg, .bmp, .png, .gif"
                                    :autoUpload="false" maxFileSize="5000000"
                                    :multiple="false" :selected="imageSelected"
                                    :asyncSettings="path"
                                    :beforeUpload="beforeUpload"
                                    :clearing="clearingFile" :removing="removing">
                        <e-files v-if="imageInfo.size > 0">
                          <e-uploadedfiles name='profile-picture' :size="imageInfo.size"
                                           :type="imageInfo.contentType"></e-uploadedfiles>
                        </e-files>
                      </ejs-uploader>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="col">
              <h2>Profile info</h2>
              <div class="row justify-content-center mb-2">
                <div class="col-2">
                  <b class="label">Birthday:</b>
                </div>
                <div class="col-4">
                  <ejs-datepicker :value="profile.birthday" v-model="profile.birthday" :format="'dd.MM.yyyy.'"
                  :enableMask="true" :maskPlaceholder="maskPlaceholder"></ejs-datepicker>
                </div>
              </div>
              <div class="row justify-content-center mb-2">
                <div class="col-2">
                  <b class="label">Gender:</b>
                </div>
                <div class="col-4">
                  <ejs-combobox :value="profile.gender"
                                v-model="profile.gender"
                                :dataSource="genders" :fields="{text: 'text', value: 'value'}"></ejs-combobox>
                </div>
              </div>
              <div class="row justify-content-center mb-2">
                <div class="col-2">
                  <b class="label">Location:</b>
                </div>
                <div class="col-4">
                  <div class="e-control e-lib e-primary">
                    <div class="e-input-group e-float-input">
                      <input v-model="profile.location" />
                      <label class="e-float-text e-label">Location</label>
                    </div>
                  </div>
                </div>
              </div>
              <div class="row mt-3 mb-5">
                <div class="col">
                  <ejs-button :disabled="!isProfileInfoFormValid()" @click="changeProfileInfo">Save profile info</ejs-button>
                </div>
              </div>
              <h2>Notifications</h2>
              <div class="row justify-content-center mb-2">
                <div class="col-2 d-flex">
                  <b class="label">Topics:</b>
                </div>
                <div class="col-1">
                  <ejs-switch :value="notificationSettings.topics" :checked="notificationSettings.topics" v-model="notificationSettings.topics"></ejs-switch>
                </div>
              </div>
              <div class="row justify-content-center mb-2">
                <div class="col-2 d-flex">
                  <b class="label">Messages:</b>
                </div>
                <div class="col-1">
                  <ejs-switch :value="notificationSettings.messages" :checked="notificationSettings.messages" v-model="notificationSettings.messages"></ejs-switch>
                </div>
              </div>
              <div class="row justify-content-center mb-2">
                <div class="col-2 d-flex text-nowrap">
                  <b class="label">Friend requests:</b>
                </div>
                <div class="col-1">
                  <ejs-switch :value="notificationSettings.friendRequests" :checked="notificationSettings.friendRequests" v-model="notificationSettings.friendRequests"></ejs-switch>
                </div>
              </div>
              <div class="row mt-3">
                <div class="col">
                  <ejs-button :disabled="!changesExist()" @click="saveNotificationSettings">Save notifications</ejs-button>
                </div>
              </div>
            </div>
          </div>
        </template>
      </e-tabitem>
    </e-tabitems>
  </ejs-tab>
</div>
</template>

<script>
import {TabComponent, TabItemDirective, TabItemsDirective} from "@syncfusion/ej2-vue-navigations";
import {ButtonComponent, SwitchComponent} from "@syncfusion/ej2-vue-buttons";
import axios from "axios";
import {auth} from "@/firebaseServices/firebaseConfig";
import {ToastUtility} from "@syncfusion/ej2-vue-notifications";
import {
  verifyPasswordResetCode, sendPasswordResetEmail,
  confirmPasswordReset, onIdTokenChanged, updateProfile, getIdTokenResult
} from "firebase/auth";
import {UploaderComponent, FilesDirective, UploadedFilesDirective} from "@syncfusion/ej2-vue-inputs";
import {getDownloadURL, getStorage, ref, uploadBytes, getMetadata, deleteObject} from "firebase/storage";
import {DatePickerComponent, MaskedDateTime} from '@syncfusion/ej2-vue-calendars'
import { ComboBoxComponent } from "@syncfusion/ej2-vue-dropdowns";
import moment from "moment";
import ComicProgress from "@/components/ComicProgress";
import ProfileOverview from "@/components/ProfileOverview";
import Users from "@/components/Users";
import FriendRequests from "@/components/FriendRequests";
import Reviews from "@/components/Reviews";
import Messages from "@/components/Messages";
import Notifications from "@/components/Notifications";


export default {
  name: "Profile",
  components: {
    Reviews,
    Users,
    ProfileOverview,
    ComicProgress,
    FriendRequests,
    Messages,
    Notifications,
    "ejs-tab": TabComponent,
    "e-tabitems": TabItemsDirective,
    "e-tabitem": TabItemDirective,
    "ejs-switch": SwitchComponent,
    "ejs-button": ButtonComponent,
    "ejs-uploader": UploaderComponent,
    'ejs-datepicker' : DatePickerComponent,
    'ejs-combobox': ComboBoxComponent,
    'e-files': FilesDirective,
    'e-uploadedfiles': UploadedFilesDirective,
  },
  data(){
    return {
      oldNotificationSettings: {
        topics: true,
        messages: true,
        friendRequests: true
      },
      notificationSettings: {
        topics: true,
        messages: true,
        friendRequests: true,
      },
      profile: {
        gender: '',
        birthday: '',
        location: '',
      },
      genders: [
        {text: 'Not specified', value: null},
        {text: 'Male', value: 'MALE'},
        {text: 'Female', value: 'FEMALE'},
        {text: 'Non-binary', value: 'NON_BINARY'},
      ],
      tabs: new Map([
        ['profile', 0],
        ['comics', 1],
        ['reviews', 2],
        ['friends', 3],
        ['friend requests', 4],
        ['messages', 5],
        ['notifications', 6],
        ['settings', 7],
      ]),
      isTypePassword: true,
      isShowIcon: true,
      showPasswordResetForm: false,
      oobCode: '',
      password: '',
      confirmPassword: '',
      username: '',
      imageUrl: '',
      path:  {
        saveUrl: 'https://ej2.syncfusion.com/services/api/uploadbox/Save',
        removeUrl: 'https://ej2.syncfusion.com/services/api/uploadbox/Remove'
      },
      isAuthorized: false,
      gender: '',
      birthday: '',
      location: '',
      imageInfo: {
        size: 0,
        contentType: 'png'
      },
      maskPlaceholder: {day: 'dd', month: 'mm', year: 'yyyy'},
      reviews: [],
      toolbar: ['Search'],
      editSettings: {},
      reviewsKey: 0,
      isAdmin: false,
    }
  },
  async mounted() {
    await this.getProfileInfo();
    onIdTokenChanged(auth, async (user) => {
      this.isAuthorized = user.displayName === this.$route.params.username;
      getIdTokenResult(user).then((idTokenResult) => {
        this.isAdmin = idTokenResult.claims.admin;
      });
      this.imageUrl = user.photoURL;
      if(this.$route.query.tab) {
        await this.$nextTick(() => {
          this.$refs.tabs.select(this.tabs.get(this.$route.query.tab));
        });
      }
      if(!this.imageUrl) return;
      const storage = getStorage()
      this.imageInfo = await getMetadata(ref(storage, this.imageUrl))
      this.imageInfo.contentType = "." + this.imageInfo.contentType.split('/')[1];
    });
    if(this.$route.query.mode === 'resetPassword' && this.$route.query.oobCode) {
      this.$refs.tabs.select(7);
      this.oobCode = this.$route.query.oobCode;
      const email = await verifyPasswordResetCode(auth, this.oobCode);
      if (!email) return;
      this.showPasswordResetForm = true;
    }
  },
  methods: {
    async tabSelected(e) {
      this.$router.push({query: {tab: e.selectedItem.innerText.toLowerCase()}});
      if(e.selectedIndex === 2){
        if(this.isAuthorized || this.isAdmin) {
          this.toolbar = ['Search', 'Add'];
          this.editSettings = {
            allowAdding: true,
            mode: 'Dialog',
          }
        }
        await this.getReviews();
      }
      else if(e.selectedIndex === 7){
        await this.getNotificationSettings();
      }
    },
    reviewSubmitted(review){
      this.reviews.unshift(review);
      this.reviewsKey++;
    },
    async getReviews(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/review/user/${this.$route.params.username}`);
      for(let r of data){
        const lastDot = r.marvelEntityThumbnail.lastIndexOf(".");
        r.marvelEntityThumbnail = r.marvelEntityThumbnail.substring(0, lastDot) + "/portrait_small" + r.marvelEntityThumbnail.substring(lastDot);
      }
      this.reviews = data;
    },
    async getNotificationSettings(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/notification/settings`);
      this.notificationSettings = data;
      this.oldNotificationSettings = Object.assign({}, data);
    },
    async getProfileInfo(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/user/${this.$route.params.username}`);
      const newData = this.replaceNullWithEmpty(data);
      this.profile = newData;
      this.gender = newData.gender;
      this.birthday = newData.birthday;
      this.location = newData.location;
    },
    replaceNullWithEmpty(obj){
      const newObj = {}
      for(let key in obj){
        if(obj[key] === null) {
          newObj[key] = '';
          continue;
        }
        newObj[key] = obj[key];
      }
      return newObj;
    },
    changesExist(){
      return this.notificationSettings.topics !== this.oldNotificationSettings.topics ||
          this.notificationSettings.messages !== this.oldNotificationSettings.messages ||
          this.notificationSettings.friendRequests !== this.oldNotificationSettings.friendRequests;
    },
    async saveNotificationSettings(){
      await axios.put(`${process.env.VUE_APP_BACKEND}/notification/settings`, this.notificationSettings);
      this.oldNotificationSettings = Object.assign({}, this.notificationSettings);
      await auth.currentUser.getIdToken(true);
    },
    changeIcon() {
      this.isShowIcon = !this.isShowIcon;
      this.isTypePassword = !this.isTypePassword;
    },
    isPasswordFormValid(){
      if(!this.password || !this.confirmPassword) return false;
      return this.password === this.confirmPassword;
    },
    isUsernameFormValid(){
      return this.username.length > 0 && this.username !== auth.currentUser.displayName;
    },
    isProfileInfoFormValid(){
      if(!moment(this.profile.birthday).isValid()) return false;
      const newBirthday = moment(this.profile.birthday).format('DD.MM.yyyy.');
      return newBirthday !== String(this.birthday) ||
          this.profile.location !== String(this.location) ||
          this.profile.gender !== String(this.gender);
    },
    async sendPasswordResetEmail(){
      await sendPasswordResetEmail(auth, auth.currentUser.email)
      ToastUtility.show({
        title: 'Password reset email sent',
        content: 'Check your email for the password reset link',
        cssClass: 'e-toast-success',
        icon: 'e-success e-icons',
        position: {X: document.body.offsetWidth - 360, Y: 80},
        showCloseButton: true,
        timeOut: 7000,
        extendedTimeout: 5000,
        animation: {show: {effect: 'SlideRightIn'}, hide: {effect: 'SlideRightOut'}},
      });
    },
    async changePassword(){
      if(!this.isPasswordFormValid()) return;
      await confirmPasswordReset(auth, this.oobCode, this.password);
      ToastUtility.show({
        title: 'Password changed',
        content: 'Please sign in again',
        cssClass: 'e-toast-success',
        icon: 'e-success e-icons',
        position: {X: document.body.offsetWidth - 360, Y: 80},
        showCloseButton: true,
        timeOut: 7000,
        extendedTimeout: 5000,
        animation: {show: {effect: 'SlideRightIn'}, hide: {effect: 'SlideRightOut'}},
      });
      this.showPasswordResetForm = false;
      this.$parent.$parent.signOut();
      this.$parent.$parent.openSignIn();
    },
    async changeUsername() {
      await updateProfile(auth.currentUser, {displayName: this.username});
      await axios.patch(`${process.env.VUE_APP_BACKEND}/user/username`, {username: this.username});
      this.$router.replace({params: {username: this.username}});
      this.username = '';
      ToastUtility.show({
        title: 'Username changed',
        content: 'Username changed successfully',
        cssClass: 'e-toast-success',
        icon: 'e-success e-icons',
        position: {X: document.body.offsetWidth - 360, Y: 80},
        showCloseButton: true,
        timeOut: 7000,
        extendedTimeout: 5000,
        animation: {show: {effect: 'SlideRightIn'}, hide: {effect: 'SlideRightOut'}},
      });
    },
    imageSelected(e){
      const reader = new FileReader();
      reader.onload = (e) => {
        this.imageUrl = e.target.result;
      };
      reader.readAsDataURL(e.filesData[0].rawFile);
    },
    async beforeUpload(e){
      e.cancel = true;
      const storage = getStorage();
      const image = this.$refs.uploader.getFilesData(0)[0];
      const {metadata} = await uploadBytes(ref(storage,
          `profile-images/${auth.currentUser.uid}`),
          image.rawFile);
      const url = await getDownloadURL(ref(storage, metadata.fullPath));
      await updateProfile(auth.currentUser, {photoURL: url});
      await axios.patch(`${process.env.VUE_APP_BACKEND}/user/image`, {imageUrl: url});
      this.imageUrl = url;
      this.imageInfo = metadata;
      this.imageInfo.contentType = '.' + metadata.contentType.split('/')[1];
      ToastUtility.show({
        title: 'Profile image changed',
        content: 'Profile image changed successfully',
        cssClass: 'e-toast-success',
        icon: 'e-success e-icons',
        position: {X: document.body.offsetWidth - 360, Y: 80},
        showCloseButton: true,
        timeOut: 7000,
        extendedTimeout: 5000,
        animation: {show: {effect: 'SlideRightIn'}, hide: {effect: 'SlideRightOut'}},
      });
    },
    async changeProfileInfo(){
      await axios.patch(`${process.env.VUE_APP_BACKEND}/user/profile`, {
        gender: this.profile.gender,
        birthday: this.profile.birthday,
        location: this.profile.location,
      });
      ToastUtility.show({
        title: 'Profile info changed',
        content: 'Profile info changed successfully',
        cssClass: 'e-toast-success',
        icon: 'e-success e-icons',
        position: {X: document.body.offsetWidth - 360, Y: 80},
        showCloseButton: true,
        timeOut: 7000,
        extendedTimeout: 5000,
        animation: {show: {effect: 'SlideRightIn'}, hide: {effect: 'SlideRightOut'}},
      });
      this.gender = this.profile.gender;
      this.location = this.profile.location;
      this.birthday = moment(this.profile.birthday).format('DD.MM.yyyy.');
    },
    async clearingFile(e){
      if(e.filesData[0]?.statusCode !== '2') {
        this.imageUrl = '';
        this.imageInfo = {size: 0, contentType: '.png'};
        return;
      }
      const storage = getStorage();
      await deleteObject(ref(storage, this.imageUrl))
      await updateProfile(auth.currentUser, {photoURL: ''});
      await axios.patch(`${process.env.VUE_APP_BACKEND}/user/image`, {imageUrl: ''});
      this.imageUrl = '';
      this.imageInfo = {size: 0, contentType: '.png'};
    },
    async removing(e){
      if(e.filesData[0]?.statusCode !== '2') {
        this.imageUrl = '';
        this.imageInfo = {size: 0, contentType: '.png'};
        return;
      }
      const storage = getStorage();
      await deleteObject(ref(storage, this.imageUrl))
      await updateProfile(auth.currentUser, {photoURL: ''});
      await axios.patch(`${process.env.VUE_APP_BACKEND}/user/image`, {imageUrl: ''});
      this.imageUrl = '';
      this.imageInfo = {size: 0, contentType: '.png'};
    }
  },
  provide: {
    datepicker: [MaskedDateTime]
  },
  watch: {
    $route: {
      deep: true,
      handler: function (to, from) {
        if(to.query.tab !== from.query.tab) {
          this.$refs.tabs.select(this.tabs.get(to.query.tab));
        }
        if(to.params.username === from.params.username) return;
        this.username = to.params.username;
        this.getProfileInfo();
        this.isAuthorized = auth.currentUser.displayName === this.username;
      },
    }
  },
}
</script>

<style>
.label {
  font-size: 16px;
}
</style>