package edu.moravian.survey

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.moravian.survey.data.SurveyDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<SurveyDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("survey.db")
    return Room.databaseBuilder<SurveyDatabase>(
        context = appContext,
        name = dbFile.absolutePath,
    )
}