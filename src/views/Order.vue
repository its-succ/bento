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
          <td>
            <select v-model="order.menu">
              <option v-for="menu in menus" :key="menu.name" :value="menu.name">
                {{ menu.name }} {{ menu.price ? `(¥${menu.price})` : "" }}
              </option>
            </select>
          </td>
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
      menus: [],
    }
  },
  async mounted() {
    const next = addWeeks(new Date(), 1);
    const edge = edgeOfWeek(next);
    this.orders = eachDay(edge.start, edge.end).map(date => {
      return {
        date: format(date, "M/D (dd)", { locale: ja }),
        menu: "",
        rice: "",
        miso: false,
      };
    });

    const db = firebase.firestore();
    const menus = await db.collection("menus").orderBy("index").get();
    this.menus = menus.docs.map(item => item.data());
    this.menus.unshift("");
  }
}
</script>
