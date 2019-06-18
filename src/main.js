import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import firebase from "firebase";
import Vuetify from "vuetify";
import "vuetify/dist/vuetify.min.css";

Vue.use(Vuetify);

const firebaseConfig = {
  apiKey: "AIzaSyCg0xo8EjH17n1eRC7uQ950jSSPWoxmUb4",
  authDomain: "bento-243007.firebaseapp.com",
  databaseURL: "https://bento-243007.firebaseio.com",
  projectId: "bento-243007",
  storageBucket: "bento-243007.appspot.com",
  messagingSenderId: "886494911661",
  appId: "1:886494911661:web:d384c1aa89e4f41d"
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App),
  created() {
    firebase.auth().onAuthStateChanged(async user => {
      if (user) {
        const db = firebase.firestore();
        const doc = db.doc(`users/${user.uid}`);
        await doc.set({ name: user.displayName });
      } else {
        this.$router.push({ path: "/" });
      }
    });
  }
}).$mount("#app");
