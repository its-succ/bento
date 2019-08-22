<template>
  <div class="home">
    <SignIn />
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
    <v-btn @click="toOrder" color="info">注文へ</v-btn>
  </div>
</template>

<script>
import SignIn from "@/components/SignIn.vue";
import firebase from "firebase";
import { edgeOfWeek, formatDate } from "@/util";
import { format, eachDay } from "date-fns";
import ja from "date-fns/locale/ja";

export default {
  name: "home",
  components: {
    SignIn
  },
  data() {
    return {
      orders: []
    };
  },
  async created() {
    const now = new Date();
    const edge = edgeOfWeek(now);
    const startDate = formatDate(edge.start);
    const endDate = formatDate(edge.end);

    const uid = firebase.auth().currentUser.uid;

    const db = firebase.firestore();
    const orders = db
      .collection("users")
      .doc(uid)
      .collection("orders");
    const query = orders
      .where("date", ">=", startDate)
      .where("date", "<=", endDate);
    const results = await query.get();
    this.orders = eachDay(startDate, endDate).map(date => {
      const order = {
        date: format(date, "M/D (dd)", { locale: ja }),
        menu: "-",
        rice: "-",
        miso: "-"
      };
      const formatted = formatDate(date);
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
  },
  methods: {
    toOrder() {
      this.$router.push("order");
    }
  }
};
</script>
