package com.github.scottschmitz.mysticwoodsclone.screen

import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.world
import com.github.scottschmitz.mysticwoodsclone.component.*
import com.github.scottschmitz.mysticwoodsclone.system.AnimationSystem
import com.github.scottschmitz.mysticwoodsclone.system.RenderSystem
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

class GameScreen: KtxScreen {

    companion object {
        private val log = logger<GameScreen>()
    }

    private val stage: Stage = Stage(ExtendViewport(16f, 9f))
    private val textureAtlas = TextureAtlas("assets/graphics/mystic-woods.atlas")

    private val world: World = world {
        injectables {
            add(textureAtlas)
            add(stage)
        }

        components {
            onAdd(ImageComponent) { _, imageComponent ->
                stage.addActor(imageComponent.image)
            }

            onRemove(ImageComponent) { _, imageComponent ->
                stage.root.removeActor(imageComponent.image)
            }
        }

        systems {
            add(AnimationSystem())
            add(RenderSystem())
        }
    }

    override fun show() {
        log.debug { "GameScreen gets shown" }

        world.entity {
            it += ImageComponent(
                image = Image().apply {
                    setSize(4f, 4f)
                }
            )
            it += AnimationComponent().apply {
                nextAnimation(AnimationModel.PLAYER, AnimationType.IDLE, Direction.UP)
            }
        }

        world.entity {
            it += ImageComponent(
                image = Image().apply {
                    setSize(4f, 4f)
                    setPosition(12f, 0f)
                }
            )
            it += AnimationComponent().apply {
                nextAnimation(AnimationModel.SLIME, AnimationType.RUN, Direction.LEFT)
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        world.update(delta)
    }

    override fun dispose() {
        stage.disposeSafely()
        textureAtlas.disposeSafely()
        world.dispose()
    }
}
