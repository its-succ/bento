<template>
  <div class="history">
    <p class="caption">{{user_name}} さんの注文</p>
    <table class="q-table bordered vertical-separator striped-odd">
      <caption class="text-left">{{week}} 〜
      </caption>
      <thead class="bg-primary text-white">
        <tr>
          <th>メニュー</th>
          <th>価格</th>
          <th>9月18日(月)</th>
          <th>9月19日(火)</th>
          <th>9月20日(水)</th>
          <th>9月21日(木)</th>
          <th>9月22日(金)</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in orders" :key="order.num">
          <td>{{order.menu}}</td>
          <td>{{order.price}}</td>
          <td>{{order.monday}}</td>
          <td>{{order.tuesday}}</td>
          <td>{{order.wednesday}}</td>
          <td>{{order.thursday}}</td>
          <td>{{order.friday}}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'history',
  props: {
    user_id: {
      type: String,
      required: true
    }
  },
  data () {
    return {
      user_name: 'さかい',
      week: '2017-11-06',
      orders: [
        { num: 1, menu: '和風（おかず）', price: 308, monday: 1, tuesday: 0, wednesday: 1, thursday: 1, friday: 0 },
        { num: 2, menu: '愛（おかず）', price: 308, monday: 0, tuesday: 2, wednesday: 1, thursday: 0, friday: 0 },
        { num: 3, menu: 'ゆうき（おかず）', price: 360, monday: 1, tuesday: 0, wednesday: 0, thursday: 0, friday: 1 },
        { num: 4, menu: 'ライス', price: 103, monday: 3, tuesday: 3, wednesday: 1, thursday: 0, friday: 0 },
        { num: 5, menu: '健康米', price: 185, monday: 1, tuesday: 0, wednesday: 2, thursday: 0, friday: 2 },
        { num: 6, menu: '応援ランチ', price: 360, monday: 3, tuesday: 2, wednesday: 1, thursday: 0, friday: 0 },
        { num: 7, menu: 'みそ汁', price: 0, monday: 1, tuesday: 0, wednesday: 1, thursday: 2, friday: 0 }
      ]
    }
  },
  methods: {
    async getOrders (userId, week) {
      try {
        const response = await axios.get(`api/histories/${userId}/${week}`)
        this.orders = response.data.orders
      }
      catch (error) {
        console.error(error)
      }
    }
  },
  mounted () {
    this.getOrders(this.user_id, this.week)
  }
}
</script>

<style>
</style>
