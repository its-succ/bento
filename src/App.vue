<template>
  <v-app id="app">
    <v-toolbar color="#234390" dark fixed app>
      <v-toolbar-title>Bento</v-toolbar-title>
      <v-spacer/>
      <v-toolbar-items>
        <v-btn flat @click="onSignOut">ログアウト</v-btn>
      </v-toolbar-items>
    </v-toolbar>
    <v-content>
      <router-view/>
    </v-content>
  </v-app>
</template>

<script>
import firebase from "firebase";

export default {
  created() {
    firebase.auth().onAuthStateChanged(async user => {
      if (user) {
        const db = firebase.firestore();
        const doc = db.doc(`users/${user.uid}`);
        await doc.set({ name: user.displayName });
      } else {
        this.$router.push({ path: "/" });
      }
    });
  },
  methods: {
    onSignOut() {
      firebase.auth().signOut();
    }
  }
};
</script>
