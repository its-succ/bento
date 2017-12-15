<template>
  <div class="orders">
    <table class="q-table bordered vertical-separator striped-odd">
      <caption class="text-left">ただいま、{{orders[0].date}}～{{orders[4].date}}までの注文受付中です。</caption>
      <thead class="bg-primary text-white">
        <tr>
          <th colspan="2">日付</th>
          <th>おかず</th>
          <th>ごはん</th>
          <th>味噌汁</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in orders" :key="order.date">
          <td>{{order.date}}</td>
          <td>{{order.dow}}</td>
          <td>
            <q-select
              v-model="select"
              :options="okazu_items"
            />
          </td>
          <td>
            <q-select
              v-model="select"
              :options="gohan_items"
            />
          </td>
          <td>
            <q-select
              v-model="select"
              :options="miso_required"
            />
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import {
  QSelect
} from 'quasar'

export default {
  name: 'orders',
  props: {
    user_id: {
      type: String,
      required: true
    }
  },
  data () {
    return {
      user_name: 'さかい',
      orders: [
        { date: '2017/10/16', dow: '月', okazu: 'wafuu', okazeName: '和風', gohan: '', gohanName: '', misosoup: 'ほしい' },
        { date: '2017/10/17', dow: '火', okazu: '', okazeName: '', gohan: '', gohanName: '', misosoup: 'いらない' },
        { date: '2017/10/18', dow: '水', okazu: 'ai', okazeName: '愛', gohan: 'hakumai', gohanName: '白米', misosoup: 'いらない' },
        { date: '2017/10/19', dow: '木', okazu: 'yuuki', okazeName: 'ゆうき', gohan: '', gohanName: '', misosoup: 'いらない' },
        { date: '2017/10/20', dow: '金', okazu: '', okazeName: '', gohan: '', gohanName: '', misosoup: 'いらない' }
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
      miso_required: [
        { label: 'ほしい', value: 'true' },
        { label: 'いりません', value: 'false' }
      ]
    }
  },
  components: {
    QSelect
  }
}
</script>

<style>
</style>
