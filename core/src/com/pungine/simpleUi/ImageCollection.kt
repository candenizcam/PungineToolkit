package modules.simpleUi

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Sprite
import modules.basic.Colour
import kotlin.random.Random

class ImageCollection {
    constructor()
    constructor(mutableList: MutableList<Sprite>, frameChanger: FrameChanger?= null){
        collection = mutableList.map { PunSprite(it) }.toMutableList()
        if(frameChanger!=null){
            this.frameChanger = frameChanger
        }

    }


    private var collection = mutableListOf<PunSprite>()
    var index = 0
    var frameChanger = FrameChanger()


    fun recolour(colour: Colour, index: Int = -1){
        try {
            if(index==-1){
                collection.forEach { it.colour = colour }
            }else{
                collection[index].colour = colour
            }
        }catch (e: Exception){
            println("index error in image collection recolour")
        }
    }

    fun getColour(index: Int = 0): Colour {
        try {
            return collection[index].colour.copy()
        }catch (e: Exception){
            println("index error in image collection getcolour")
            throw e
        }
    }

    fun update(){
        frameChanger.update()
    }

    fun add(punSprite: PunSprite){
        collection.add(punSprite)
    }

    fun clear(){
        collection.clear()
        index = 0
    }

    fun yieldImage(): PunSprite? {
        return when(collection.size){
            0 -> null
            else -> collection[index]
        }
    }

    fun copy(): ImageCollection {
        val ic = ImageCollection()
        collection.forEach {
            ic.add(it.copy())
        }
        when(frameChanger){
            is SingleLoopFrameChanger -> ic.frameChanger = ic.SingleLoopFrameChanger(frameChanger as SingleLoopFrameChanger)
            is IdleActivator -> ic.frameChanger = ic.IdleActivator(frameChanger as IdleActivator)
            is FpsFrameChanger -> ic.frameChanger = ic.FpsFrameChanger(frameChanger as FpsFrameChanger)
            else -> ic.frameChanger = ic.FrameChanger(frameChanger)
        }

        return ic
    }


    fun rollActiveFrame(n: Int) {
        index = (index + n).rem(collection.size)
    }

    /** This is the base class for frame changers
     * for any custom design, one can generate a frame changer class here
     * or inherit this from outside and make its own class, then inject to frameChanger of this object
     */
    open inner class FrameChanger {
        constructor()
        constructor(fc: FrameChanger){}
        var loopDone = false
        open fun update() {

        }

        open fun start() {
        }

        open fun deactivate(){

        }
    }


    /** This generates a frame changer class with fps and changes frame with time
     *
     */
    inner class FpsFrameChanger(var fps: Float) : FrameChanger(){
        constructor(f: FpsFrameChanger): this(f.fps)
        var time = 0f
        var active = true
        override fun update() {
            if (active) {
                time += Gdx.graphics.deltaTime
                rollActiveFrame((time * fps).toInt())
                time = time.rem(1 / fps)
            }
        }

        override fun start() {
            index = 0
            time = 0f
            loopDone = false
            active = true
        }

        override fun deactivate() {
            active = false
            index = 0
        }


    }

    inner class SingleLoopFrameChanger(var fps: Float, var doneAtStart: Boolean = false) : FrameChanger() {
        constructor(s: SingleLoopFrameChanger): this(s.fps, s.doneAtStart)
        var time = 0f

        init {
            if (doneAtStart) {
                index = collection.size - 1
                time = index / fps
            }
        }

        override fun update() {
            index = (time * fps).toInt().coerceAtMost(collection.size - 1)
            if (collection.size - 1 == index) {
                loopDone = true
            } else {
                time += Gdx.graphics.deltaTime

            }
        }

        override fun start() {
            index = 0
            time = 0f
            loopDone = false
        }
    }

    inner class IdleActivator(var fps: Float, var goChance: Float) : FrameChanger() {
        constructor(ia: IdleActivator): this(ia.fps,ia.goChance)
        var time = 0f
        private var idleCounter = 0

        init {
            index = collection.size - 1

        }

        override fun update() {
            if (time * fps > 1) {
                if (collection.size - 1 == index) {
                    if (Random.nextFloat() < goChance * idleCounter) {
                        start()
                        idleCounter = 0
                    } else {
                        idleCounter += 1
                    }
                    loopDone = true
                } else {
                    index += 1
                }
                time -= 1f / fps
            }



            time += Gdx.graphics.deltaTime


        }

        override fun start() {
            index = 0
            time = 0f
            loopDone = false
        }
    }
}