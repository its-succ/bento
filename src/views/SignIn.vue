<template>
  <div class="signIn">
    <v-responsive>
      <v-container fill-height>
        <v-layout align-center>
          <v-flex>
            <h3 class="display-3">Welcome to Bento</h3>
            <span class="subheading">福井本社のお弁当注文できます.</span>
            <v-divider class="my-3"></v-divider>
            <sign-in class="mx-0" large v-on:sign-in="onSignIn" />
          </v-flex>
        </v-layout>
      </v-container>
    </v-responsive>
  </div>
</template>

<script>
import SignIn from "@/components/SignIn.vue";

export default {
  name: "signIn",
  components: {
    "sign-in": SignIn
  },
  methods: {
    onSignIn() {
      this.$router.push("home");
    }
  }
};
</script>
