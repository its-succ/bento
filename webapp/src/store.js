import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

const GOOGLE_CLIENT_ID =
  "599875423654-67v0atig5nn91to63c2d4a7lmpgi55ns.apps.googleusercontent.com";

export default new Vuex.Store({
  state: {
    isAuthInit: false,
    isSignedIn: undefined
  },
  getters: {
    isSignedIn() {
      return authInstance().isSignedIn.get();
    }
  },
  mutations: {
    setInitAuth(state) {
      state.isAuthInit = true;
    },
    setIsSignedIn(state, isSignedIn) {
      state.isSignedIn = isSignedIn;
    }
  },
  actions: {
    async initAuth({ commit, dispatch }) {
      if (window.gapi.auth2 === undefined) {
        const load = () => {
          return new Promise(resolve => {
            window.gapi.load("auth2", async () => {
              await window.gapi.auth2.init({ clientId: GOOGLE_CLIENT_ID });
              resolve();
            });
          });
        };
        await load();
      }
      commit("setInitAuth");
      dispatch("updateSignInState");
    },
    async signIn({ state, dispatch }) {
      if (!state.isAuthInit) {
        await dispatch("initAuth");
      }
      await authInstance().signIn();
      dispatch("updateSignInState");
    },
    async signOut({ state, dispatch }) {
      if (!state.isAuthInit) {
        await dispatch("initAuth");
      }
      await authInstance().signOut();
      dispatch("updateSignInState");
    },
    updateSignInState({ commit }) {
      commit("setIsSignedIn", authInstance().isSignedIn.get());
    }
  }
});
function authInstance() {
  return window.gapi.auth2.getAuthInstance();
}
