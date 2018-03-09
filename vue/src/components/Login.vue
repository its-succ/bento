<template>
  <div class="login">
    <div id="content-view">
      <p class="q-subheading">利用するにはesm.co.jpドメインにログインしてください</p>
      <div>
        <div id="google-signin-btn"></div>
      </div>
    </div>
  </div>
</template>

<script>
import auth from '../google-auth'
export default {
  methods: {
    async onSignIn (googleUser) {
      const profile = googleUser.getBasicProfile()
      const user = {
        id: profile.getId(),
        name: profile.getName(),
        id_token: googleUser.getAuthResponse().id_token
      }
      try {
        // サインイン後、サーバへログイン
        const params = new URLSearchParams()
        params.append('google_id_token', user.id_token)
        await this.$http.post('/login', params)
      }
      catch (error) {
        await auth.signOut()
        return
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

<style>

</style>
