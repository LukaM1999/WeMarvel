<template>
<div class="row">
  <div class="col">
    <div v-if="!isSelf" class="row me-2 mt-2 mb-2">
      <div class="col d-flex justify-content-end">
        <ejs-button @click="openReportForm()" iconCss="e-icons e-description" content="Report"></ejs-button>
      </div>
    </div>
    <div class="row justify-content-center">
      <div class="col">
        <img width="300" height="300" :src="profile.imageUrl ? profile.imageUrl : '/placeholder.jpg'"
             :alt="`${profile.username}'s picture`" :title="profile.username"/>
      </div>
    </div>
    <div class="row justify-content-center mt-2">
      <div class="col-1 d-flex justify-content-start">
        <b class="label">Gender:</b>
      </div>
      <div class="col-1 align-self-center">
        {{profile.gender ? formatGender(profile.gender) : 'Unknown'}}
      </div>
    </div>
    <div class="row justify-content-center mt-2">
      <div class="col-1 d-flex justify-content-start">
        <b class="label">Location:</b>
      </div>
      <div class="col-1 align-self-center">
        {{profile.location ? profile.location : 'Unknown'}}
      </div>
    </div>
    <div class="row justify-content-center mt-2">
      <div class="col-1 d-flex justify-content-start">
        <b class="label">Birthday:</b>
      </div>
      <div class="col-1 align-self-center">
        {{profile. birthday ? profile.birthday : 'Unknown'}}
      </div>
    </div>
    <div class="row justify-content-center mb-2 mt-2">
      <div v-if="!isSelf" class="col-1 d-flex justify-content-center">
        <ejs-button v-if="!friend" :content="'Add friend'" :is-primary="true"
                    @click="sendFriendRequest" :iconCss="'e-icons e-plus-2'"></ejs-button>
        <ejs-button v-else-if="!friend.accepted"
                    :content="friend.receiverId === profile.id ? 'Cancel friend request' : 'Accept friend request'"
                    :is-primary="true"
                    @click="friend.receiverId === profile.id ? removeFriend() : acceptFriendRequest()"
                    :iconCss="[friend.receiverId === profile.id ? 'e-icons e-close' : 'e-icons e-plus-2']"></ejs-button>
        <ejs-button v-else :content="'Remove friend'" :is-primary="true"
                    @click="removeFriend" :iconCss="'e-icons e-close'"></ejs-button>
        <ejs-button class="ms-2" v-if="friend && !friend.accepted && friend.receiverId !== profile.id"
                    :content="'Decline friend request'"
                    @click="removeFriend" :iconCss="'e-icons e-close'"></ejs-button>
      </div>
    </div>
    <ejs-dialog ref="reportForm" header="Report user" :showCloseIcon="true" :visible="showReportForm"
                :width="'500px'" :height="'400px'" :isModal="true" :closeOnEscape="true"
                :position="{ X: 'center', Y: 'center' }"
                :content="'contentTemplate'"
                :footerTemplate="'footerTemplate'"
                @close="dialogClosed">
      <template v-slot:contentTemplate>
        <ejs-textbox ref="reportExplanation" v-model="reportExplanation" :placeholder="'Report explanation*'"
                     :multiline="true" :rows="10" :floatLabelType="'Auto'"></ejs-textbox>
      </template>
      <template v-slot:footerTemplate>
        <div class="row justify-content-end">
          <div class="col-3">
            <ejs-button ref="reportButton" isPrimary="true" :enabled="reportExplanation"
                        @click="reportUser" :content="'Submit'"></ejs-button>
          </div>
          <div class="col-3">
            <ejs-button @click="dialogClosed" :content="'Cancel'"></ejs-button>
          </div>
        </div>
      </template>
    </ejs-dialog>
  </div>
</div>
</template>

<script>
import {capitalize} from "eslint-plugin-vue/lib/utils/casing";
import {ButtonComponent} from "@syncfusion/ej2-vue-buttons";
import axios from "axios";
import {auth} from "@/firebaseServices/firebaseConfig";
import {onIdTokenChanged} from "firebase/auth";
import {ToastUtility} from "@syncfusion/ej2-vue-notifications";
import {store} from "@/main";
import {DialogComponent} from "@syncfusion/ej2-vue-popups";
import {TextBoxComponent} from "@syncfusion/ej2-vue-inputs";

