import Vue from "vue";
import Router from "vue-router";
import SignIn from "@/views/SignIn.vue";
import Home from "@/views/Home.vue";
import firebase from "firebase";

Vue.use(Router);

const router = new Router({
  mode: "history",
  base: process.env.BASE_URL,
  routes: [
    {
      path: "/",
      name: "signIn",
      component: SignIn
    },
    {
      path: "/home",
      name: "home",
      component: Home
    }
  ]
});

router.beforeResolve((to, from, next) => {
  if (to.path === "/") {
    next();
    return;
  }
  firebase.auth().onAuthStateChanged(user => {
    if (user) {
      next();
    } else {
      next({ path: "/" });
    }
  });
});

export default router;
