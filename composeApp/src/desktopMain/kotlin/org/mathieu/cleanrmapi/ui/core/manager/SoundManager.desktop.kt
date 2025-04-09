package org.mathieu.cleanrmapi.ui.core.manager

import javax.sound.sampled.Clip

actual class SoundManager {
    private var clip : Clip? = null

    actual fun play(name: String) {
        if (clip != null && clip!!.isRunning) {
            clip!!.stop()
            clip!!.close()
        }
        val soundFile = javaClass.getResource("/raw/$name.wav")
        if (soundFile != null) {
            clip = javax.sound.sampled.AudioSystem.getClip()
            clip!!.open(javax.sound.sampled.AudioSystem.getAudioInputStream(soundFile))
            clip!!.start()
        } else {
            println("Sound file not found: $name")
        }
    }

    actual fun stop() {
        if (clip != null && clip!!.isRunning) {
            clip!!.stop()
            clip!!.close()
        }
    }
}