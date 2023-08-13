package com.minutesock.wordgame.uiutils

data class ShakeConfig(
    val iterations: Int,
    val intensity: Float = 100_000f,
    val rotate: Float = 0f,
    val rotateX: Float = 0f,
    val rotateY: Float = 0f,
    val scaleX: Float = 0f,
    val scaleY: Float = 0f,
    val translateX: Float = 0f,
    val translateY: Float = 0f,
    val trigger: Long = System.currentTimeMillis(),
    val animationFinishDelay: Long? = null,
    val onAnimationFinished: (() -> Unit)? = null
) {
    companion object {
        fun no(
            animationFinishDelay: Long? = null,
            onAnimationFinished: (() -> Unit)? = null
        ) = ShakeConfig(
            iterations = 2,
            intensity = 2_000f,
            rotateY = 15f,
            translateX = 40f,
            animationFinishDelay = animationFinishDelay,
            onAnimationFinished = onAnimationFinished,
        )
    }

}




