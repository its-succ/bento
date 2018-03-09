const GOOGLE_CLIENT_ID = '599875423654-67v0atig5nn91to63c2d4a7lmpgi55ns.apps.googleusercontent.com'

async function getGoogleAuth () {
  if (window.gapi.auth2 === undefined) {
    const load = () => {
      return new Promise((resolve, reject) => {
        window.gapi.load('auth2', async () => {
          await window.gapi.auth2.init({ clientId: GOOGLE_CLIENT_ID })
          resolve()
        })
      })
    }
    await load()
  }
  return window.gapi.auth2.getAuthInstance()
}

export default {
  async isSignedIn () {
    const googleAuth = await getGoogleAuth()
    return googleAuth.isSignedIn.get()
  },
  async signOut () {
    const googleAuth = await getGoogleAuth()
    await googleAuth.signOut()
  },
  async getCurrentUser () {
    const googleAuth = await getGoogleAuth()
    return this.createUser(googleAuth.currentUser.get())
  },
  createUser (googleUser) {
    const profile = googleUser.getBasicProfile()
    const user = {
      id: profile.getId(),
      name: profile.getName(),
      id_token: googleUser.getAuthResponse().id_token
    }
    return user
  },
  async renderSignInButton (id, options) {
    await getGoogleAuth()
    window.gapi.signin2.render(id, options)
  }
}
