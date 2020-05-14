package ua.khpi.lms.helper.db.entity

import javax.persistence.*

@Entity
@Table(name = "question")
data class QuestionEntity(
        @Id
        @Column(name = "question_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var questionId: Long?,
        @Column(name = "text")
        var question: String,
        @OneToMany(mappedBy = "questionId", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
        var answers: Set<AnswerEntity>,
        @Column(name = "test_id")
        var testId: Long? = null
)
