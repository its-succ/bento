<template>
  <div class="initial">
    <div>PLEASE WAIT...</div>
    <div>{{ isAuthInit }}</div>
    <div>{{ isSignedIn }}</div>
    <button @click="onSignInClicked">in</button>
    <button @click="onSignOutClicked">out</button>
    <div id="google-signin-btn"></div>
  </div>
</template>

<script>
// import { mapGetters } from "vuex";

export default {
  name: "initial",
  computed: {
    isAuthInit() {
      return this.$store.state.isAuthInit;
    },
    isSignedIn() {
      return this.$store.state.isSignedIn;
    }
  },
  watch: {
    isAuthInit() {
      console.log("watch init");
      window.gapi.signin2.render("google-signin-btn", {
        onsuccess: this.onSignIn
      });
    }
  },
  methods: {
    onSignInClicked() {
      this.$store.dispatch("signIn");
    },
    onSignOutClicked() {
      this.$store.dispatch("signOut");
    }
  }
};
</script>
