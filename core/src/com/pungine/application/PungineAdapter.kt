package modules.application

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.pungo.modules.scenes.LayerManager

open class PungineAdapter(var bgColor: Color = Color(0f,0f,0f,1f)): ApplicationAdapter() {
    lateinit var batch: SpriteBatch
    var currentPixelWidth = 0
    var currentPixelHeight = 0

    override fun create() {
        super.create()
        batch = SpriteBatch()
        currentPixelWidth = Gdx.graphics.width
        currentPixelHeight = Gdx.graphics.height
    }

    override fun render() {
        super.render()
        Gdx.gl.glClearColor(bgColor.r,bgColor.g,bgColor.b,bgColor.a)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        LayerManager.update()
        if((currentPixelHeight!=Gdx.graphics.height)||(currentPixelWidth!=Gdx.graphics.width)){
            currentPixelWidth = Gdx.graphics.width
            currentPixelHeight = Gdx.graphics.height
            batch.dispose()
            batch = SpriteBatch()
        }
        batch.begin()
        LayerManager.draw(batch)
        batch.end()
    }

    override fun dispose() {
        super.dispose()
        LayerManager.dispose()
        batch.dispose()
    }
}