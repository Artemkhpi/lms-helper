package ua.khpi.lms.helper.db.repository.jpa

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ua.khpi.lms.helper.db.entity.SequenceEntity

@Repository
interface SequenceJpaRepository : CrudRepository<SequenceEntity, Long>
