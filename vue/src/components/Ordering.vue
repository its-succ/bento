<template>
  <div class="ordering">
    <table class="q-table bordered vertical-separator striped-odd">
      <caption class="text-left">{{week}} 〜
      </caption>
      <thead class="bg-primary text-white">
        <tr>
          <th colspan="2">メニュー</th>
          <th>値段</th>
          <th>{{dates.monday}}</th>
          <th>{{dates.tuesday}}</th>
          <th>{{dates.wednesday}}</th>
          <th>{{dates.thursday}}</th>
          <th>{{dates.friday}}</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in okazu_orders" :key="order.menu">
          <td class="text-left">{{order.menu}}</td>
          <td class="text-left">おかず</td>
          <td class="text-right">{{order.price}}</td>
          <td class="text-right">{{order.monday}}</td>
          <td class="text-right">{{order.tuesday}}</td>
          <td class="text-right">{{order.wednesday}}</td>
          <td class="text-right">{{order.thursday}}</td>
          <td class="text-right">{{order.friday}}</td>
        </tr>
        <tr v-for="order in gohan_orders" :key="order.menu">
          <td class="text-left" colspan="2">{{order.menu}}</td>
          <td class="text-right">{{order.price}}</td>
          <td class="text-right">{{order.monday}}</td>
          <td class="text-right">{{order.tuesday}}</td>
          <td class="text-right">{{order.wednesday}}</td>
          <td class="text-right">{{order.thursday}}</td>
          <td class="text-right">{{order.friday}}</td>
        </tr>
        <tr>
          <td class="text-left" colspan="3">{{miso.menu}}</td>
          <td class="text-right">{{miso.monday}}</td>
          <td class="text-right">{{miso.tuesday}}</td>
          <td class="text-right">{{miso.wednesday}}</td>
          <td class="text-right">{{miso.thursday}}</td>
          <td class="text-right">{{miso.friday}}</td>
        </tr>
        <tr>
          <td class="text-left" colspan="3">合計金額</td>
          <td class="text-right">{{summary.monday}}</td>
          <td class="text-right">{{summary.tuesday}}</td>
          <td class="text-right">{{summary.wednesday}}</td>
          <td class="text-right">{{summary.thursday}}</td>
          <td class="text-right">{{summary.friday}}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'ordering',
  props: {
    user_id: {
      type: String,
      required: true
    }
  },
  data () {
    return {
      week: '2017-11-06',
      dates: {
        monday: '9月18日(月)', tuesday: '9月19日(火)', wednesday: '9月20日(水)', thursday: '9月21日(木)', friday: '9月22日(金)'
      },
      okazu_orders: [
        { menu: '和風', price: 308, monday: 1, tuesday: 0, wednesday: 1, thursday: 1, friday: 0 },
        { menu: '愛', price: 308, monday: 0, tuesday: 2, wednesday: 1, thursday: 0, friday: 0 },
        { menu: 'ゆうき', price: 360, monday: 1, tuesday: 0, wednesday: 0, thursday: 0, friday: 1 }
      ],
      gohan_orders: [
        { menu: 'ライス', price: 103, monday: 3, tuesday: 3, wednesday: 1, thursday: 0, friday: 0 },
        { menu: '健康米', price: 185, monday: 1, tuesday: 0, wednesday: 2, thursday: 0, friday: 2 },
        { menu: '応援ランチ', price: 360, monday: 3, tuesday: 2, wednesday: 1, thursday: 0, friday: 0 }
      ],
      miso: {
        menu: '味噌汁', price: 0, monday: 1, tuesday: 0, wednesday: 1, thursday: 2, friday: 0
      },
      summary: {
        monday: 100, tuesday: 200, wednesday: 300, thursday: 400, friday: 500
      }
    }
  },
  methods: {
    async getOrders (userId, week) {
      try {
        const response = await axios.get(`api/ordering/${week}`)
        this.okazu_orders = response.data.orders.filter(order => order.isMain)
        this.gohan_orders = response.data.orders.filter(order => !order.isMain && order.price > 0)
        this.miso = response.data.orders.filter(order => !order.isMain && order.price === 0)[0]
        this.calcSummary()
      }
      catch (error) {
        console.error(error)
      }
    },
    calcSummary () {
      this.summary = this.okazu_orders.concat(this.gohan_orders)
        .map(order => {
          return {
            monday: order.price * order.monday,
            tuesday: order.price * order.tuesday,
            wednesday: order.price * order.wednesday,
            thursday: order.price * order.thursday,
            friday: order.price * order.friday
          }
        })
        .reduce((previous, current, index, array) => {
          return {
            monday: previous.monday + current.monday,
            tuesday: previous.tuesday + current.tuesday,
            wednesday: previous.wednesday + current.wednesday,
            thursday: previous.thursday + current.thursday,
            friday: previous.friday + current.friday
          }
        })
    },
    async getDates (week) {
      try {
        const response = await axios.get(`api/dates/${week}`)
        this.dates = response.data.dates
      }
      catch (error) {
        console.error(error)
      }
    }
  },
  mounted () {
    this.getOrders(this.user_id, this.week)
    this.getDates(this.week)
  }
}
</script>

<style>
</style>
