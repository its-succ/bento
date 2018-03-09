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
import { currency } from '../filters'

export default {
  name: 'ordering',
  data () {
    return {
      week: '2017-11-06',
      dates: {
        monday: '', tuesday: '', wednesday: '', thursday: '', friday: ''
      },
      okazu_orders: [],
      gohan_orders: [],
      miso: {
        menu: '', price: 0, monday: 0, tuesday: 0, wednesday: 0, thursday: 0, friday: 0
      },
      summary: {
        monday: 0, tuesday: 0, wednesday: 0, thursday: 0, friday: 0
      }
    }
  },
  methods: {
    async getOrders (week) {
      try {
        const response = await axios.get(`api/ordering/${week}`)
        const orders = response.data.orders.map(order => {
          return {
            menu: order.menu,
            isMain: order.isMain,
            price: order.price,
            monday: order[this.dates.monday],
            tuesday: order[this.dates.tuesday],
            wednesday: order[this.dates.wednesday],
            thursday: order[this.dates.thursday],
            friday: order[this.dates.friday]
          }
        })
        this.okazu_orders = orders.filter(order => order.isMain)
        this.gohan_orders = orders.filter(order => !order.isMain && order.price > 0)
        this.miso = orders.filter(order => !order.isMain && order.price === 0)[0]
        this.calcSummary()
        this.format()
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
    format () {
      Object.keys(this.summary).forEach(key => {
        this.summary[key] = currency(this.summary[key])
      })
      this.okazu_orders.forEach(order => {
        order.price = currency(order.price)
      })
      this.gohan_orders.forEach(order => {
        order.price = currency(order.price)
      })
    },
    async getDates (week) {
      // TODO weekからの日付計算はフロント側でやる（曜日も）
      this.dates.monday = '2017-11-06'
      this.dates.tuesday = '2017-11-07'
      this.dates.wednesday = '2017-11-08'
      this.dates.thursday = '2017-11-09'
      this.dates.friday = '2017-11-10'
    }
  },
  mounted () {
    this.getDates(this.week)
    this.getOrders(this.week)
  }
}
</script>

<style>
</style>
