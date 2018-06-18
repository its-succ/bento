<template lang="html">
  <!-- Don't drop "q-app" class -->
  <div id="q-app">
    <q-layout>
      <q-toolbar slot="header">
        <q-toolbar-title class="text-bold">ぐるめし</q-toolbar-title>
        <q-btn flat icon="exit to app" @click="signOut" />
      </q-toolbar>
      <router-view :user="user" @signed-in="onSignedIn"></router-view>
    </q-layout>
  </div>
</template>

<script>
/*
 * Root component
 */
import {
  QLayout,
  QToolbar,
  QToolbarTitle,
  QBtn
} from 'quasar'

import auth from './google-auth'

export default {
  components: {
    QLayout,
    QToolbar,
    QToolbarTitle,
    QBtn
  },
  data () {
    return {
      user: {
      }
    }
  },
  methods: {
    async onSignedIn (user) {
      this.user = user
    },
    async signOut () {
      await this.$http.post('/logout')
      await auth.signOut()
      this.$router.push('/login')
    }
  },
  async mounted () {
    const isSignedIn = await auth.isSignedIn()
    if (isSignedIn) {
      this.onSignedIn(await auth.getCurrentUser())
    }
    this.$http.interceptors.response.use(
      response => {
      },
      error => {
        if (error.response.status === 401) {
          this.signOut()
        }
      }
    )
  }
}
</script>

<style>
#content-view {
  padding: 8px
}
</style>
