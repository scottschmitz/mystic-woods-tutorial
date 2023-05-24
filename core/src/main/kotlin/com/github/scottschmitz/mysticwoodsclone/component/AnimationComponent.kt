package com.github.scottschmitz.mysticwoodsclone.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

enum class AnimationType {
    IDLE,
    RUN,
    ATTACK,
    DEATH,
    OPEN,
    ;

    val atlasKey: String = this.toString().lowercase()
}

enum class Direction {
    UP,
    DOWN,
    LEFT,
    RIGHT,
    ;

    val atlasKey: String = this.toString().lowercase()
}

enum class AnimationModel {
    PLAYER,
    SLIME,
    CHEST,
    UNDEFINED,
    ;

    val atlasKey: String = this.toString().lowercase()
}

sealed class NextAnimation {
    object None: NextAnimation()

    data class Some(
        val model: AnimationModel,
        val type: AnimationType,
        val direction: Direction
    ): NextAnimation()
}

data class AnimationComponent(
    var stateTime: Float = 0f,
    var playMode: Animation.PlayMode = Animation.PlayMode.LOOP
): Component<AnimationComponent> {
    companion object : ComponentType<AnimationComponent>()

    override fun type(): ComponentType<AnimationComponent> = AnimationComponent

    lateinit var animation: Animation<TextureRegionDrawable>
    var nextAnimation: NextAnimation = NextAnimation.None

    fun nextAnimation(model: AnimationModel, type: AnimationType, direction: Direction) {
        nextAnimation = NextAnimation.Some(model, type, direction)
    }
}
