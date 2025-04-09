package org.mathieu.cleanrmapi.ui.core.manager

expect class SoundManager() {
    fun play(name: String)
    fun stop()
}