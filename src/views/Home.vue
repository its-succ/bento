<template>
  <div class="home">
    <img alt="Vue logo" src="../assets/logo.png">
    <SignIn/>
  </div>
</template>

<script>
import HelloWorld from "@/components/HelloWorld.vue";
import SignIn from "@/components/SignIn.vue";
import firebase from "firebase";

export default {
  name: "home",
  components: {
    SignIn
  },
  created() {
    const db = firebase.firestore();
    const users = db.collection("users");
    console.log(users);

    const uid = firebase.auth().currentUser.uid;

    const userOrders = users.doc(uid);
    console.log(userOrders);
    console.log(userOrders.get());
    const orders = userOrders.collection('orders');
    const doc = orders.doc('20190607');
    console.log(doc);

    doc
      .get()
      .then(function(doc) {
        if (doc.exists) {
          console.log("Document data:", doc.data());
        } else {
          console.log("No such document!");
        }
      })
      .catch(function(error) {
        console.log("Error getting document:", error);
      });
  }
};
</script>
