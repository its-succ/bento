import Vue from "vue";
import Router from "vue-router";
import SignIn from "@/views/SignIn.vue";
import Home from "@/views/Home.vue";
import Order from "@/views/Order.vue";
import Admin from "@/views/Admin.vue";
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
    },
    {
      path: "/admin",
      name: "admin",
      component: Admin
    }
  ]
});

router.beforeResolve(async (to, from, next) => {
  if (to.path === "/") {
    next();
    return;
  }
  const user = firebase.auth().currentUser;
  if (!user) {
    next({ path: "/" });
    return;
  }

  const db = firebase.firestore();
  const admin = await db.doc("settings/admin").get();
  const isAdmin = admin.data().uids.includes(user.uid);
  if (isAdmin) {
    if (to.path === "/admin") {
      next();
      return;
    }
    next({ path: "/admin" });
    return;
  }
  next();
});

export default router;
