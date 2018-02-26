const GOOGLE_CLIENT_ID = '599875423654-67v0atig5nn91to63c2d4a7lmpgi55ns.apps.googleusercontent.com'

async function getGoogleAuth () {
  console.log('enter auth')
  if (window.gapi.auth2 === undefined) {
    const load = () => {
      return new Promise((resolve, reject) => {
        window.gapi.load('auth2', async () => {
          await window.gapi.auth2.init({ clientId: GOOGLE_CLIENT_ID })
          resolve()
        })
      })
    }
    console.log('before load')
    await load()
    console.log('afeter load')
  }
  console.log('leave auth')
  return window.gapi.auth2.getAuthInstance()
}

export default {
  async isSignedIn () {
    const googleAuth = await getGoogleAuth()
    return googleAuth.isSignedIn.get()
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