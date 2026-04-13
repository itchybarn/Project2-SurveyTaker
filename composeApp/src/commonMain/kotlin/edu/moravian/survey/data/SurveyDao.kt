package edu.moravian.survey.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SurveyDao {
    @Insert
    suspend fun insertSurvey(survey: SurveyEntity)

    @Query("SELECT * FROM SurveyEntity ORDER BY dateTime DESC")
    fun getAllSurveys(): Flow<List<SurveyEntity>>

    @Query("SELECT * FROM SurveyEntity WHERE id = :id")
    suspend fun getSurveyById(id: Long): SurveyEntity?
}