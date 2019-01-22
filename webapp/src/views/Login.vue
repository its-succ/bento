<template>
  <div class="login">
    <div>利用するにはesm.co.jpアカウントにログインしてください</div>
    <button @click="signOut">out</button>
    <div id="google-signin-btn"></div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";

export default {
  name: "login",
  computed: mapState(["isAuthInit", "isSignedIn"]),
  watch: {
    isAuthInit() {
      window.gapi.signin2.render("google-signin-btn", {
        onsuccess: this.updateSignInState
      });
    }
  },
  methods: {
    ...mapActions(["updateSignInState", "signOut"])
  }
};
</script>
