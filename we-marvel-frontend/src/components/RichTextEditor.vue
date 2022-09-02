<template>
  <div id="rteContainer" class="row justify-content-center">
    <div class="col-8">
      <ejs-richtexteditor id="rte" ref="rteInstance"
                          showCharCount="true" maxLength="20000"
                          enableResize="true"
                          :value="initialValue"
                          :toolbarSettings="toolbarSettings"
                          v-model="rteValue"
                          :insertImageSettings="insertImageSettings"
                          @change="onChange"
                          @actionBegin="actionBegin"
                          @imageUploadSuccess="imageUploadSuccess"
                          @imageRemoving="imageRemoving"
                          @afterImageDelete="afterImageDelete">
      </ejs-richtexteditor>
    </div>
  </div>
</template>

<script>
import {
  Count,
  HtmlEditor,
  Image,
  Link,
  QuickToolbar, Resize,
  RichTextEditorComponent,
  Table,
  Toolbar
} from "@syncfusion/ej2-vue-richtexteditor";
import axios from "axios";
import {auth} from "@/firebaseServices/firebaseConfig";
import { getStorage, ref, getDownloadURL, deleteObject } from "firebase/storage";

export default {
  name: "RichTextEditor",
  data() {
    return {
      toolbarSettings: {
        items: ['Bold', 'Italic', 'Underline', 'StrikeThrough',
          'FontName', 'FontSize', 'FontColor', 'BackgroundColor', '|',
          'Formats', 'Alignments', 'NumberFormatList', 'BulletFormatList',
          'Outdent', 'Indent', '|',
          'CreateTable', 'CreateLink', 'Image', '|', 'ClearFormat', 'Undo', 'Redo', '|', 'SourceCode', 'FullScreen']
      },
      rteValue: '',
      insertImageSettings: {
        allowedTypes: ['.jpg', '.jpeg', '.png', '.gif', '.bmp'],
        saveUrl: `${process.env.VUE_APP_BACKEND}/forum/post/image`,
      },
      imageName: '',
    };
  },
  components: {
    'ejs-richtexteditor': RichTextEditorComponent,
  },
  provide: {
    richtexteditor: [HtmlEditor, Toolbar, Link, Image, Table, QuickToolbar, Count, Resize],
  },
  props: {
    initialValue: {
      type: String,
      required: false,
      default: "",
    },
  },
  mounted() {
    this.rteValue = this.initialValue;
    this.$refs.rteInstance.updated();
    document.querySelectorAll('input[id^="rte_toolbar"]').forEach((el) => {
      el.style.visibility = 'hidden';
    });
  },
  methods: {
    actionBegin(args) {
      if(args.requestType === 'Image'){
        const storage = getStorage();
        if(!this.imageName) return
        getDownloadURL(ref(storage, this.imageName)).then(url => {
          const imageElement = document.querySelectorAll(`img[src="${args.itemCollection.url}"]`)[0]
          if(!imageElement) return;
          imageElement.src = url;
          imageElement.name = this.imageName;
        }).catch(error => {
          console.log(error);
        });
      }
    },
    imageUploadSuccess(args) {
      this.imageName = args.e.currentTarget?.responseText;
      const storage = getStorage();
      getDownloadURL(ref(storage, this.imageName)).then(url => {
        args.element.src = url;
        args.element.name = this.imageName;
      }).catch(error => {
        console.log(error);
      });
    },
    afterImageDelete(args) {
      if(!args.element.name) return
      const storage = getStorage();
      deleteObject(ref(storage, args.element.name));
    },
    imageRemoving(args) {
      const storage = getStorage();
      deleteObject(ref(storage, this.imageName));
    },
    onChange(){
      this.$emit('value-changed', this.rteValue);
    }
  }
}
</script>

<style>

#rteContainer .e-toolbar .e-tbar-btn {
  background: #fafafa;
}

#rteContainer .e-toolbar .e-toolbar-items{
  background: #fafafa;
}

#rteContainer .e-toolbar .e-tbar-btn:active {
  background: #bdbdbd;
  border-color: #bdbdbd;
  border-radius: 0;
  box-shadow: none;
  color: rgba(0, 0, 0, 0.87);
}

#rteContainer .e-toolbar .e-tbar-btn:hover {
  background: rgba(0, 0, 0, 0.12);
  border-color: rgba(0, 0, 0, 0.12);
  border-radius: 0;
  color: rgba(0, 0, 0, 0.87);
}

#rteContainer .e-richtexteditor .e-rte-toolbar .e-toolbar-item.e-active .e-tbar-btn, .e-richtexteditor .e-rte-toolbar .e-toolbar-item.e-active .e-tbar-btn:focus {
  background: rgba(0, 0, 0, 0.12);
  border: 0;
}

#rteContainer .e-toolbar .e-tbar-btn {
  color: initial;
}

</style>