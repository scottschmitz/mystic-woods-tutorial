package com.github.scottschmitz.mysticwoodsclone.system

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.scottschmitz.mysticwoodsclone.component.AnimationComponent
import com.github.scottschmitz.mysticwoodsclone.component.ImageComponent
import com.github.scottschmitz.mysticwoodsclone.component.NextAnimation
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
        when (val nextAnimation = animComp.nextAnimation) {
            NextAnimation.None -> {
                animComp.stateTime += deltaTime
            }
            is NextAnimation.Some -> {
                animComp.animation = animation(nextAnimation)
                animComp.stateTime = 0f

                animComp.nextAnimation = NextAnimation.None
            }
        }

        animComp.animation.playMode = animComp.playMode
        entity[ImageComponent].image.drawable = animComp.animation.getKeyFrame(animComp.stateTime)
    }

    private fun animation(nextAnimation: NextAnimation.Some): Animation<TextureRegionDrawable> {
        val modelAtlasKey = nextAnimation.model.atlasKey
        val typeAtlasKey = nextAnimation.type.atlasKey
        val directionAtlasKey = nextAnimation.direction.atlasKey

        val directionalAnimation = "${modelAtlasKey}/${typeAtlasKey}_${directionAtlasKey}"
        val directionlessFallbackAnimation = "${modelAtlasKey}/${typeAtlasKey}"

        return cachedAnimations.getOrPut(directionalAnimation) {

            val regions = textureAtlas.findRegions(directionalAnimation)
            if (regions.isEmpty) {
                cachedAnimations.getOrPut(directionlessFallbackAnimation) {
                    val directionlessRegions = textureAtlas.findRegions(directionlessFallbackAnimation)
                    if (directionlessRegions.isEmpty) {
                        gdxError("There are no texture regions for '$directionalAnimation' or '$directionlessFallbackAnimation'")
                    } else {
                        logger.debug { "New animation is created for '$directionlessFallbackAnimation'" }
                        Animation(DEFAULT_FRAME_DURATION, directionlessRegions.map { TextureRegionDrawable(it) })
                    }
                }
            } else {
                logger.debug { "New animation is created for '$directionalAnimation'" }
                Animation(DEFAULT_FRAME_DURATION, regions.map { TextureRegionDrawable(it) })
            }
        }
    }
}
