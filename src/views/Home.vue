<template>
  <div class="home">
    <SignIn/>
    <ul>
      <li v-for="order in orders">
        {{ order.date }}, {{ order.menu }}
      </li>
    </ul>
  </div>
</template>

<script>
import HelloWorld from "@/components/HelloWorld.vue";
import SignIn from "@/components/SignIn.vue";
import firebase from "firebase";
import { format, startOfWeek, endOfWeek } from 'date-fns';

export default {
  name: "home",
  components: {
    SignIn
  },
  data() {
    return {
      orders: [],
    }
  },
  async created() {
    const now = new Date();
    const edge = this.edgeOfWeek(now);
    const startDate = this.formatDate(edge.start);
    const endDate = this.formatDate(edge.end);

    const uid = firebase.auth().currentUser.uid;

    const db = firebase.firestore();
    const orders = db.collection("users").doc(uid).collection("orders");
    const query = orders.where("date", ">=", startDate).where("date", "<=", endDate);
    const results = await query.get();
    this.orders = results.docs.map(item => item.data());
  },
  methods: {
    edgeOfWeek(date) {
      const start = startOfWeek(date);
      const end = endOfWeek(date);
      return { start, end };
    },
    formatDate(date) {
      return format(date, "YYYYMMDD");
    }
  }
};
</script>
