package com.thoughtworks.archguard.scanner.domain.scanner.codescan.sourcecode

import com.thoughtworks.archguard.scanner.infrastructure.FileOperator
import com.thoughtworks.archguard.scanner.infrastructure.Processor
import org.slf4j.LoggerFactory
import java.io.File
import java.net.URL

class SourceCodeTool(
    val systemRoot: File,
    val systemId: Long,
    val language: String,
    val dbUrl: String,
    val codePath: String
) {
    private val log = LoggerFactory.getLogger(SourceCodeTool::class.java)
    private val host = "https://github.com/archguard/scanner/releases/download/v1.4.1"
    private val version = "1.4.1"
    private val SCAN_SOURCECODE_JAR = "scan_sourcecode-$version-all.jar"

    fun analyse() {
        prepareTool()
        var path = codePath
        if(codePath.isEmpty()) {
            path = "."
        }

        scan(listOf("java", "-jar", "-Ddburl=$dbUrl?useSSL=false", "scan_sourcecode.jar", "--path=$path", "--system-id=$systemId", "--language=${language.lowercase()}"))
    }

    private fun prepareTool() {
        val jarExist = checkIfExistInLocal()
        if (jarExist) {
            copyIntoSystemRoot()
        } else {
            download()
        }
    }

    private fun copyIntoSystemRoot() {
        log.info("copy SourceCode jar tool from local")
        FileOperator.copyTo(File(SCAN_SOURCECODE_JAR), File("$systemRoot/scan_sourcecode.jar"))
        val chmod = ProcessBuilder("chmod", "+x", "scan_sourcecode.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun checkIfExistInLocal(): Boolean {
        return File(SCAN_SOURCECODE_JAR).exists()
    }

    private fun download() {
        log.info("download SourceCodeTool")
        val downloadUrl = "$host/$SCAN_SOURCECODE_JAR"
        FileOperator.download(URL(downloadUrl), File(SCAN_SOURCECODE_JAR))
        val chmod = ProcessBuilder("chmod", "+x", "scan_sourcecode.jar")
        chmod.directory(systemRoot)
        chmod.start().waitFor()
    }

    private fun scan(cmd: List<String>) {
        Processor.executeWithLogs(ProcessBuilder(cmd), systemRoot)
    }

}
