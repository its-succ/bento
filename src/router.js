import Vue from "vue";
import Router from "vue-router";
import SignIn from "@/views/SignIn.vue";
import Home from "@/views/Home.vue";
import Order from "@/views/Order.vue";
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
    },
    {
      path: "/order",
      name: "order",
      component: Order
    }
  ]
});

router.beforeResolve((to, from, next) => {
  if (to.path === "/") {
    next();
    return;
  }
  firebase.auth().onAuthStateChanged(async user => {
    if (user) {
      const db = firebase.firestore();
      const doc = db.doc(`users/${user.uid}`);
      await doc.set({ name: user.displayName });
      next();
    } else {
      next({ path: "/" });
    }
  });
});

export default router;
