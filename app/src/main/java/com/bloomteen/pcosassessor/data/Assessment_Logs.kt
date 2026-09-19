package com.bloomteen.pcosassessor.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Assessment_Logs")
data class Assessment_Logs(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "patient_name")
    val patient_name: String,

    @ColumnInfo(name = "age")
    val age: Int,

    @ColumnInfo(name = "symptom_score")
    val symptom_score: Int,

    @ColumnInfo(name = "risk_level")
    val risk_level: String, // "High Risk", "Moderate Risk", "Low Risk"

    @ColumnInfo(name = "assessment_date")
    val assessment_date: String = "Today",

    @ColumnInfo(name = "reported_symptoms")
    val reported_symptoms: String = ""
)
