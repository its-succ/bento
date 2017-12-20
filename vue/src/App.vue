<template lang="html">
  <!-- Don't drop "q-app" class -->
  <div id="q-app">
    <q-layout>
      <q-toolbar slot="header">
        <q-toolbar-title class="text-bold">ぐるめし</q-toolbar-title>
      </q-toolbar>
      <q-tabs slot="navigation">
        <q-route-tab slot="title" replace to="/history" class="text-bold">履歴</q-route-tab>
        <q-route-tab slot="title" replace to="/order" class="text-bold">注文</q-route-tab>
      </q-tabs>
      <router-view id="content-view"></router-view>
    </q-layout>
  </div>
</template>

<script>

export default {
}
</script>

<style>
#content-view {
  padding: 8px
}
</style>
