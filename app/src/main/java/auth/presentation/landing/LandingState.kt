package auth.presentation.landing

data class LandingState(
    val paramOne: String = "default",
    val paramTwo: List<String> = emptyList(),
)