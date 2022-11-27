package ru.mclient.network.analytics.controller

import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
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
        val records = recordService.getRecordsInPeriod(company, start, end)
        var totalSum = 0L
        var count = 0
        val statusCount = EnumMap<RecordEntity.VisitStatus, Int>(RecordEntity.VisitStatus::class.java)
        records.forEach {
            count++
            totalSum += it.sum
            statusCount[it.status] = statusCount[it.status]?.plus(1) ?: 1
        }
        return MainAnalyticsResponse(
            totalSum = totalSum,
            averageSum = if (count != 0) (totalSum / count) else 0,
            comeCount = statusCount.getOrDefault(COME, 0),
            notComeCount = statusCount.getOrDefault(NOT_COME, 0),
            waitingCount = statusCount.getOrDefault(WAITING, 0),
        )
    }

}


class MainAnalyticsResponse(
    val totalSum: Long,
    val averageSum: Long,
    val comeCount: Int,
    val notComeCount: Int,
    val waitingCount: Int,
)