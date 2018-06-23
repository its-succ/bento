// === DEFAULT / CUSTOM STYLE ===
// WARNING! always comment out ONE of the two require() calls below.
// 1. use next line to activate CUSTOM STYLE (./src/themes)
// require(`./themes/app.${__THEME}.styl`)
// 2. or, use next line to activate DEFAULT QUASAR STYLE
require(`quasar/dist/quasar.${__THEME}.css`)
// ==============================

// Uncomment the following lines if you need IE11/Edge support
// require(`quasar/dist/quasar.ie`)
// require(`quasar/dist/quasar.ie.${__THEME}.css`)

import Vue from 'vue'
import Quasar from 'quasar'
import router from './router'

Vue.config.productionTip = false
Vue.use(Quasar) // Install Quasar Framework

if (__THEME === 'mat') {
  // require('quasar-extras/roboto-font')
}
import 'quasar-extras/material-icons'
import 'quasar-extras/ionicons'
import 'quasar-extras/fontawesome'
import 'quasar-extras/animate'

import axios from 'axios'
import VueAxios from 'vue-axios'
Vue.use(VueAxios, axios)

import Vuelidate from 'vuelidate'
Vue.use(Vuelidate)

/* filters */
import * as filters from 'filters'
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

import auth from './google-auth'

router.beforeEach(async (to, from, next) => {
  if (!to.matched.some(record => record.meta.requireAuth)) {
    return next()
  }

  const isSignedIn = await auth.isSignedIn()
  if (isSignedIn) {
    return next()
  }
  next({ path: '/login', query: { redirect: to.fullPath } })
})

/* eslint-disable no-new */
new Vue({
  el: '#q-app',
  router,
  render: h => h(require('./App').default)
})
