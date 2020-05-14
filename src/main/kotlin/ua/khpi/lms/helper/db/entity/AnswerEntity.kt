package ua.khpi.lms.helper.db.entity

import javax.persistence.*

@Entity
@Table(name = "answer")
data class AnswerEntity(
        @Id
        @Column(name = "answer_id")
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        var answerId: Long?,
        @Column(name = "text")
        var text: String,
        @Column(name = "is_correct")
        var correct: Boolean,
        @Column(name = "question_id")
        var questionId: Long? = null
)
