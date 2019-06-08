<template>
  <div class="home">
    <SignIn/>
    <ul>
      <li v-for="order in orders" :key="order.date">
        {{ order.date }}, {{ order.menu }}
      </li>
    </ul>
    <button @click="toOrder">注文へ</button>
  </div>
</template>

<script>
import HelloWorld from "@/components/HelloWorld.vue";
import SignIn from "@/components/SignIn.vue";
import firebase from "firebase";
import { edgeOfWeek, formatDate} from "@/util";

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
    const edge = edgeOfWeek(now);
    const startDate = formatDate(edge.start);
    const endDate = formatDate(edge.end);

    const uid = firebase.auth().currentUser.uid;

    const db = firebase.firestore();
    const orders = db.collection("users").doc(uid).collection("orders");
    const query = orders.where("date", ">=", startDate).where("date", "<=", endDate);
    const results = await query.get();
    this.orders = results.docs.map(item => item.data());
  },
  methods: {
    toOrder() {
      this.$router.push("order");
    }
  }
};
</script>
