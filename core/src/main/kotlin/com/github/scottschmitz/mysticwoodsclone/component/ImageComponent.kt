package com.github.scottschmitz.mysticwoodsclone.component

import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.github.quillraven.fleks.Component
import com.github.quillraven.fleks.ComponentType

data class ImageComponent(
    var image: Image
): Component<ImageComponent>, Comparable<ImageComponent> {
    companion object : ComponentType<ImageComponent>()
    override fun type(): ComponentType<ImageComponent> = ImageComponent

    override fun compareTo(other: ImageComponent): Int {
        val yDiff = other.image.y.compareTo(image.y)
        return if (yDiff != 0) {
            yDiff
        } else {
            other.image.x.compareTo(image.x)
        }
    }
}
