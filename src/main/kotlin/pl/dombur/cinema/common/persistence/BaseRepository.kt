package pl.dombur.cinema.common.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.NoRepositoryBean
import java.util.UUID

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> :
    JpaRepository<T, UUID>,
    JpaSpecificationExecutor<T> {
    fun findByReferenceId(referenceId: UUID): T?
}
