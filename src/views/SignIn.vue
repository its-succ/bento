<template>
  <div class="home">
    <h1>本社弁当注文</h1>
    <sign-in />
  </div>
</template>

<script>
import SignIn from "@/components/SignIn.vue";

export default {
  name: "home",
  components: {
    "sign-in" : SignIn
  }
};
</script>
