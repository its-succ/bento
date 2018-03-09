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
          <td>{{order.okazu}}</td>
          <td>{{order.gohan}}</td>
          <td>{{order.miso}}</td>
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
      week: '2017-10-16',
      orders: [
      ]
    }
  },
  watch: {
    user () {
      // userが変化したら注文を取得する
      this.getOrders(this.week)
    }
  },
  methods: {
    async getOrders (week) {
      if (this.user.id === undefined) {
        // user未設定時はNOP
        return
      }
      try {
        const response = await this.$http.get(`api/orders/${week}`)
        this.orders = response.data.orders
      }
      catch (error) {
        console.error(error)
      }
    }
  },
  mounted () {
    this.getOrders(this.week)
  }
}
</script>

<style>
</style>
