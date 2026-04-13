package edu.moravian.survey.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface SurveyDao {
    @Insert
    suspend fun insertSurvey(survey: SurveyEntity): Long

    @Insert
    suspend fun insertAnswers(answers: List<SurveyAnswerEntity>)

    @Transaction
    suspend fun insertSurveyWithAnswers(survey: SurveyEntity, answers: List<SurveyAnswerEntity>) {
        val id = insertSurvey(survey)
        val answersWithId = answers.map { it.copy(surveyId = id) }
        insertAnswers(answersWithId)
    }

    @Query("SELECT * FROM SurveyEntity ORDER BY dateTime DESC")
    fun getAllSurveys(): Flow<List<SurveyEntity>>

    @Query("SELECT * FROM SurveyEntity WHERE id = :id")
    suspend fun getSurveyById(id: Long): SurveyEntity?

    @Query("SELECT * FROM SurveyAnswerEntity WHERE surveyId = :surveyId")
    suspend fun getAnswersForSurvey(surveyId: Long): List<SurveyAnswerEntity>
}