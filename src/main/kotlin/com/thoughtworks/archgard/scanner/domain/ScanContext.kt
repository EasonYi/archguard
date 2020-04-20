package com.thoughtworks.archgard.scanner.domain

import com.thoughtworks.archgard.scanner.domain.config.model.ToolConfigure
import com.thoughtworks.archgard.scanner.domain.project.BuildTool
import java.io.File

data class ScanContext(var repo: String, var buildTool: BuildTool, val workspace: File, val config: List<ToolConfigure>)