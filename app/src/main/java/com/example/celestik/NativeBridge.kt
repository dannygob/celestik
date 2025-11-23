package com.example.celestik

object NativeBridge {
    init {
        System.loadLibrary("native_opencv")
    }

    external fun convertToGray(matAddr: Long)
}