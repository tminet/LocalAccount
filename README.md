## Local Account

<b>Local Account</b> is an app that exemplifies a user authentication flow, built with `Kotlin`
and `Jetpack Compose`.

### Features

- `Sign In Screen`: it is the entry point of the app if there is no user connected. On this screen
  it is possible to login using <b>email</b> and <b>password</b>, or navigate to the screen
  responsible for creating a new account.
- `Sign Up Screen`: this is the screen responsible for creating a new account, with <b>name</b>, <b>
  email</b> and <b>password</b>.
- `Reset Password Screen`: the user can reset the password by providing an <b>email</b> on this
  screen.
- `Home Screen`: it is the entry point of the app if the user checks the option to stay connected
  during login. Here it is possible to logout and navigate to settings screen.
- `Settings Screen`: here it is possible to change the <b>name</b> or <b>email</b> of the account.
  In addition to being able to change the preference between <b>light theme</b> or <b>dark theme</b>
  and whether or not to use <b>DynamicColors</b> (feature only for Android 12 or higher).

### Libraries

- `Coroutines`: for all asynchronous tasks.
- `Hilt`: responsible for <b>Dependency Injection</b>.
- `Room`: responsible for intermediation with <b>SQLite</b>, where account data is saved.
- `Datastore Preferences`: where preferences such as theme option and <b>stay connected</b> are
  saved.
- `Navigation`: screen navigation is done following good practices to keep composable functions
  decoupled and reusable.
- `Material 3`: the app's is based on Material 3 colors and components.

---

Download available
on [Play Store](https://play.google.com/store/apps/details?id=tmidev.localaccount).
 