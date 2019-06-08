<template>
  <div class="admin">
    <sign-in />
  </div>
</template>

<script>
import SignIn from "@/components/SignIn.vue";
import firebase from "firebase";
import { edgeOfWeek, formatDate } from "@/util";
import { format, eachDay } from 'date-fns';
import ja from 'date-fns/locale/ja'

export default {
  name: "admin",
  components: {
    "sign-in" : SignIn
  },
  async created() {
    const now = new Date();
    const edge = edgeOfWeek(now);
    const startDate = formatDate(edge.start);
    const endDate = formatDate(edge.end);

    const db = firebase.firestore();
    const users = await db.collection("users").get();
    users.docs.forEach(async doc => {
      const orders = doc.ref.collection("orders");
      const query = orders.where("date", ">=", startDate).where("date", "<=", endDate);
      const results = await query.get();
      console.log(doc.data(), results.docs.map(r => r.data()));
    })

  }
}
</script>
