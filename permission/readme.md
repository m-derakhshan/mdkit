## 📦 Installation

Add JitPack to your `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
````

Then include the library in your `build.gradle.kts`:

```kotlin
dependencies {
       implementation("com.github.m-derakhshan.mdkit:permission:$LATEST_VERSION")
}
```

---

## 🛠️ Usage

The usage is very simple, all you need is providing a permission state align with permission handler.
You need to first add your runtime permissions into your `Manifest` file and then provide them in permission state.

```kotlin
val permissionState = rememberPermissionState(
    permissions = listOf(
        PermissionModel(
            permission = "android.permission.CAMERA",
            maxSDKVersion = Int.MAX_VALUE,
            minSDKVersion = 21,
            rational = "We need camera permission to take pictures",
        )
    )
)

LaunchedEffect(true) {
    permissionState.askPermission()
}

PermissionHandler(state = permissionState){ rationals ->
   AlertDialog(
       onDismissRequest = {
           permissionState.onConsumeRational()
       },
       title = {
           Text("Give permission")
       },
       text = {
           Text(rationals.joinToString { it })
       },
       confirmButton = {
           Button(onClick = {
               permissionState.askPermission()
           }) {
               Text("Give permission")
           }
       }
   )
}
```
