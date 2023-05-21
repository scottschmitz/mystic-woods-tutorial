package com.github.scottschmitz.mysticwoodsclone.system

import com.badlogic.gdx.scenes.scene2d.Stage
import com.github.quillraven.fleks.Entity
import com.github.quillraven.fleks.IteratingSystem
import com.github.quillraven.fleks.World
import com.github.quillraven.fleks.collection.compareEntityBy
import com.github.scottschmitz.mysticwoodsclone.component.ImageComponent

class RenderSystem(
    private val stage: Stage = World.inject(),
) : IteratingSystem(
    family = World.family {
        all(ImageComponent)
    },
    comparator = compareEntityBy(ImageComponent)
) {

    override fun onTick() {
        super.onTick()

        with(stage) {
            viewport.apply()

            act(deltaTime)
            draw()
        }
    }

    override fun onTickEntity(entity: Entity) {

    }
}
