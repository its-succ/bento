import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import firebase from "firebase";

const firebaseConfig = {
  apiKey: "AIzaSyA_jueY2J6SGNwOrIk45A5CWLZqNlEOcC8",
  authDomain: "bento-184202.firebaseapp.com",
  databaseURL: "https://bento-184202.firebaseio.com",
  projectId: "bento-184202",
  storageBucket: "bento-184202.appspot.com",
  messagingSenderId: "599875423654",
  appId: "1:599875423654:web:5ec328cb63bd7472"
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);

Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");
