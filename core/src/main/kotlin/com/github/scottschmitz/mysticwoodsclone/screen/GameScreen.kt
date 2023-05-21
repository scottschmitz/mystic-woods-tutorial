package com.github.scottschmitz.mysticwoodsclone.screen

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.utils.Scaling
import com.badlogic.gdx.utils.viewport.ExtendViewport
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.world
import com.github.scottschmitz.mysticwoodsclone.component.ImageComponent
import com.github.scottschmitz.mysticwoodsclone.system.RenderSystem
import ktx.app.KtxScreen
import ktx.assets.disposeSafely
import ktx.log.logger

class GameScreen: KtxScreen {

    companion object {
        private val log = logger<GameScreen>()
    }

    private val stage: Stage = Stage(ExtendViewport(16f, 9f))
    private val texture: Texture = Texture("assets/graphics/player.png")
    private val world: World = world {
        injectables {
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
            add(RenderSystem())
        }
    }

    override fun show() {
        log.debug { "GameScreen gets shown" }

        world.entity {
            it += ImageComponent(
                image = Image(texture).apply {
                    setPosition(1f, 1f)
                    setSize(4f, 4f)
                    setScaling(Scaling.fill)
                }
            )
        }

//        stage.addActor(
//            Image(texture).apply {
//                setPosition(1f, 1f)
//                setSize(1f, 1f)
//                setScaling(Scaling.fill)
//            }
//        )
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        world.update(delta)
    }

    override fun dispose() {
        stage.disposeSafely()
        texture.disposeSafely()
        world.dispose()
    }
}
