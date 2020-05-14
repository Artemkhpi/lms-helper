package ua.khpi.lms.helper.db.entity

import javax.persistence.*

@Entity
@Table(name = "test")
data class TestEntity(
        @Id
        @Column(name = "test_id")
        var testId: Long,
        @OneToMany(mappedBy = "testId", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        var questions: Set<QuestionEntity>
)
