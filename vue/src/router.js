import Vue from 'vue'
import VueRouter from 'vue-router'

import History from '@/History.vue'
import Order from '@/Order.vue'
import Ordering from '@/Ordering.vue'

Vue.use(VueRouter)

/*
 * Uncomment this section and use "load()" if you want
 * to lazy load routes.
function load (component) {
  // '@' is aliased to src/components
  return () => import(`@/${component}.vue`)
}
*/

export default new VueRouter({
  /*
   * NOTE! VueRouter "history" mode DOESN'T works for Cordova builds,
   * it is only to be used only for websites.
   *
   * If you decide to go with "history" mode, please also open /config/index.js
   * and set "build.publicPath" to something other than an empty string.
   * Example: '/' instead of current ''
   *
   * If switching back to default "hash" mode, don't forget to set the
   * build publicPath back to '' so Cordova builds work again.
   */

  routes: [
    { path: '/', redirect: '/history/100' },
    { path: '/history/:user_id', name: 'history', component: History, props: true },
    { path: '/order/:user_id', name: 'order', component: Order, props: true },
    { path: '/ordering/:week', name: 'ordering', component: Ordering, props: true }
  ]
})
