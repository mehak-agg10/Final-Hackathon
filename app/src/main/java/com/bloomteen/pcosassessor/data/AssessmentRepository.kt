package com.bloomteen.pcosassessor.data

import kotlinx.coroutines.flow.Flow

class AssessmentRepository(private val dao: AssessmentLogsDao) {

    val allAssessments: Flow<List<Assessment_Logs>> = dao.getAllAssessments()

    suspend fun insert(assessment: Assessment_Logs): Long {
        return dao.insertAssessment(assessment)
    }

    suspend fun delete(assessment: Assessment_Logs) {
        dao.deleteAssessment(assessment)
    }
}
