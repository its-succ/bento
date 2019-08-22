<template>
  <div class="signin">
    <v-btn v-if="this.isAuth === true" @click="signOut">ログアウト</v-btn>
    <v-btn v-if="this.isAuth === false" @click="signIn">ログイン</v-btn>
  </div>
</template>

<script>
import firebase from "firebase";

export default {
  data() {
    return {
      isAuth: undefined
    };
  },
  methods: {
    signIn: function() {
      const provider = new firebase.auth.GoogleAuthProvider();
      firebase.auth().signInWithPopup(provider);
    },
    signOut: function() {
      firebase.auth().signOut();
    }
  },
  mounted() {
    firebase.auth().onAuthStateChanged(user => {
      this.isAuth = !!user;
      this.$emit(this.isAuth ? "sign-in" : "sign-out");
    });
  }
};
</script>
