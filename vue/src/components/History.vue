<template>
  <div class="history">
    <p class="caption">{{user.name}} さんの注文</p>
    <table class="q-table bordered vertical-separator striped-odd">
      <caption class="text-left">{{week}} 〜
      </caption>
      <thead class="bg-primary text-white">
        <tr>
          <th colspan="2">日付</th>
          <th>おかず</th>
          <th>ごはん</th>
          <th>味噌汁</th>
          <th>金額</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in orders" :key="order.date">
          <td>{{order.date}}</td>
          <td>{{order.dow}}</td>
          <td>{{order.main}}</td>
          <td>{{order.rice}}</td>
          <td>{{order.soup}}</td>
          <td class="text-right">¥{{order.price}}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: 'history',
  props: {
    user: {
      type: Object,
      required: true
    }
  },
  data () {
    return {
      week: '2017-11-06',
      orders: [
        { date: '11/1', dow: '月', main: '愛', rice: 'ふつう', soup: true, price: 300 },
        { date: '11/2', dow: '火', main: 'ゆうき', rice: 'ふつう', soup: true, price: 360 }
      ]
    }
  },
  methods: {
    async getOrders (userId, week) {
      try {
        const response = await this.$http.get(`api/histories/${userId}/${week}`)
        this.orders = response.data.orders
      }
      catch (error) {
        console.error(error)
      }
    }
  },
  mounted () {
    this.getOrders(this.user.id, this.week)
  }
}
</script>

<style>
</style>
