package ru.mclient.network.staff.controllers

import com.fasterxml.jackson.annotation.JsonFormat
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import ru.mclient.network.branch.service.CompanyBranchService
import ru.mclient.network.staff.domain.StaffScheduleEntity
import ru.mclient.network.staff.service.StaffService
import java.time.LocalDate
import java.time.LocalTime
import javax.transaction.Transactional
import javax.validation.Valid
import javax.validation.constraints.NotEmpty

@RestController
class StaffScheduleController(
    private val staffService: StaffService,
    private val companyBranchService: CompanyBranchService,
) {


    @Tag(name = "Staff schedule")
    @Operation(summary = "get schedule for single staff")
    @GetMapping("/staff/{staffId}/schedule")
    fun getSingleStaffSchedule(
        @PathVariable staffId: Long,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @RequestParam start: LocalDate,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @RequestParam end: LocalDate,
    ): GetSingleStaffScheduleResponse {
        val staff = staffService.findByStaffId(staffId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return GetSingleStaffScheduleResponse(
            schedule = staffService.findScheduleByStaff(staff, start, end).map {
                GetSingleStaffScheduleResponse.ScheduleDate(
                    date = it.date,
                    slots = listOf(GetSingleStaffScheduleResponse.ScheduleSlot(from = it.from, to = it.to))
                )
            }
        )
    }

    @Tag(name = "Staff schedule")
    @Operation(summary = "get schedule for companies staff")
    @GetMapping("/companies/{companyId}/schedule")
    fun getCompanyStaffSchedule(
        @PathVariable companyId: String,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        @RequestParam date: LocalDate,
        @RequestParam(required = false) staffId: List<Long>?,
    ): GetMultipleStaffScheduleResponse {
        val schedule = if (staffId.isNullOrEmpty()) {
            val company = companyBranchService.findByIdOrCodename(companyId)
            staffService.findScheduleByCompany(company, date)
        } else {
            val staff = staffService.findStaffByIds(staffId)
            staffService.findScheduleByStaff(staff, date)
        }
        return GetMultipleStaffScheduleResponse(
            schedule = schedule.map { (staff, schedule) ->
                GetMultipleStaffScheduleResponse.ScheduleDate(
                    date = date,
                    staffId = staff.id,
                    slots = schedule.map { GetMultipleStaffScheduleResponse.ScheduleSlot(from = it.from, to = it.to) }
                )
            }
        )
    }

    @Tag(name = "Staff schedule")
    @Operation(summary = "add list of schedules for staff")
    @PostMapping("/staff/{staffId}/schedule")
    @Transactional
    fun addSingleStaffSchedule(
        @PathVariable staffId: Long,
        @Valid @RequestBody data: AddSingeStaffScheduleRequest,
    ): GetSingleStaffScheduleResponse {
        val staff = staffService.findByStaffId(staffId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val schedule = staffService.addStaffSchedule(
            data.schedule.map {
                val slot = it.slots.first()
                StaffScheduleEntity(
                    date = it.date,
                    staff = staff,
                    from = slot.from,
                    to = slot.to,
                )
            }
        )
        return GetSingleStaffScheduleResponse(
            schedule = schedule.map {
                GetSingleStaffScheduleResponse.ScheduleDate(
                    date = it.date,
                    slots = listOf(GetSingleStaffScheduleResponse.ScheduleSlot(from = it.from, to = it.to))
                )
            }
        )
    }

    @Tag(name = "Staff schedule")
    @Operation(summary = "add list of schedules for staff")
    @PostMapping("/companies/{companyQuery}/schedule")
    @Transactional
    fun addMultipleStaffSchedule(
        @PathVariable companyQuery: String,
        @Valid @RequestBody data: AddMultipleStaffScheduleRequest,
    ): GetMultipleStaffScheduleResponse {
        companyBranchService.findByIdOrCodename(companyQuery)
        val staff = staffService.findStaffByIds(data.schedule.map { it.staffId }).associateBy { it.id }
        val schedule = staffService.addStaffSchedule(
            data.schedule.map {
                val slot = it.slots.first()
                StaffScheduleEntity(
                    date = it.date,
                    staff = staff[it.staffId] ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "staff not found"),
                    from = slot.from,
                    to = slot.to,
                )
            }
        )
        return GetMultipleStaffScheduleResponse(
            schedule = schedule.map {
                GetMultipleStaffScheduleResponse.ScheduleDate(
                    date = it.date,
                    staffId = it.staff.id,
                    slots = listOf(GetMultipleStaffScheduleResponse.ScheduleSlot(from = it.from, to = it.to))
                )
            }
        )
    }


}

class AddSingeStaffScheduleRequest(
    @field:Valid
    val schedule: List<ScheduleDate>,
) {
    class ScheduleDate(
        val operation: ScheduleOperation,
        val date: LocalDate,
        @field:NotEmpty
        val slots: List<ScheduleSlot> = emptyList(),
    )

    class ScheduleSlot(
        val operation: ScheduleOperation,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val from: LocalTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val to: LocalTime,
    )

    enum class ScheduleOperation {
        ADD, DELETE
    }
}


class GetSingleStaffScheduleResponse(
    val schedule: List<ScheduleDate>,
) {
    class ScheduleDate(
        val date: LocalDate,
        val slots: List<ScheduleSlot>,
    )

    class ScheduleSlot(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val from: LocalTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val to: LocalTime,
    )
}

class AddMultipleStaffScheduleRequest(
    @field:Valid
    val schedule: List<ScheduleDate>,
) {
    class ScheduleDate(
        val staffId: Long,
        val operation: ScheduleOperation,
        val date: LocalDate,
        @field:NotEmpty
        val slots: List<ScheduleSlot> = emptyList(),
    )

    class ScheduleSlot(
        val operation: ScheduleOperation,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val from: LocalTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val to: LocalTime,
    )

    enum class ScheduleOperation {
        ADD, DELETE
    }
}


class GetMultipleStaffScheduleResponse(
    val schedule: List<ScheduleDate>,
) {
    class ScheduleDate(
        val staffId: Long,
        val date: LocalDate,
        val slots: List<ScheduleSlot>,
    )

    class ScheduleSlot(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val from: LocalTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val to: LocalTime,
    )
}
