<template>
  <div class="signIn">
    <h1>本社弁当注文</h1>
    <sign-in />
  </div>
</template>

<script>
import SignIn from "@/components/SignIn.vue";

export default {
  name: "signIn",
  components: {
    "sign-in" : SignIn
  }
};
</script>
