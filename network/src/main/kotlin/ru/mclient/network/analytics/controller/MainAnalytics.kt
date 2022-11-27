package ru.mclient.network.analytics.controller

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.branch.service.CompanyBranchService
import ru.mclient.network.record.domain.RecordEntity
import ru.mclient.network.record.domain.RecordEntity.VisitStatus.*
import ru.mclient.network.record.service.RecordService
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*


@RestController
class MainAnalytics(
    private val companyBranchService: CompanyBranchService,
    private val recordService: RecordService,
) {


    @GetMapping("/companies/{companyQuery}/analytics/main")
    fun getMainCompanyAnalytics(
        @PathVariable companyQuery: String,
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        start: LocalDate,
        @RequestParam
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        end: LocalDate,
    ): MainAnalyticsResponse {
        DateTimeFormatter.ISO_DATE
        val company = companyBranchService.findByIdOrCodename(companyQuery)
        val currentAnalytics = calculateMainAnalytics(company, start, end)
        val amount = end.toEpochDay() - start.toEpochDay() + 1
        val previousStart = start.minusDays(amount)
        val previousEnd = end.minusDays(amount)
        val previousAnalytics = calculateMainAnalytics(company, previousStart, previousEnd)
        return calculateAnalytics(currentAnalytics, previousAnalytics)
    }

    private fun calculatePercent(current: Long, previous: Long): Int {
        if (previous == 0L) return 100
        return (100 - (100 * current / previous)).toInt()
    }

    private fun calculateAnalytics(current: MainAnalyticsItem, previous: MainAnalyticsItem): MainAnalyticsResponse {
        return MainAnalyticsResponse(
            totalSum = AnalyticItem(
                value = "${current.totalSum} ₽",
                difference = calculatePercent(current.totalSum, previous.totalSum),
            ),
            averageSum = AnalyticItem(
                value = "${current.averageSum} ₽",
                difference = calculatePercent(current.averageSum, previous.averageSum),
            ),
            comeCount = AnalyticItem(
                value = "${current.comeCount}",
                difference = calculatePercent(current.comeCount, previous.comeCount),
            ),
            notComeCount = AnalyticItem(
                value = "${current.notComeCount}",
                difference = calculatePercent(current.notComeCount, previous.notComeCount),
            ),
            waitingCount = AnalyticItem(
                value = "${current.waitingCount}",
                difference = calculatePercent(current.waitingCount, previous.waitingCount),
            ),
        )
    }

    private fun calculateMainAnalytics(
        company: CompanyBranchEntity,
        start: LocalDate,
        end: LocalDate,
    ): MainAnalyticsItem {
        val records = recordService.getRecordsInPeriod(company, start, end)
        var totalSum = 0L
        var count = 0
        val statusCount = EnumMap<RecordEntity.VisitStatus, Long>(RecordEntity.VisitStatus::class.java)
        records.forEach {
            count++
            totalSum += it.sum
            statusCount[it.status] = statusCount[it.status]?.plus(1) ?: 1
        }
        return MainAnalyticsItem(
            totalSum = totalSum,
            averageSum = if (count != 0) (totalSum / count) else 0,
            comeCount = statusCount.getOrDefault(COME, 0),
            notComeCount = statusCount.getOrDefault(NOT_COME, 0),
            waitingCount = statusCount.getOrDefault(WAITING, 0),
        )
    }

}

class AnalyticItem(
    val value: String,
    val difference: Int,
)

class MainAnalyticsResponse(
    val totalSum: AnalyticItem,
    val averageSum: AnalyticItem,
    val comeCount: AnalyticItem,
    val notComeCount: AnalyticItem,
    val waitingCount: AnalyticItem,
)

class MainAnalyticsItem(
    val totalSum: Long,
    val averageSum: Long,
    val comeCount: Long,
    val notComeCount: Long,
    val waitingCount: Long,
)