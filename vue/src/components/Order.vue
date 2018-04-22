<template lang="html">
  <div class="order">
    <navigation></navigation>
    <div id="content-view">
      <p class="caption" v-html='welcomeMessage'></p>
      <p class="caption">{{ user.name }} さんの注文（★：未登録です）</p>
      <form id="form">
        <table class="q-table bordered vertical-separator striped-odd">
          <thead class="bg-primary text-white">
            <tr class="text-center">
              <th colspan="3" >日付</th>
              <th>おかず</th>
              <th>ごはん</th>
              <th>味噌汁</th>
              <th>値段</th>
            </tr>
          </thead>
          <tbody>
            <tr
              v-for="order in orders"
              :key="order.date">
              <td>{{ order.id | status }}</td>
              <td v-bind:class="[order.holiday? 'holiday' : '']">{{ order.date }}</td>
              <td v-bind:class="[order.holiday? 'holiday' : '']">{{ order.date | dayofweek }}</td>
              <td>
                <q-select
                  v-model="order.okazu"
                  inverted
                  color="light-blue"
                  separator
                  :disable="closed || order.holiday"
                  :options="filteredOkazu(order.date)"
                  @change="validate(order)"
                />
              </td>
              <td>
                <q-select
                  v-model="order.gohan"
                  inverted
                  color="cyan"
                  separator
                  :disable="closed || order.holiday"
                  :options="options.gohan"
                  @change="validate(order)"
                />
              </td>
              <td>
                <q-toggle
                  v-model="order.miso"
                  color="light-green"
                  :disable="closed || order.holiday"
                  :click="validateMiso(order)"
                />
              </td>
              <td class="text-right">
                {{ order | total(options) | currency }}
              </td>
            </tr>
            <tr>
              <td colspan="6" class="text-right">合計</td>
              <td class="text-right">{{ orders | payment | currency }}</td>
            </tr>
          </tbody>
        </table>
      </form>
      <div v-if="this.closed === false" class="layout-padding text-center">
        <q-fixed-position corner="bottom-left" :offset="[18, 18]">
          <q-btn push color="primary" @click="submitOrder()">注文する</q-btn>
        </q-fixed-position>
        <q-fixed-position corner="bottom-right" :offset="[18, 18]">
          <q-btn push color="secondary" @click="noOrder()">この週はいりません</q-btn>
        </q-fixed-position>
      </div>
    </div>
  </div>
</template>

<script>
import Navigation from './Navigation'
import {
  Toast,
  Loading,
  QFixedPosition,
  QSelect,
  QToggle,
  QBtn
} from 'quasar'
import moment from 'moment'
moment.locale('ja')

export default {
  components: {
    Navigation,
    QFixedPosition,
    QSelect,
    QToggle,
    QBtn
  },
  name: 'order',
  props: {
    user: {
      type: Object,
      required: true
    }
  },
  data () {
    return {
      week: '',
      closed: false,
      orders: [],
      options: {
        okazu: [],
        gohan: []
      },
      initializedOption: {
        label: '（なし）',
        value: '',
        price: 0,
        dayofweek: null
      }
    }
  },
  methods: {
    /**
     * 登録済みの注文を取得する
     */
    async getOrders (week) {
      try {
        const response = await this.$http.get(`api/orders/${week}`)
        this.orders = response.data
        console.log(response.data)
      }
      catch (error) {
        console.error(error)
      }
    },
    /**
     * マスタを取得する
     */
    async getMasters () {
      try {
        const response = await this.$http.get(`api/masters/all`)
        Object.keys(response.data).forEach(key => {
          this.options[key] = response.data[key]
          this.options[key].unshift(this.initializedOption)
        })
      }
      catch (error) {
        console.error(error)
      }
    },
    /**
     * おかずのリストを曜日でフィルタリングする
     */
    filteredOkazu (date) {
      let dayofweek = moment(date).day()
      return this.options.okazu.filter(function (okazu) {
        return okazu.dayofweek === null || okazu.dayofweek === dayofweek
      })
    },
    /**
     * Toastでエラー表示する
     */
    showErrorToast (message) {
      Toast.create.negative({
        html: message
      })
    },
    /**
     * バリデーション（おかず、ごはん相関チェック）
     */
    validate (order) {
      if (order.okazu === 'ouen' && order.gohan !== '') {
        this.showErrorToast('応援はごはんつきのためごはんをなしにします。')
        order.gohan = ''
      }
      else if (order.okazu === '' && order.gohan === '') {
        order.miso = false
      }
    },
    /**
     * バリデーション（味噌汁相関チェック）
     */
    validateMiso (order) {
      if (order.okazu === '' && order.gohan === '') {
        // おかずもごはんも選んでないなら味噌汁なし
        order.miso = false
      }
    },
    /**
     * 入力の内容で注文する
     */
    async submitOrder () {
      Loading.show()
      try {
        const response = await this.$http.post(`api/orders/${this.week}`, this.orders)
        this.orders = response.data
        Loading.hide()
        Toast.create.positive({
          html: '注文を受け付けました。'
        })
      }
      catch (error) {
        console.error(error)
      }
      finally {
        if (Loading.isActive) Loading.hide()
      }
    },
    /**
     * すべて注文なしで登録する
     */
    noOrder () {
      Object.values(this.orders).forEach(order => {
        order.okazu = ''
        order.gohan = ''
        order.miso = false
        order.price = 0
      })
      this.submitOrder()
    }
  },
  mounted () {
    let today = moment()
    // 翌週の月曜の日付
    this.week = today.clone().add(7, 'days').day(1).format('YYYY-MM-DD')
    // 0は日曜日をさすため、日曜日から翌週の受付開始となる
    // 〆日判定（金曜日15時まで）
    let closingDate = today.clone().day(5).set('hour', 15).set('minute', 0).set('second', 0)
    this.closed = today.diff(closingDate) > 0

    // データ取得
    this.getOrders(this.week)
    this.getMasters()
  },
  computed: {
    /**
     * Welcomeメッセージを取得
     */
    welcomeMessage () {
      let message = ''
      if (this.closed) {
        message = this.week + 'の週の注文は締め切りました。'
      }
      else {
        message = 'ただいま、' + this.week + 'の週の注文を受付中です。'
      }
      return message + '<br/>今週の注文の追加・変更がある場合は直接管理部へ連絡ください。'
    }
  },
  filters: {
    /**
     * order.idが未設定の場合は未登録の印を表示する
     */
    status (id) {
      if (id) return ''
      return '★'
    },
    /**
     * 指定の注文の金額を計算する
     */
    total (order, items) {
      let result = 0
      Object.keys(items).forEach(key => {
        let item = items[key].find(item => {
          return item.value === order[key]
        })
        if (item) result += item.price
      })
      order.price = result
      return order.price
    },
    /**
     * 支払金額を計算する
     */
    payment (orders) {
      let result = 0
      Object.values(orders).forEach(order => {
        result += order.price
      })
      return result
    }
  }
}
</script>

<style lang="css">
  .holiday {
    color: 'red';
    font-weight: bold;
  }
</style>
