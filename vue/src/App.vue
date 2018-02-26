<template lang="html">
  <!-- Don't drop "q-app" class -->
  <div id="q-app">
    <q-layout>
      <q-toolbar slot="header">
        <q-toolbar-title class="text-bold">ぐるめし</q-toolbar-title>
      </q-toolbar>
      <q-tabs slot="navigation">
        <q-route-tab slot="title" replace :to="{ name: 'history' }" class="text-bold">履歴</q-route-tab>
        <q-route-tab slot="title" replace :to="{ name: 'order' }" class="text-bold">注文</q-route-tab>
      </q-tabs>
      <router-view id="content-view" :user="user" @signed-in="onSignedIn"></router-view>
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
  QTabs,
  QRouteTab
} from 'quasar'

import auth from './google-auth'

export default {
  components: {
    QLayout,
    QToolbar,
    QToolbarTitle,
    QTabs,
    QRouteTab
  },
  data () {
    return {
      user: {
      }
    }
  },
  methods: {
    onSignedIn (user) {
      console.log('onSignedIn')
      console.log(user)
      this.user = user
    }
  },
  async mounted () {
    const isSignedIn = await auth.isSignedIn()
    if (isSignedIn) {
      this.user = await auth.getCurrentUser()
    }
  }
}
</script>

<style>
#content-view {
  padding: 8px
}
</style>
