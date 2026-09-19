package com.bloomteen.pcosassessor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bloomteen.pcosassessor.data.Assessment_Logs
import com.bloomteen.pcosassessor.ui.theme.*

@Composable
fun PatientCardItem(
    patient: Assessment_Logs,
    modifier: Modifier = Modifier
) {
    val isHigh = patient.risk_level.contains("High", ignoreCase = true)
    val isModerate = patient.risk_level.contains("Moderate", ignoreCase = true)

    val riskTextColor = when {
        isHigh -> RiskHighText
        isModerate -> RiskModerateText
        else -> RiskLowText
    }
    val riskBgColor = when {
        isHigh -> RiskHighBg
        isModerate -> RiskModerateBg
        else -> RiskLowBg
    }
    val riskBorderColor = when {
        isHigh -> RiskHighBorder
        isModerate -> RiskModerateBorder
        else -> RiskLowBorder
    }
    val riskIcon = when {
        isHigh -> Icons.Default.Warning
        isModerate -> Icons.Default.Visibility
        else -> Icons.Default.CheckCircle
    }
    val riskLabel = when {
        isHigh -> "High PCOS Risk (${patient.symptom_score}/6)"
        isModerate -> "PCOS Monitoring (${patient.symptom_score}/6)"
        else -> "Low PCOS Risk (${patient.symptom_score}/6)"
    }

    // Dynamic initials
    val initials = patient.patient_name
        .split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .mapNotNull { it.firstOrNull()?.uppercaseChar() }
        .joinToString("")
        .ifEmpty { "PT" }

    // Avatar gradient
    val avatarGradient = when {
        isHigh -> listOf(Color(0xFFFBCFE8), Color(0xFFFFE4E6))
        isModerate -> listOf(Color(0xFFE9D5FF), Color(0xFFEDE9FE))
        else -> listOf(Color(0xFFA7F3D0), Color(0xFFCCFBF1))
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceContainerLowest)
            .border(1.dp, OutlineVariant.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header Row: Avatar, Name, Age, Risk Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    // Avatar Box
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Brush.linearGradient(avatarGradient))
                            .border(2.dp, Color.White, RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            color = riskTextColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = patient.patient_name,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                            // Age chip
                            Text(
                                text = "Age ${patient.age}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,
                                color = OnSurfaceVariant,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(999.dp))
                                    .background(SurfaceContainer)
                                    .padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }

                        Text(
                            text = patient.assessment_date,
                            fontSize = 11.sp,
                            color = OnSurfaceVariant.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Risk Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(riskBgColor)
                        .border(1.dp, riskBorderColor, RoundedCornerShape(999.dp))
                        .padding(horizontal = 9.dp, vertical = 4.dp)
                ) {
                    Icon(
                        imageVector = riskIcon,
                        contentDescription = null,
                        tint = riskTextColor,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = riskLabel,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = riskTextColor
                    )
                }
            }

            // Signs & Symptoms Breakdown Box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceContainerLow.copy(alpha = 0.7f))
                    .border(1.dp, SurfaceContainerHigh, RoundedCornerShape(14.dp))
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Signs & Symptoms",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = OnSurfaceVariant
                    )
                    Text(
                        text = "${patient.symptom_score} of 6 signs reported",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = riskTextColor
                    )
                }

                // 6-Segment Progress Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (i in 1..6) {
                        val isFilled = i <= patient.symptom_score
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(5.dp)
                                .clip(RoundedCornerShape(999.dp))
                                .background(
                                    if (isFilled) riskTextColor else SurfaceContainerHighest
                                )
                        )
                    }
                }

                // Symptom Chips
                val symptomsList = if (patient.reported_symptoms.isNotBlank()) {
                    patient.reported_symptoms.split(",").filter { it.isNotBlank() }
                } else {
                    when {
                        isHigh -> listOf("✨ Cycle irregularity", "🌿 Hormonal acne", "🌸 Hirsutism")
                        isModerate -> listOf("✨ Acanthosis nigricans", "🌸 Skipped cycles")
                        else -> listOf("🌿 Mild skin changes", "✨ Routine regular cycle")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    symptomsList.take(3).forEach { symptom ->
                        Text(
                            text = symptom.trim(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = OnSurfaceVariant,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White)
                                .border(1.dp, OutlineVariant.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            // Next Steps & Guidance Box
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(riskBgColor.copy(alpha = 0.7f))
                    .border(1.dp, riskBorderColor, RoundedCornerShape(12.dp))
                    .padding(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = when {
                        isHigh -> Icons.Default.MedicalServices
                        isModerate -> Icons.Default.CalendarMonth
                        else -> Icons.Default.Spa
                    },
                    contentDescription = null,
                    tint = riskTextColor,
                    modifier = Modifier
                        .size(15.dp)
                        .padding(top = 1.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Next Steps & Guidance:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = riskTextColor
                    )
                    Text(
                        text = when {
                            isHigh -> "Pediatric endocrine lab work & pelvic ultrasound evaluation. Discuss gentle cycle regulation."
                            isModerate -> "3-month cycle tracking & dietary insulin management check-in. Gentle lifestyle support."
                            else -> "Routine annual check & reassure about normal pubertal cycle variation. Continue cycle log."
                        },
                        fontSize = 11.sp,
                        color = OnSurfaceVariant,
                        lineHeight = 15.sp
                    )
                }
            }

            // Footer Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = when {
                        isHigh -> "Pediatric endocrinology review recommended"
                        isModerate -> "Nutritional counseling & routine insulin checkup"
                        else -> "Stable profile. Reassure teen on natural shifts."
                    },
                    fontSize = 11.sp,
                    color = OnSurfaceVariant,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // View Plan button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(
                                if (isHigh) PrimaryFixed.copy(alpha = 0.5f)
                                else if (isModerate) PrimaryFixed.copy(alpha = 0.5f)
                                else SecondaryFixed.copy(alpha = 0.6f)
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "View Plan 💬",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (patient.risk_level.contains("Low", ignoreCase = true)) Secondary else Primary
                        )
                    }

                    // Nudge icon
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(SurfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "💌", fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
