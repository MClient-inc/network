package ru.mclient.network.record.domain

import ru.mclient.network.branch.domain.CompanyBranchEntity
import ru.mclient.network.clients.domain.ClientEntity
import ru.mclient.network.service.domain.ServiceToCompanyEntity
import ru.mclient.network.staff.domain.StaffScheduleEntity
import java.time.LocalTime
import javax.persistence.*


@Entity
@Table(name = "records")
class RecordEntity(
    @Column(name = "start_time")
    var time: LocalTime,
    @ManyToOne
    @JoinColumn
    var schedule: StaffScheduleEntity,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    var company: CompanyBranchEntity,
    @ManyToOne
    @JoinColumn
    var client: ClientEntity,
    var status: VisitStatus = VisitStatus.WAITING,
    @ManyToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var services: List<ServiceToRecordEntity> = emptyList(),
    @Id
    @GeneratedValue
    var id: Long = 0,
) {
    enum class VisitStatus {
        WAITING, COME, NOT_COME,
    }
}

@Entity
@Table(name = "service_to_records")
class ServiceToRecordEntity(
    @ManyToOne
    @JoinColumn
    var record: RecordEntity,
    @ManyToOne
    @JoinColumn(name = "service")
    var serviceToCompany: ServiceToCompanyEntity,
    @Id
    @GeneratedValue
    var id: Long = 0,
)