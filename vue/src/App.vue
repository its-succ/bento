<template lang="html">
  <!-- Don't drop "q-app" class -->
  <div id="q-app">
    <q-layout>
      <q-toolbar slot="header">
        <q-toolbar-title class="text-bold">ぐるめし</q-toolbar-title>
      </q-toolbar>
      <q-tabs slot="navigation">
        <q-route-tab slot="title" replace :to="{ name: 'history', params: { user_id: user.id }}" class="text-bold">履歴</q-route-tab>
        <q-route-tab slot="title" replace :to="{ name: 'order', params: { user_id: user.id }}" class="text-bold">注文</q-route-tab>
      </q-tabs>
      <router-view id="content-view" :user="user"></router-view>
    </q-layout>
  </div>
</template>

<script>
/*
 * Root component
 */
import axios from 'axios'
import {
  QLayout,
  QToolbar,
  QToolbarTitle,
  QTabs,
  QRouteTab
} from 'quasar'

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
        id: 100,
        name: 'さかい'
      }
    }
  },
  methods: {
    async getUser () {
      try {
        const user = await axios.get('api/users/current')
        this.user.id = user.data.userId
        this.user.name = user.data.nickname
      }
      catch (error) {
        console.error(error)
      }
    }
  },
  mounted () {
    this.getUser()
  }
}
</script>

<style>
#content-view {
  padding: 8px
}
</style>
