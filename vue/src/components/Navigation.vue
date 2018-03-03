<template lang="html">
  <div class="navigation">
    <q-tabs slot="navigation">
      <q-route-tab slot="title" replace :to="{ name: 'history' }" class="text-bold">履歴</q-route-tab>
      <q-route-tab slot="title" replace :to="{ name: 'order' }" class="text-bold">注文</q-route-tab>
      <q-route-tab slot="title" replace :to="{ name: 'ordering' }" class="text-bold">注文合計</q-route-tab>
    </q-tabs>
  </div>
</template>

<script>
import {
  QTabs,
  QTab,
  QRouteTab
} from 'quasar'

export default {
  components: {
    QTabs,
    QTab,
    QRouteTab
  }
}
</script>

<style>
</style>
