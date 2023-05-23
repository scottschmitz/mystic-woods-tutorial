package com.github.scottschmitz.mysticwoodsclone.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.scottschmitz.mysticwoodsclone.component.AnimationComponent
import com.github.scottschmitz.mysticwoodsclone.component.ImageComponent
import ktx.app.gdxError
import ktx.collections.map
import ktx.log.logger

class AnimationSystem(
    private val textureAtlas: TextureAtlas  = World.inject(),
//    private val animationComponents: ComponentMapper<AnimationComponent>,
//    private val imageComponents: ComponentManager<ImageComponent>
) : IteratingSystem(
    family = World.family {
        all(AnimationComponent, ImageComponent)
    },
) {
    companion object {
        private val logger = logger<AnimationSystem>()
        private const val DEFAULT_FRAME_DURATION = 1/8f
    }

    private val cachedAnimations = mutableMapOf<String, Animation<TextureRegionDrawable>>()
    override fun onTickEntity(entity: Entity) {
        val animComp = entity[AnimationComponent]
         when (animComp.nextAnimation) {
            AnimationComponent.NO_ANIMATION -> {
                animComp.stateTime += deltaTime
            }

            else -> {
                animComp.animation = animation(animComp.nextAnimation)
                animComp.stateTime = 0f

                animComp.nextAnimation = AnimationComponent.NO_ANIMATION
            }
        }

        animComp.animation.playMode = animComp.playMode
        entity[ImageComponent].image.drawable = animComp.animation.getKeyFrame(animComp.stateTime)
    }

    private fun animation(animKeyPath: String): Animation<TextureRegionDrawable> {
        return cachedAnimations.getOrPut(animKeyPath) {
            logger.debug { "New animation is created for '$animKeyPath'" }

            val regions = textureAtlas.findRegions(animKeyPath)
            if (regions.isEmpty) {
                gdxError("There are no texture regions for $animKeyPath")
            } else {
                Animation(DEFAULT_FRAME_DURATION, regions.map { TextureRegionDrawable(it) })
            }
        }
    }
}
