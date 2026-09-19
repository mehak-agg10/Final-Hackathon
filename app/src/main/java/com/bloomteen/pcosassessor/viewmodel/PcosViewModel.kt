package com.bloomteen.pcosassessor.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.bloomteen.pcosassessor.data.AppDatabase
import com.bloomteen.pcosassessor.data.AssessmentRepository
import com.bloomteen.pcosassessor.data.Assessment_Logs
import com.bloomteen.pcosassessor.ui.components.RiskFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class PcosSymptomQuestion(
    val id: Int,
    val title: String,
    val description: String,
    val chipLabel: String,
    val iconEmoji: String = "✨",
    val isChecked: Boolean = false
)

data class DashboardUiState(
    val allAssessments: List<Assessment_Logs> = emptyList(),
    val filteredAssessments: List<Assessment_Logs> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: RiskFilter = RiskFilter.ALL,
    val totalCount: Int = 0,
    val highCount: Int = 0,
    val moderateCount: Int = 0,
    val lowCount: Int = 0,
    val isLoading: Boolean = true
)

class PcosViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AssessmentRepository

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedFilter = MutableStateFlow(RiskFilter.ALL)
    val selectedFilter = _selectedFilter.asStateFlow()

    // 6 Standard PCOS Checkbox questions
    private val _symptomQuestions = MutableStateFlow(getDefaultQuestions())
    val symptomQuestions = _symptomQuestions.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application, viewModelScope)
        repository = AssessmentRepository(database.assessmentLogsDao())
    }

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.allAssessments,
        _searchQuery,
        _selectedFilter
    ) { list, query, filter ->
        val total = list.size
        val high = list.count { it.risk_level.contains("High", ignoreCase = true) }
        val moderate = list.count { it.risk_level.contains("Moderate", ignoreCase = true) }
        val low = list.count { it.risk_level.contains("Low", ignoreCase = true) }

        val filtered = list.filter { patient ->
            val matchesFilter = when (filter) {
                RiskFilter.ALL -> true
                RiskFilter.HIGH -> patient.risk_level.contains("High", ignoreCase = true)
                RiskFilter.MODERATE -> patient.risk_level.contains("Moderate", ignoreCase = true)
                RiskFilter.LOW -> patient.risk_level.contains("Low", ignoreCase = true)
            }

            val q = query.trim().lowercase()
            val matchesQuery = q.isEmpty() ||
                    patient.patient_name.lowercase().contains(q) ||
                    patient.reported_symptoms.lowercase().contains(q) ||
                    patient.risk_level.lowercase().contains(q)

            matchesFilter && matchesQuery
        }

        DashboardUiState(
            allAssessments = list,
            filteredAssessments = filtered,
            searchQuery = query,
            selectedFilter = filter,
            totalCount = total,
            highCount = high,
            moderateCount = moderate,
            lowCount = low,
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DashboardUiState()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelected(filter: RiskFilter) {
        _selectedFilter.value = filter
    }

    fun toggleSymptom(symptomId: Int) {
        _symptomQuestions.update { list ->
            list.map { item ->
                if (item.id == symptomId) item.copy(isChecked = !item.isChecked) else item
            }
        }
    }

    fun resetSymptomForm() {
        _symptomQuestions.value = getDefaultQuestions()
    }

    /**
     * Logic:
     * When calculating the risk level, add 1 point for each checked symptom.
     * Score 4-6 is 'High Risk'
     * Score 2-3 is 'Moderate Risk'
     * Score 0-1 is 'Low Risk'
     */
    fun calculateRiskLevel(score: Int): String {
        return when (score) {
            in 4..6 -> "High Risk"
            in 2..3 -> "Moderate Risk"
            else -> "Low Risk"
        }
    }

    fun saveNewAssessment(
        name: String,
        age: Int,
        onComplete: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentQuestions = _symptomQuestions.value
            val score = currentQuestions.count { it.isChecked }
            val riskLevel = calculateRiskLevel(score)

            val checkedTags = currentQuestions
                .filter { it.isChecked }
                .joinToString(",") { it.chipLabel }
                .ifEmpty { "✨ Routine checkup" }

            val newAssessment = Assessment_Logs(
                patient_name = name.trim(),
                age = age,
                symptom_score = score,
                risk_level = riskLevel,
                assessment_date = "Assessed Today • Just Now",
                reported_symptoms = checkedTags
            )

            repository.insert(newAssessment)

            // Reset form on main thread
            launch(Dispatchers.Main) {
                resetSymptomForm()
                onComplete()
            }
        }
    }

    private fun getDefaultQuestions(): List<PcosSymptomQuestion> {
        return listOf(
            PcosSymptomQuestion(
                id = 1,
                title = "Irregular or Absent Cycles",
                description = "Infrequent periods, cycles longer than 35-45 days, or skipped cycles (>2 yrs post-menarche).",
                chipLabel = "✨ Cycle irregularity",
                iconEmoji = "🌸"
            ),
            PcosSymptomQuestion(
                id = 2,
                title = "Persistent Hormonal Acne",
                description = "Moderate to cystic facial, jawline, or back acne resistant to regular skincare treatments.",
                chipLabel = "🌿 Hormonal acne",
                iconEmoji = "🌿"
            ),
            PcosSymptomQuestion(
                id = 3,
                title = "Excessive Hair Growth (Hirsutism)",
                description = "Noticeable darker, coarser hair on the upper lip, chin, chest, abdomen, or inner thighs.",
                chipLabel = "🌸 Hirsutism",
                iconEmoji = "✨"
            ),
            PcosSymptomQuestion(
                id = 4,
                title = "Scalp Hair Thinning",
                description = "Diffuse shedding or thinning along the center part line or crown (androgenic alopecia pattern).",
                chipLabel = "✨ Hair thinning",
                iconEmoji = "🌿"
            ),
            PcosSymptomQuestion(
                id = 5,
                title = "Darkened Skin Patches (Acanthosis Nigricans)",
                description = "Velvety hyperpigmented skin patches around the back of the neck, armpits, or groin folds.",
                chipLabel = "✨ Acanthosis nigricans",
                iconEmoji = "🌸"
            ),
            PcosSymptomQuestion(
                id = 6,
                title = "Unexplained Weight Shifts / Insulin Fatigue",
                description = "Difficulty managing weight, central body weight shifts, or intense sugar cravings with post-meal fatigue.",
                chipLabel = "🌿 Weight changes",
                iconEmoji = "✨"
            )
        )
    }
}
