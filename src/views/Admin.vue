<template>
  <div class="admin">
    <sign-in />
    <v-layout 
      class="pa-3"
      column
    >
      <v-simple-table fixed-header>
        <thead>
          <tr>
            <th>名前</th>
            <th v-for="date in dates" :key="date">{{ date }}</th>
            <th>合計</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.name">
            <td>{{ user.name }}</td>
            <td v-for="order in user.orders" :key="order.date">
              {{ order.menu }}<br />
              {{ order.rice }}<br />
              {{ order.miso }}<br />
            </td>
            <td>{{ user.price }}</td>
          </tr>
        </tbody>
      </v-simple-table>
      <v-layout 
        class="pa-3" 
        justify-space-between
      >
        <v-btn outlined rounded color="indigo" @click="prevWeek">前の週</v-btn>
        <v-btn outlined rounded color="indigo" @click="nextWeek">次の週</v-btn>
      </v-layout>
      
    </v-layout>
    
  </div>
</template>

<script>
import SignIn from "@/components/SignIn.vue";
import firebase from "firebase";
import { edgeOfWeek, formatDate } from "@/util";
import { addWeeks, subWeeks, format, eachDay } from "date-fns";
import ja from "date-fns/locale/ja";

export default {
  name: "admin",
  components: {
    "sign-in": SignIn
  },
  data() {
    return {
      week: null,
      dates: [],
      users: []
    };
  },
  async created() {
    await this.createData(addWeeks(new Date(), 1));
  },
  methods: {
    async createData(date) {
      this.week = date;
      const edge = edgeOfWeek(date);
      this.dates = eachDay(edge.start, edge.end).map(date =>
        format(date, "M/D (dd)", { locale: ja })
      );

      const startDate = formatDate(edge.start);
      const endDate = formatDate(edge.end);

      const db = firebase.firestore();
      const users = await db.collection("users").get();
      const promises = users.docs.map(async user => {
        const query = user.ref
          .collection("orders")
          .where("date", ">=", startDate)
          .where("date", "<=", endDate);
        const results = await query.get();
        const orders = eachDay(edge.start, edge.end).map(date => {
          const formatted = formatDate(date);
          const order = {
            date: formatted,
            menu: "-",
            rice: "-",
            miso: "-"
          };
          const item = results.docs
            .map(item => item.data())
            .find(item => item.date === formatted);
          if (item) {
            order.menu = item.menu;
            order.rice = item.rice;
            order.miso = item.miso ? "あり" : "なし";
          }
          return order;
        });
        const price = results.docs
          .map(item => item.data().price)
          .reduce((acc, val) => acc + val, 0);
        return {
          name: user.data().name,
          orders,
          price
        };
      });
      this.users = await Promise.all(promises);
    },
    nextWeek() {
      this.createData(addWeeks(this.week, 1));
    },
    prevWeek() {
      this.createData(subWeeks(this.week, 1));
    }
  }
};
</script>
