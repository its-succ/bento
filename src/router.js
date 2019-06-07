import Vue from "vue";
import Router from "vue-router";
import SignIn from "@/views/SignIn.vue";
import Home from "@/views/Home.vue";

Vue.use(Router);

export default new Router({
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
