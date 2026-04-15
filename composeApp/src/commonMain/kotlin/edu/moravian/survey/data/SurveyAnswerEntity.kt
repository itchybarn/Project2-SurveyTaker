package edu.moravian.survey.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = SurveyEntity::class, // connected to the SurveyEntity table
            parentColumns = ["id"], // points to unique ID of survey attempt in parent table
            childColumns = ["surveyId"], // column in current table that stores the above ID
            onDelete = ForeignKey.CASCADE // if a survey is deleted, this child surveyAnswer will be deleted too (this is cool)
        )
    ]
)
data class SurveyAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val surveyId: Long? = 0,
    val questionId: String,
    val selection: Set<Int>?,
    val otherText: String? = null
)
