package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.overview.BadSmell
import com.thoughtworks.archguard.report.domain.sizing.SizingRepository
import org.springframework.beans.factory.annotation.Value

class MethodOverSizingCalculator(val sizingRepository: SizingRepository) : BaseOverSizingCalculator() {
    @Value("\${threshold.method.line}")
    private val methodSizingThreshold: Int = 0

    override fun getCount(systemId: Long): Long {
        return 0L
    }

    override fun getLineCount(systemId: Long): Long {
        return sizingRepository.getMethodSizingAboveLineThresholdCount(systemId, methodSizingThreshold)
    }

    override fun getBadSmellType(): BadSmell {
        return BadSmell.METHOD_OVER_SIZING
    }

    override fun getLineCountLevelRanges(): Array<LongRange> {
        val linesRangeLevel1 = 30L until 40L
        val linesRangeLevel2 = 40L until 50L
        val linesRangeLevel3 = 50L until Long.MAX_VALUE
        return arrayOf(linesRangeLevel1, linesRangeLevel2, linesRangeLevel3)
    }

    override fun getCountLevelRanges(): Array<LongRange> {
        TODO("Not yet implemented")
    }
}
