package com.thoughtworks.archgard.scanner.domain.scanner.dependencies

import com.thoughtworks.archgard.scanner.domain.project.ProjectRepository
import com.thoughtworks.archgard.scanner.domain.tools.JavaByteCodeTool
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JavaDependencyAnalysis(@Value("\${spring.datasource.url}") val dbUrl: String,
                             @Value("\${spring.datasource.username}") val username: String,
                             @Value("\${spring.datasource.password}") val password: String,
                             @Autowired val projectRepository: ProjectRepository) {
    private val log = LoggerFactory.getLogger(JavaDependencyAnalysis::class.java)

    fun analysis() {
        log.info("start scan java analysis")
        val build = projectRepository.getProjectInfo().build()
        val url = dbUrl.replace("://", "://" + username + ":" + password+"@")
        val javaByteCodeTool = JavaByteCodeTool(build.workspace, url)
        javaByteCodeTool.analyse()
        log.info("finished scan java analysis")
    }
}