<template>
  <div class="signin">
    <a v-if="this.isAuth===true" @click="signOut" class="button--grey">signOut</a>
    <a v-if="this.isAuth===false" @click="signIn" class="button--green">signIn</a>
  </div>
</template>


<script>
import firebase from "firebase";

export default {
  data () {
    return {
      isAuth: undefined,
    }
  },
  methods: {
    signIn: function() {
      const provider = new firebase.auth.GoogleAuthProvider();
      firebase.auth().signInWithPopup(provider);
      this.$emit('sign-in');
    },
    signOut: function() {
      firebase.auth().signOut();
      this.$emit('sign-out');
    }
  },
  mounted() {
    firebase.auth().onAuthStateChanged((user) => this.isAuth = !!user);
  }
};
</script>
