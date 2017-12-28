<template lang="html">
  <div class="order">
    <p class="caption">{{ welcomeMessage }}<br/>
    {{ user.name }} さんの注文</p>
    <form id="form">
      <table class="q-table bordered vertical-separator striped-odd">
        <thead class="bg-primary text-white">
          <tr class="text-center">
            <th colspan="2" >日付</th>
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
            <td>{{ order.date }}</td>
            <td>{{ order.dow }}</td>
            <td>
              <q-select
                v-model="order.okazu"
                inverted
                color="light-blue"
                separator
                :disabled="closed"
                :options="options.okazu"
                @change="validate(order)"
              />
            </td>
            <td>
              <q-select
                v-model="order.gohan"
                inverted
                color="cyan"
                separator
                :disable="closed"
                :options="options.gohan"
                @change="validate(order)"
              />
            </td>
            <td>
              <q-toggle
                v-model="order.miso"
                color="light-green"
                :disable="closed"
                :click="validateMiso(order)"
              />
            </td>
            <td class="text-right">
              {{ order | total(options) | currency }}
            </td>
          </tr>
          <tr>
            <td colspan="5" class="text-right">合計</td>
            <td class="text-right">{{ orders | payment | currency }}</td>
          </tr>
        </tbody>
      </table>
    </form>
    <div class="layout-padding text-center">
      <q-fixed-position corner="bottom-left" :offset="[18, 18]">
        <q-btn push color="primary" @click="submitOrder()">注文する</q-btn>
      </q-fixed-position>
      <q-fixed-position corner="bottom-right" :offset="[18, 18]">
        <q-btn push color="secondary" @click="noOrder()">この週はいりません</q-btn>
      </q-fixed-position>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import {
  Toast,
  Loading,
  QFixedPosition,
  QSelect,
  QToggle,
  QBtn
} from 'quasar'

export default {
  components: {
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
      week: '2017-10-16',
      closed: false,
      orders: [],
      options: {
        okazu: [],
        gohan: []
      },
      initializedOption: {
        label: '（なし）',
        value: '',
        price: 0
      }
    }
  },
  methods: {
    /**
     * 登録済みの注文を取得する
     */
    async getOrders (userId, week) {
      try {
        const response = await axios.get(`api/orders/${userId}/${week}`)
        this.orders = response.data.orders
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
        const response = await axios.get(`api/masters/all`)
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
    submitOrder () {
      // TODO デモ実装
      Loading.show()
      setTimeout(() => {
        Loading.hide()
      }, 3000)
    },
    /**
     * すべて注文なしで登録する
     */
    noOrder () {
      Object.values(this.orders).forEach(order => {
        order.okazu = ''
        order.gohan = ''
        order.miso = false
      })
      this.submitOrder()
    }
  },
  mounted () {
    this.getOrders(this.user.id, this.week)
    this.getMasters()
  },
  computed: {
    /**
     * Welcomeメッセージを取得
     */
    welcomeMessage: function () {
      if (this.closed) {
        // TODO 締め切っている場合は次の週の受付でもいいような...
        return this.week + 'の週の注文は締め切りました。追加・変更がある場合は直接管理部へ連絡ください。'
      }
      return 'ただいま、' + this.week + 'の週の注文を受付中です。'
    }
  },
  filters: {
    /**
     * 指定の注文の金額を計算する
     */
    total: function (order, items) {
      let result = 0
      Object.keys(items).forEach(key => {
        let item = items[key].find(item => {
          return item.value === order[key]
        })
        if (item) result += item.price
      })
      order.total = result
      return order.total
    },
    /**
     * 支払金額を計算する
     */
    payment: function (orders) {
      let result = 0
      Object.values(orders).forEach(order => {
        result += order.total
      })
      return result
    },
    /**
     * カンマ付円表示する
     */
    currency: function (price) {
      return price.toString().replace(/(\d)(?=(\d{3})+$)/g, '$1,') + ' 円'
    }
  }
}
</script>

<style lang="css">
</style>