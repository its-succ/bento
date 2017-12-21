<template lang="html">
  <div class="order">
    <p class="caption">ただいま、{{ orders[0].date }}～{{ orders[4].date }}までの注文受付中です。<br/>
    {{ user_name }} さんの注文</p>
    <table class="q-table bordered vertical-separator striped-odd">
      <thead class="bg-primary text-white">
        <tr>
          <th colspan="2">日付</th>
          <th>おかず</th>
          <th>ごはん</th>
          <th>味噌汁</th>
        </tr>
      </thead>
      <tbody>
        <tr 
          v-for="(order,index) in orders" 
          :key="order.date">
          <td>{{ order.date }}</td>
          <td>{{ order.dow }}</td>
          <td>
            <q-select
              v-model="okazu[index]"
              inverted
              color="amber"
              separator
              :options="okazu_options"
              :select="order.okazu"
            />
          </td>
          <td>
            <q-select
              v-model="gohan[index]"
              separator
              :options="gohan_options"
            />
          </td>
          <td>
            <q-select
              v-model="soup[index]"
              separator
              :options="soup_options"
            />
          </td>
        </tr>
      </tbody>
    </table>
    <q-layout>
      <q-fixed-position corner="bottom-left" :offset="[18, 18]">
        <q-btn push color="primary">注文する</q-btn>
      </q-fixed-position>
      <q-fixed-position corner="bottom-right" :offset="[18, 18]">
        <q-btn push color="secondary">今週はいりません</q-btn>
      </q-fixed-position>
    </q-layout>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'order',
  props: {
    user_id: {
      type: String,
      required: true
    }
  },
  data () {
    return {
      user_name: 'さかい',
      week: '2017-10-16',
      orders: [],
      okazu: [],
      okazu_options: [],
      gohan: [],
      gohan_options: [],
      soup: [],
      soup_options: []
    }
  },
  methods: {
    async getOrders (userId, week) {
      try {
        // 登録済みの注文を取得する
        const response = await axios.get(`api/orders/${userId}/${week}`)
        this.orders = response.data.orders
        // 登録済みの注文の内容で初期値表示する
        this.okazu = []
        this.gohan = []
        this.soup = []
        for (let key in this.orders) {
          let order = this.orders[key]
          this.okazu.push(order.okazu)
          this.gohan.push(order.gohan)
          this.soup.push(order.soup)
        }
      }
      catch (error) {
        console.error(error)
      }
    },
    async getMasters () {
      try {
        // マスターを取得する
        const response = await axios.get(`api/masters/all`)
        this.okazu_options = response.data.okazu
        this.gohan_options = response.data.gohan
        this.soup_options = response.data.soup
        // おかず、ごはんは未選択状態を追加
        let unselect = { label: '', value: '' }
        this.okazu_options.unshift(unselect)
        this.gohan_options.unshift(unselect)
      }
      catch (error) {
        console.error(error)
      }
    }
  },
  mounted () {
    this.getOrders(this.user_id, this.week)
    this.getMasters()
  }
}
</script>

<style lang="css">
</style>