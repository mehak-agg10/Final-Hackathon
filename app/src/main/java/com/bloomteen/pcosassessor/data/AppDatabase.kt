package com.bloomteen.pcosassessor.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Assessment_Logs::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun assessmentLogsDao(): AssessmentLogsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "bloomteen_pcos_database"
                )
                    .addCallback(DatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateInitialData(database.assessmentLogsDao())
                    }
                }
            }

            suspend fun populateInitialData(dao: AssessmentLogsDao) {
                val initialPatients = listOf(
                    Assessment_Logs(
                        patient_name = "Ananya Patel",
                        age = 17,
                        symptom_score = 5,
                        risk_level = "High Risk",
                        assessment_date = "Assessed Today • 09:40 AM",
                        reported_symptoms = "✨ Cycle irregularity,🌿 Hormonal acne,🌸 Hirsutism"
                    ),
                    Assessment_Logs(
                        patient_name = "Chloe Bennett",
                        age = 15,
                        symptom_score = 2,
                        risk_level = "Low Risk",
                        assessment_date = "Assessed Today • 11:15 AM",
                        reported_symptoms = "🌿 Mild skin changes,✨ Routine regular cycle"
                    ),
                    Assessment_Logs(
                        patient_name = "Sofia Ramirez",
                        age = 18,
                        symptom_score = 4,
                        risk_level = "Moderate Risk",
                        assessment_date = "Assessed Yesterday • 04:20 PM",
                        reported_symptoms = "✨ Acanthosis nigricans,🌸 Skipped cycles"
                    ),
                    Assessment_Logs(
                        patient_name = "Zara Khan",
                        age = 16,
                        symptom_score = 6,
                        risk_level = "High Risk",
                        assessment_date = "Assessed Oct 14",
                        reported_symptoms = "✨ Primary amenorrhea,🌿 Severe hirsutism"
                    ),
                    Assessment_Logs(
                        patient_name = "Emma Watson",
                        age = 14,
                        symptom_score = 3,
                        risk_level = "Moderate Risk",
                        assessment_date = "Assessed Oct 12",
                        reported_symptoms = "✨ Hair thinning,🌿 Mild fatigue"
                    ),
                    Assessment_Logs(
                        patient_name = "Mia Takahashi",
                        age = 16,
                        symptom_score = 1,
                        risk_level = "Low Risk",
                        assessment_date = "Assessed Oct 08",
                        reported_symptoms = "✨ Post-menarche normal variation"
                    )
                )
                dao.insertAll(initialPatients)
            }
        }
    }
}
