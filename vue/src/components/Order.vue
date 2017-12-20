<template lang="html">
  <div class="order">
    <p class="caption">ただいま、{{ orders[0].date }}～{{ orders[4].date }}までの注文受付中です。</p>
    <p class="caption">{{ user_name }} さんの注文</p>
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
              v-model="select"
              :options="okazu_items"
              :display-value="`${ order.okazuName }`"
            />
          </td>
          <td>
            <q-select
              v-model="select"
              :options="gohan_items"
              :display-value="`${ order.gohanName }`"
            />
          </td>
          <td>
            <q-select
              v-model="select"
              :options="soup_options"
              :display-value="`${ order.soupName }`"
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
      orders: [
        { date: '2017-10-16', dow: '月' },
        { date: '2017-10-17', dow: '火' },
        { date: '2017-10-18', dow: '水' },
        { date: '2017-10-19', dow: '木' },
        { date: '2017-10-20', dow: '金' }
      ],
      okazu_items: [
        { label: '和風', value: 'wafuu' },
        { label: '愛', value: 'ai' },
        { label: 'ゆうき', value: 'yuuki' }
      ],
      gohan_items: [
        { label: '白米', value: 'hakumai' },
        { label: '日替わり健康米', value: 'higawari' },
        { label: '炊き込みご飯', value: 'takikomi' }
      ],
      soup_options: [
        { label: 'ほしい', value: true },
        { label: 'いりません', value: false }
      ]
    }
  },
  methods: {
    async getOrders (userId, week) {
      try {
        const response = await axios.get(`api/orders/${userId}/${week}`)
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

<style lang="css">
</style>