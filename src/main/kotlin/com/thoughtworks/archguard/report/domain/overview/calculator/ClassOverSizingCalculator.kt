package com.thoughtworks.archguard.report.domain.overview.calculator

import com.thoughtworks.archguard.report.domain.overview.BadSmell
import com.thoughtworks.archguard.report.domain.sizing.SizingRepository
import org.springframework.beans.factory.annotation.Value

class ClassOverSizingCalculator(val sizingRepository: SizingRepository) : BaseOverSizingCalculator() {

    @Value("\${threshold.class.method.count}")
    private val classMethodCountSizingThreshold: Int = 0

    @Value("\${threshold.class.line}")
    private val classSizingThreshold: Int = 0

    override fun getCount(systemId: Long): Long {
        return sizingRepository.getClassSizingListAboveMethodCountThresholdCount(systemId, classMethodCountSizingThreshold);
    }

    override fun getLineCount(systemId: Long): Long {
        return sizingRepository.getClassSizingAboveLineThresholdCount(systemId, classSizingThreshold)
    }

    override fun getBadSmellType(): BadSmell {
        return BadSmell.CLASS_OVER_SIZING
    }

    override fun getLineCountLevelRanges(): Array<LongRange> {
        val linesRangeLevel1 = 600L until 1000L
        val linesRangeLevel2 = 1000L until 2000L
        val linesRangeLevel3 = 2000L until Long.MAX_VALUE
        return arrayOf(linesRangeLevel1, linesRangeLevel2, linesRangeLevel3)
    }

    override fun getCountLevelRanges(): Array<LongRange> {
        val countRangeLevel1 = 20L until 40L
        val countRangeLevel2 = 40L until 60L
        val countRangeLevel3 = 60L until Long.MAX_VALUE
        return arrayOf(countRangeLevel1, countRangeLevel2, countRangeLevel3)
    }
}
