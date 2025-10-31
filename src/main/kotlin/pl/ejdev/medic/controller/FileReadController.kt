package pl.ejdev.medic.controller

import java.io.File

class FileReadController {
    fun onUploadFile(file: File): List<String> = file
        .readText()
        .split("\n")
        .onEach { println(it) }
}