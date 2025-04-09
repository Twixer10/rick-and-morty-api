package org.mathieu.cleanrmapi.ui.core.manager

import android.content.Context
import android.media.MediaPlayer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class SoundManager : KoinComponent {
    private var media = MediaPlayer();
    private val context: Context by inject()

    actual fun play(name: String) {
        if (media.isPlaying) {
            media.stop()
            media.reset()
        }
        val resId = context.resources.getIdentifier(name, "raw", context.packageName)
        if (resId != 0) {
            media = MediaPlayer.create(context, resId)
            media.start()
        } else {
            println("Sound file not found: $name")
        }
    }

    actual fun stop() {
        if (media.isPlaying) {
            media.stop()
            media.reset()
            media.release()
        }
    }
}