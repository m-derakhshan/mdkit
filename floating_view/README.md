
## 🛠️ Usage

The usage is very simple, all you need is providing a floating view state and then you can use floating view composable.
Floating view, should be used inside of a `Box`. but avoid setting `Modifier.align()` on it, as it will not work properly.

```kotlin
val floatingViewState = rememberFloatingViewState()
Box(modifier = Modifier.fillMaxSize()) {
    FloatingView(state = floatingViewState) {
        ...
    }
}
```