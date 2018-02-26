<template>
  <div class="login">
    <div id="google-signin-btn"></div>
  </div>
</template>

<script>
import auth from '../google-auth'
export default {
  methods: {
    onSignIn (googleUser) {
      const profile = googleUser.getBasicProfile()
      const user = {
        id: profile.getId(),
        name: profile.getName(),
        id_token: googleUser.getAuthResponse().id_token
      }
      this.$emit('signed-in', user)

      if (this.$route.query.redirect === undefined) {
        this.$router.push('/')
      }
      else {
        this.$router.push(this.$route.query.redirect)
      }
    }
  },
  async mounted () {
    await auth.renderSignInButton('google-signin-btn', {
      onsuccess: this.onSignIn
    })
  }
}
</script>
