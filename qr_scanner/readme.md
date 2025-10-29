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
       implementation("com.github.m-derakhshan.mdkit:qr_scanner:$LATEST_VERSION")
}
```

---

## 🛠️ Usage

The usage is very simple, all you need is providing a qr code state align with QRScanner composable.
It is up to you to handle and request for Camera runtime permission. for that, you can use "Permission" module.

```kotlin
val qrCodeState = rememberQRCodeState()
QRScanner(
    modifier = Modifier.fillMaxSize(),
    state = qrCodeState
)
```
QRCode State accepts QRCodeConfig. you can use it to add specific configuration to the QR code. For example, adding torch.

```kotlin
val torch = remember { TorchConfig() }
val torchState by torch.torchState
val qrCodeState = rememberQRCodeState( config = QRCodeConfig.Builder().addTorchConfig(torch).build())

Button(onClick = {
    if (torchState == TORCH.OFF)
        torch.torchOn()
    else
        torch.torchOff()
}) {
    Text(text = "Toggle Torch")
}

QRScanner(
    modifier = Modifier.fillMaxSize(),
    state = qrCodeState
)
```

Also you can add any overlay on camera. to do that, simply use overlay and put your composable inside:

```kotlin
 QRScanner(
    modifier = Modifier.fillMaxSize(),
    state = qrCodeState,
    overlay = {
     //-----------your overlay here------------//   
    }
)
```
