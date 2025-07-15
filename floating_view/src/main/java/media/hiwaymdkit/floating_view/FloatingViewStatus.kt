package media.hiwaymdkit.floating_view

sealed interface FloatingViewStatus {
    data object Minimized : FloatingViewStatus
    data object Opened : FloatingViewStatus
    data object Closing : FloatingViewStatus
    data object Closed : FloatingViewStatus
}