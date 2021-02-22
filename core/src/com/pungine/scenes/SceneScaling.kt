package modules.scenes

enum class SceneScaling {
    STRETCH, RATIO
    /*
    STRETCH{
        override fun scaledWidth(desiredRatio: Float): Int {
            return Gdx.graphics.width
        }

        override fun scaledHeight(desiredRatio: Float): Int {
            return Gdx.graphics.height
        }
           },
    RATIO{
        override fun scaledWidth(desiredRatio: Float): Int {
            val r = Rectangle(0f,Gdx.graphics.width.toFloat(),0f,Gdx.graphics.height.toFloat())
            return r.getSubRectangle(desiredRatio).width.toInt()
        }

        override fun scaledHeight(desiredRatio: Float): Int {
            val r = Rectangle(0f,Gdx.graphics.width.toFloat(),0f,Gdx.graphics.height.toFloat())
            return r.getSubRectangle(desiredRatio).height.toInt()
        }
    };


    abstract fun scaledWidth(desiredRatio: Float = 1f): Int
    abstract fun scaledHeight(desiredRatio: Float = 1f): Int

     */
}