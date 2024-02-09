package com.bhanit.sensor

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform