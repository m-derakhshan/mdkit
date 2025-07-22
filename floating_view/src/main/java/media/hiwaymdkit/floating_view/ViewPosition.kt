package media.hiwaymdkit.floating_view
internal sealed interface ViewPosition{
    data object TopLeft: ViewPosition
    data object TopRight : ViewPosition
    data object BottomLeft : ViewPosition
    data object BottomRight: ViewPosition
}