package com.bloomteen.pcosassessor

import com.bloomteen.pcosassessor.data.Assessment_Logs
import org.junit.Assert.assertEquals
import org.junit.Test

class PcosRiskCalculationTest {

    private fun calculateRisk(score: Int): String {
        return when (score) {
            in 4..6 -> "High Risk"
            in 2..3 -> "Moderate Risk"
            else -> "Low Risk"
        }
    }

    @Test
    fun testLowRiskScoring() {
        assertEquals("Low Risk", calculateRisk(0))
        assertEquals("Low Risk", calculateRisk(1))
    }

    @Test
    fun testModerateRiskScoring() {
        assertEquals("Moderate Risk", calculateRisk(2))
        assertEquals("Moderate Risk", calculateRisk(3))
    }

    @Test
    fun testHighRiskScoring() {
        assertEquals("High Risk", calculateRisk(4))
        assertEquals("High Risk", calculateRisk(5))
        assertEquals("High Risk", calculateRisk(6))
    }

    @Test
    fun testAssessmentLogsEntity() {
        val log = Assessment_Logs(
            id = 1,
            patient_name = "Maya Lin",
            age = 16,
            symptom_score = 5,
            risk_level = "High Risk",
            assessment_date = "Assessed Today",
            reported_symptoms = "✨ Cycle irregularity,🌿 Hormonal acne"
        )

        assertEquals(1, log.id)
        assertEquals("Maya Lin", log.patient_name)
        assertEquals(16, log.age)
        assertEquals(5, log.symptom_score)
        assertEquals("High Risk", log.risk_level)
    }
}
