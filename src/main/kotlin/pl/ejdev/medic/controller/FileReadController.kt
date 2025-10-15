package pl.ejdev.medic.controller

import java.io.File

class FileReadController {
    fun onUploadFile(file: File): List<String> {
        println("Selected file: ${file.absolutePath}")
        return file.readText().split("\n")
            .onEach { println(it) }

    }
}