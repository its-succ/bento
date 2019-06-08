<template>
  <div class="admin">
    <sign-in />
  </div>
</template>

<script>
import SignIn from "@/components/SignIn.vue";

export default {
  name: "admin",
  components: {
    "sign-in" : SignIn
  },
}
</script>
