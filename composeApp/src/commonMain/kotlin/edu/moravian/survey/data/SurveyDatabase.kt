package edu.moravian.survey.data

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.TypeConverters
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

//Can change the entities used, wanted to fill something in here for now.
@Database(
    entities = [
        SurveyEntity::class, SurveyAnswerEntity::class,
    ],
    version = 2,
)
@TypeConverters(Converters::class) // use the Set converter any time it sees a Set<Int>
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
    .fallbackToDestructiveMigration(true) // destroys old data upon a version migration
    .build()
