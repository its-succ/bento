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
          <td>
            <select v-model="order.rice">
              <option v-for="r in rice" :key="r.index" :value="r.name">
                {{ r.name }} {{ r.price ? `(¥${r.price})` : "" }}
              </option>
            </select>
          </td>
          <td>
            <input type="checkbox" v-model="order.miso" />
          </td>
        </tr>
      </tbody>
    </table>
    <v-btn @click="onSubmit" color="info">注文</v-btn>
  </div>
</template>

<script>
import firebase from "firebase";
import { edgeOfWeek, formatDate } from "@/util";
import { addWeeks, format, eachDay } from "date-fns";
import ja from "date-fns/locale/ja";

export default {
  name: "order",
  data() {
    return {
      orders: [],
      menus: [],
      rice: []
    };
  },
  async mounted() {
    const next = addWeeks(new Date(), 1);
    const edge = edgeOfWeek(next);
    const startDate = formatDate(edge.start);
    const endDate = formatDate(edge.end);

    const db = firebase.firestore();
    const uid = firebase.auth().currentUser.uid;

    const orders = db
      .collection("users")
      .doc(uid)
      .collection("orders");
    const query = orders
      .where("date", ">=", startDate)
      .where("date", "<=", endDate);
    const results = await query.get();
    this.orders = eachDay(edge.start, edge.end).map(date => {
      const order = {
        _date: date,
        date: format(date, "M/D (dd)", { locale: ja }),
        menu: "なし",
        rice: "なし",
        miso: true
      };

      const formatted = formatDate(date);
      const item = results.docs
        .map(item => item.data())
        .find(item => item.date === formatted);
      if (item) {
        order.menu = item.menu;
        order.rice = item.rice;
        order.miso = item.miso;
      }
      return order;
    });

    const menus = await db
      .collection("menus")
      .orderBy("index")
      .get();
    this.menus = menus.docs.map(item => item.data());
    this.menus.unshift({ name: "なし" });

    const rice = await db
      .collection("rice")
      .orderBy("index")
      .get();
    this.rice = rice.docs.map(item => item.data());
    this.rice.unshift({ name: "なし" });
  },
  methods: {
    async onSubmit() {
      const uid = firebase.auth().currentUser.uid;
      const db = firebase.firestore();
      const batch = db.batch();
      this.orders.forEach(order => {
        const menu = this.menus.find(m => m.name === order.menu).price || 0;
        const rice = this.rice.find(r => r.name === order.rice).price || 0;
        const item = {
          date: formatDate(order._date),
          menu: order.menu,
          rice: order.rice,
          miso: order.miso,
          price: menu + rice
        };
        const doc = db.doc(`users/${uid}/orders/${formatDate(order._date)}`);
        batch.set(doc, item);
      });
      await batch.commit();
    }
  }
};
</script>
