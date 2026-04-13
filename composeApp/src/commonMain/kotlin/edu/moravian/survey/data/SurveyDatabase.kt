package edu.moravian.survey.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

//Can change the entities used, wanted to fill something in here for now.
@Database(
    entities = [
        SurveyEntity::class, SurveyAnswerEntity::class,
    ],
    version = 1,
)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class SurveyDatabase : RoomDatabase() {
    abstract fun getDao(): SurveyDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<SurveyDatabase> {
    override fun initialize(): SurveyDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<SurveyDatabase>,
): SurveyDatabase = builder
    .setDriver(BundledSQLiteDriver())
    .setQueryCoroutineContext(Dispatchers.IO)
    .build()
