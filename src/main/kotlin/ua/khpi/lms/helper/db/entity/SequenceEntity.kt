package ua.khpi.lms.helper.db.entity

import ua.khpi.lms.helper.mocks.db.converter.ListToStringConverter
import javax.persistence.*

@Entity
@Table(name = "sequence")
data class SequenceEntity(
        @Id
        @Column(name = "sequence_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var sequenceId: Long?,
        @Column(name = "articles")
        @Convert(converter = ListToStringConverter::class)
        var articles: List<String>
)
