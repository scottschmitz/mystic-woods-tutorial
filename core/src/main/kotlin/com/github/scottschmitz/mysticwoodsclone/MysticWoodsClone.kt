package com.github.scottschmitz.mysticwoodsclone

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.github.scottschmitz.mysticwoodsclone.screen.GameScreen
import ktx.app.KtxGame
import ktx.app.KtxScreen

class MysticWoodsClone : KtxGame<KtxScreen>() {
    override fun create() {
        Gdx.app.logLevel = Application.LOG_DEBUG

        addScreen(GameScreen())
        setScreen<GameScreen>()
    }
}
