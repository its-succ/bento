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
          v-for="order in orders" 
          :key="order.date">
          <td>{{ order.date }}</td>
          <td>{{ order.dow }}</td>
          <td>
            <q-select
              v-model="order.okazu"
              inverted
              color="amber"
              separator
              :options="okazu_options"
            />
          </td>
          <td>
            <q-select
              v-model="order.gohan"
              separator
              :options="gohan_options"
            />
          </td>
          <td>
            <q-select
              v-model="order.miso"
              separator
              :options="miso_options"
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
      okazu_options: [],
      gohan_options: [],
      miso_options: []
    }
  },
  methods: {
    async getOrders (userId, week) {
      try {
        // 登録済みの注文を取得する
        const response = await axios.get(`api/orders/${userId}/${week}`)
        this.orders = response.data.orders
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
        this.miso_options = response.data.miso
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