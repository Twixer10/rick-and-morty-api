package org.mathieu.cleanrmapi.ui

import org.koin.dsl.module
import org.mathieu.cleanrmapi.ui.core.manager.SoundManager

val UiModule = module {
    single { SoundManager() }
}