package modules.visuals

import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture

class PuxMap: Pixmap {
    constructor(fileHandle: FileHandle): super(fileHandle)
    constructor(w:Int,h:Int,format: Format): super(w,h,format)

    fun finalize(){
        dispose()
    }
}