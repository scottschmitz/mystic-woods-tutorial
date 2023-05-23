package com.github.scottschmitz.mysticwoodsclone.component

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

enum class AnimationModel {
    PLAYER,
    SLIME,
    CHEST,
    UNDEFINED,
    ;

    val atlasKey: String = this.toString().lowercase()
}

enum class AnimationType {
    IDLE_RIGHT,
    RUN_RIGHT,
    ATTACK_RIGHT,
    DEATH_RIGHT,
    OPEN,
    RUN,
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

data class AnimationComponent(
    var model: AnimationModel = AnimationModel.UNDEFINED,
    var stateTime: Float = 0f,
    var playMode: Animation.PlayMode = Animation.PlayMode.LOOP
): Component<AnimationComponent> {
    companion object : ComponentType<AnimationComponent>() {
        val NO_ANIMATION = ""
    }

    override fun type(): ComponentType<AnimationComponent> = AnimationComponent

    lateinit var animation: Animation<TextureRegionDrawable>
    var nextAnimation: String = NO_ANIMATION

    fun nextAnimation(model: AnimationModel, type: AnimationType) {
        this.model = model
        nextAnimation = "${model.atlasKey}/${type.atlasKey}"
    }
}
