package utils

import java.io.File
import java.nio.charset.StandardCharsets

object FileWriter {
    private val file = File("logs/example.txt")
    fun write(str:Any) = file.appendText("\n$str", StandardCharsets.UTF_8)
    fun separate() = file.appendText("\n--------------------------------------------------------------")
    fun clear() = file.writeText("")
}