export default {
  name: "ProfileOverview",
  components: {
    "ejs-button": ButtonComponent,
    'ejs-dialog': DialogComponent,
    'ejs-textbox': TextBoxComponent
  },
  props: {
    profile: {
      type: Object,
      required: true,
    }
  },
  data(){
    return {
      friend: null,
      isSelf: false,
      showReportForm: false,
      reportExplanation: '',
    }
  },
  async mounted(){
    await this.getFriend();
    onIdTokenChanged(auth, async () => {
      this.isSelf = this.profile.username === auth.currentUser.displayName;
    });
  },
  methods: {
    async getFriend(){
      const {data} = await axios.get(`${process.env.VUE_APP_BACKEND}/friend/${this.profile.username}`);
      this.friend = data;
    },
    async sendFriendRequest(){
      await axios.post(`${process.env.VUE_APP_BACKEND}/friend/${this.profile.username}`);
      await this.getFriend();
      ToastUtility.show({
        icon: 'e-icons e-success toast-icon',
        title: 'Request sent',
        content: 'Friend request successfully sent',
        position: {X: document.body.offsetWidth - 360, Y: 80},
        showCloseButton: true,
        timeOut: 5000,
        extendedTimeout: 5000,
      });
      await axios.post(`${process.env.VUE_APP_BACKEND}/notification/friend`, {
        type: 'new_friend_request',
        recipientId: this.profile.id,
        recipientUsername: this.profile.username,
        socketId: store.getters.socketId,
      });
    },
    async removeFriend(){
      await axios.delete(`${process.env.VUE_APP_BACKEND}/friend/${this.friend.id}`);
      ToastUtility.show({
        cssClass: 'e-toast-success',
        title: this.friend.accepted ? 'Friend removed' : 'Friend request cancelled',
        content: this.friend.accepted ? 'You are no longer friends with this user' : 'Friend request successfully cancelled',
        position: {X: document.body.offsetWidth - 360, Y: 80},
        showCloseButton: true,
        timeOut: 5000,
        extendedTimeout: 5000,
      });
      this.friend = null;
    },
    async acceptFriendRequest(){
      await axios.patch(`${process.env.VUE_APP_BACKEND}/friend/${this.friend.id}`);
      this.friend.accepted = true;
      ToastUtility.show({
        cssClass: 'e-toast-success',
        title: 'Request accepted',
        content: 'Friend request successfully accepted',
        position: {X: document.body.offsetWidth - 360, Y: 80},
        showCloseButton: true,
        timeOut: 5000,
        extendedTimeout: 5000,
      });
      await axios.post(`${process.env.VUE_APP_BACKEND}/notification/friend`, {
        type: 'accepted_friend_request',
        recipientId: this.profile.id,
        recipientUsername: this.profile.username,
        socketId: store.getters.socketId,
      });
    },
    formatGender(gender){
      if(!gender) return 'Unknown';
      return capitalize(gender.toLowerCase().replace('_', '-'));
    },
    openReportForm(postId){
      this.showReportForm = true;
      this.reportedPostId = postId;
    },
    dialogClosed(){
      this.showReportForm = false;
      this.reportExplanation = '';
    },
    async reportUser(){
      await axios.post(`${process.env.VUE_APP_BACKEND}/report/user`, {
        reportedUsername: this.profile.username,
        explanation: this.reportExplanation,
      });
      this.showReportForm = false;
      this.reportedPostId = '';
      this.reportExplanation = '';
      ToastUtility.show({
        title: 'Report sent',
        content: 'Report sent successfully',
        position: {X: document.body.offsetWidth - 360, Y: 80},
        cssClass: 'e-toast-success',
        showCloseButton: true,
        timeOut: 5000,
        extendedTimeout: 5000,
      });
    }
  }
}
</script>

<style scoped>

</style>