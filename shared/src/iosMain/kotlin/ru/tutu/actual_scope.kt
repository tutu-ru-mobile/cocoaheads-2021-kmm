package ru.tutu

import kotlinx.coroutines.MainScope

actual internal inline fun getAppScope() = MainScope()
