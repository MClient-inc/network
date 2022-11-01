package ru.mclient.network.record.controller

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import ru.mclient.network.branch.service.CompanyBranchService
import ru.mclient.network.clients.service.ClientsService
import ru.mclient.network.record.service.RecordService
import ru.mclient.network.service.service.ServiceService
import ru.mclient.network.staff.service.StaffService
import java.time.LocalDate
import java.time.LocalTime

@RestController
class RecordController(
    private val staffService: StaffService,
    private val recordService: RecordService,
    private val serviceService: ServiceService,
    private val companyBranchService: CompanyBranchService,
    private val clientsService: ClientsService,
) {

    @GetMapping("/companies/{companyQuery}/records")
    fun getRecordsForCompany(
        @PathVariable companyQuery: String,
        @RequestParam(required = false) from: LocalDate?,
        @RequestParam(required = false) to: LocalDate?,
        @RequestParam(required = false) staffIds: List<Long>?,
        @RequestParam(required = false) limit: Int?,
    ): GetRecordsResponse {
        val company = companyBranchService.findByIdOrCodename(companyQuery)
        val staff = if (staffIds.isNullOrEmpty()) {
            staffService.findAllStaffForCompany(company)
        } else {
            staffService.findStaffByIds(staffIds)
        }
        val records =
            recordService.findRecordsByStaff(staff, from, to, limit ?: 10)
        return GetRecordsResponse(
            records.groupBy { it.schedule }.map { (schedule, records) ->
                GetRecordsResponse.RecordsGroup(
                    schedule = GetRecordsResponse.Schedule(
                        id = schedule.id,
                        date = schedule.date,
                        from = schedule.from,
                        to = schedule.to,
                        staff = GetRecordsResponse.Staff(
                            id = schedule.staff.id,
                            name = schedule.staff.name,
                        )
                    ),
                    records = records.map { record ->
                        GetRecordsResponse.Record(
                            id = record.id,
                            time = GetRecordsResponse.TimeOffset(
                                from = record.time,
                                to = record.time.plusMinutes(record.services.maxOfOrNull { it.serviceToCompany.durationInMinutes }
                                    ?.toLong() ?: 0L)
                            ),
                            client = GetRecordsResponse.Client(
                                id = record.client.id,
                                name = record.client.name,
                                phone = record.client.phone.orEmpty(),
                            ),
                            services = record.services.map {
                                GetRecordsResponse.Service(
                                    it.serviceToCompany.service.id,
                                    it.serviceToCompany.service.title,
                                )
                            }
                        )
                    }
                )
            }
        )
    }

    @PostMapping("/companies/{companyQuery}/records")
    fun createRecord(
        @PathVariable companyQuery: String,
        @RequestBody data: CreateRecordRequest,
    ): CreateRecordResponse {
        val company = companyBranchService.findByIdOrCodename(companyQuery)
        val staff = staffService.findByStaffId(data.staffId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "staff not found"
        )
        val client = clientsService.findClientById(data.clientId) ?: throw ResponseStatusException(
            HttpStatus.NOT_FOUND,
            "client not found"
        )
        val services = serviceService.findServicesByIdsAndCompany(data.services, company)
        val record = recordService.createRecord(
            staff = staff,
            date = data.date,
            time = data.time,
            client = client,
            company = company,
            services = services,
        )
        return CreateRecordResponse(
            id = record.id,
            client = CreateRecordResponse.Client(
                id = record.client.id,
                name = record.client.name,
                phone = record.client.phone.orEmpty(),
            ),
            time = CreateRecordResponse.TimeOffset(
                record.time,
                record.time.plusMinutes(services.maxOfOrNull { it.durationInMinutes }?.toLong() ?: 0L)
            ),
            schedule = CreateRecordResponse.Schedule(
                id = record.schedule.id,
                date = record.schedule.date,
                staff = CreateRecordResponse.Staff(
                    id = record.schedule.staff.id,
                    name = record.schedule.staff.name,
                ),
                start = record.schedule.from,
                end = record.schedule.to,
            ),
            services = record.services.map {
                CreateRecordResponse.Service(
                    id = it.serviceToCompany.service.id,
                    title = it.serviceToCompany.service.title,
                )
            },
        )
    }

}

class CreateRecordRequest(
    val staffId: Long,
    val clientId: Long,
    val date: LocalDate,
    val time: LocalTime,
    val services: List<Long>,
)

class CreateRecordResponse(
    val id: Long,
    val time: TimeOffset,
    val client: Client,
    val services: List<Service>,
    val schedule: Schedule,
) {

    class Service(
        val id: Long,
        val title: String,
    )

    class Client(
        val id: Long,
        val name: String,
        val phone: String,
    )

    class Staff(
        val name: String,
        val id: Long,
    )

    class TimeOffset(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val start: LocalTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val end: LocalTime,
    )

    class Schedule(
        val id: Long,
        val date: LocalDate,
        val staff: Staff,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val start: LocalTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val end: LocalTime,
    )
}

class GetRecordsResponse(
    val records: List<RecordsGroup>,
) {

    class RecordsGroup(
        val schedule: Schedule,
        val records: List<Record>,
    )

    class Record(
        val id: Long,
        val time: TimeOffset,
        val client: Client,
        val services: List<Service>,
    )

    class Service(
        val id: Long,
        val title: String,
    )

    class Client(
        val id: Long,
        val name: String,
        val phone: String,
    )

    class Staff(
        val name: String,
        val id: Long,
    )

    class TimeOffset(
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val from: LocalTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val to: LocalTime,
    )

    class Schedule(
        val id: Long,
        val date: LocalDate,
        val staff: Staff,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val from: LocalTime,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        val to: LocalTime,
    )

}