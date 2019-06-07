<template>
  <div class="signin">
    <a @click="signIn" class="button--green">signIn</a>
  </div>
</template>


<script>
import firebase from "firebase";

export default {
  methods: {
    signIn: function() {
      const provider = new firebase.auth.GoogleAuthProvider();
      firebase.auth().signInWithRedirect(provider);
    }
  }
};
</script>
