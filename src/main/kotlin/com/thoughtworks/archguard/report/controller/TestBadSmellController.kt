package com.thoughtworks.archguard.report.controller

import com.thoughtworks.archguard.evaluation.domain.TestBadSmellCount
import com.thoughtworks.archguard.report.infrastructure.TestBadSmellRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestBadSmellController(@Autowired val testBadSmellRepo: TestBadSmellRepo) {

    @GetMapping("/reports/test-bad-smells")
    fun getBadSmellReport(): List<TestBadSmellCount> {
        return testBadSmellRepo.getTestBadSmellCount()
    }

}