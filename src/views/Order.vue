<template>
  <div class="home">
    <table>
      <thead>
        <tr>
          <th>日付</th>
          <th>おかず</th>
          <th>ごはん</th>
          <th>味噌汁</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="order in orders" :key="order.date">
          <td>{{ order.date }}</td>
          <td>{{ order.menu }}</td>
          <td>{{ order.rice }}</td>
          <td>{{ order.miso }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import firebase from "firebase";
import { edgeOfWeek, formatDate } from "@/util";
import { addWeeks, format, eachDay } from 'date-fns';
import ja from 'date-fns/locale/ja'

export default {
  name: "order",
  data() {
    return {
      orders: [],
    }
  },
  async mounted() {
    const next = addWeeks(new Date(), 1);
    const edge = edgeOfWeek(next);
    this.orders = eachDay(edge.start, edge.end).map(date => {
      return {
        date: format(date, "M/D(dd)", { locale: ja }),
        menu: "",
        rice: "",
        miso: false,
      };
    });
  }
}
</script>
