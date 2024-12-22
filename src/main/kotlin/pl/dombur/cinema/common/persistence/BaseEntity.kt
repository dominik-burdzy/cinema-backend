package pl.dombur.cinema.common.persistence

import jakarta.persistence.Basic
import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.OffsetDateTime
import java.util.UUID

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity(
    referenceId: UUID? = null,
) {
    @Id
    var referenceId: UUID = referenceId ?: UUID.randomUUID()

    @Basic
    @CreatedDate
    @Column(updatable = false, nullable = false)
    var createdAt: OffsetDateTime = OffsetDateTime.now()

    @Basic
    @LastModifiedDate
    @Column(nullable = false)
    var modifiedAt: OffsetDateTime = OffsetDateTime.now()
}
