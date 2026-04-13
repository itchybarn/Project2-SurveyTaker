package edu.moravian.survey.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SurveyEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateTime: Long,
    val score: Int = 0,
)