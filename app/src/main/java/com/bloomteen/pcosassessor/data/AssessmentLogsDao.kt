package com.bloomteen.pcosassessor.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AssessmentLogsDao {

    @Query("SELECT * FROM Assessment_Logs ORDER BY id DESC")
    fun getAllAssessments(): Flow<List<Assessment_Logs>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssessment(assessment: Assessment_Logs): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(assessments: List<Assessment_Logs>)

    @Query("SELECT COUNT(*) FROM Assessment_Logs")
    fun getAssessmentCount(): Flow<Int>

    @Delete
    suspend fun deleteAssessment(assessment: Assessment_Logs)

    @Query("DELETE FROM Assessment_Logs")
    suspend fun deleteAll()
}
