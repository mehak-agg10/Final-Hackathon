package com.bloomteen.pcosassessor.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bloomteen.pcosassessor.ui.theme.*
import com.bloomteen.pcosassessor.viewmodel.PcosSymptomQuestion
import com.bloomteen.pcosassessor.viewmodel.PcosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewSelfCheckScreen(
    viewModel: PcosViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var patientName by remember { mutableStateOf("") }
    var patientAge by remember { mutableStateOf("") }
    var nameError by remember { mutableStateOf<String?>(null) }
    var ageError by remember { mutableStateOf<String?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    val symptomQuestions by viewModel.symptomQuestions.collectAsState()
    val checkedCount = symptomQuestions.count { it.isChecked }
    val currentRiskLevel = viewModel.calculateRiskLevel(checkedCount)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "New PCOS Self-Check",
                            style = Typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = OnSurface,
                                fontSize = 17.sp
                            )
                        )
                        Text(
                            text = "Adolescent Confidential Screening",
                            style = Typography.bodySmall.copy(
                                color = OnSurfaceVariant,
                                fontSize = 11.sp
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Surface
                )
            )
        },
        containerColor = Surface
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 40.dp)
        ) {
            // Empathetic teen notice banner
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(SecondaryFixed.copy(alpha = 0.35f))
                        .border(1.dp, SecondaryFixed, RoundedCornerShape(14.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🌿", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Take your time. Symptoms during adolescence are common, and tracking them accurately helps your doctor support your hormonal health.",
                        color = Secondary,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }

            // Section 1: Patient Information Card
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceContainerLowest),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(OutlineVariant, OutlineVariant))),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(PrimaryFixed),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "1",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Primary
                                )
                            }
                            Text(
                                text = "Patient Information",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnSurface
                            )
                        }

                        // Name Field
                        OutlinedTextField(
                            value = patientName,
                            onValueChange = {
                                patientName = it
                                if (nameError != null) nameError = null
                            },
                            label = { Text("Teen Patient Name") },
                            placeholder = { Text("e.g. Maya Lin") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Primary
                                )
                            },
                            isError = nameError != null,
                            supportingText = {
                                nameError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Primary,
                                focusedLabelColor = Primary
                            )
                        )

                        // Age Field
                        OutlinedTextField(
                            value = patientAge,
                            onValueChange = {
                                if (it.all { char -> char.isDigit() } && it.length <= 2) {
                                    patientAge = it
                                    if (ageError != null) ageError = null
                                }
                            },
                            label = { Text("Patient Age (Years)") },
                            placeholder = { Text("e.g. 16") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.CalendarToday,
                                    contentDescription = null,
                                    tint = Primary
                                )
                            },
                            isError = ageError != null,
                            supportingText = {
                                ageError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Primary,
                                focusedLabelColor = Primary
                            )
                        )
                    }
                }
            }

            // Section 2: 6 PCOS Symptom Checkboxes
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(PrimaryFixed),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "2",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }
                    Column {
                        Text(
                            text = "PCOS Symptom Checklist (6 Signs)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurface
                        )
                        Text(
                            text = "Check all symptoms present for 6+ months",
                            fontSize = 11.sp,
                            color = OnSurfaceVariant
                        )
                    }
                }
            }

            // 6 Checkbox Question Items
            items(
                items = symptomQuestions,
                key = { it.id }
            ) { question ->
                SymptomCheckboxCard(
                    question = question,
                    onToggle = { viewModel.toggleSymptom(question.id) }
                )
            }

            // Section 3: Live Risk Level Preview & Score Summary
            item {
                LiveRiskPreviewCard(
                    score = checkedCount,
                    riskLevel = currentRiskLevel
                )
            }

            // Section 4: Submit Button
            item {
                Button(
                    onClick = {
                        var hasError = false
                        if (patientName.isBlank()) {
                            nameError = "Please enter the teen's name"
                            hasError = true
                        }
                        val ageInt = patientAge.toIntOrNull()
                        if (ageInt == null || ageInt !in 10..25) {
                            ageError = "Please enter a valid age between 10 and 25"
                            hasError = true
                        }

                        if (!hasError && ageInt != null) {
                            isSubmitting = true
                            viewModel.saveNewAssessment(
                                name = patientName,
                                age = ageInt,
                                onComplete = {
                                    isSubmitting = false
                                    onNavigateBack()
                                }
                            )
                        }
                    },
                    enabled = !isSubmitting,
                    shape = RoundedCornerShape(999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = OnPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Calculate & Save Assessment",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SymptomCheckboxCard(
    question: PcosSymptomQuestion,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isChecked = question.isChecked

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isChecked) PrimaryFixed.copy(alpha = 0.25f) else SurfaceContainerLowest
            )
            .border(
                1.dp,
                if (isChecked) Primary else OutlineVariant.copy(alpha = 0.7f),
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onToggle)
            .padding(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Custom Rounded Checkbox matching DESIGN.md (#5B8E7D Sage Mint when checked)
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (isChecked) Secondary else Color.Transparent)
                    .border(
                        2.dp,
                        if (isChecked) Secondary else Outline,
                        RoundedCornerShape(6.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Checked",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = question.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                }

                Text(
                    text = question.description,
                    fontSize = 11.sp,
                    color = OnSurfaceVariant,
                    lineHeight = 15.sp
                )

                Text(
                    text = question.chipLabel,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isChecked) Primary else OnSurfaceVariant,
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (isChecked) PrimaryFixed.copy(alpha = 0.6f) else SurfaceContainer)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun LiveRiskPreviewCard(
    score: Int,
    riskLevel: String,
    modifier: Modifier = Modifier
) {
    val isHigh = riskLevel == "High Risk"
    val isModerate = riskLevel == "Moderate Risk"

    val textColor = when {
        isHigh -> RiskHighText
        isModerate -> RiskModerateText
        else -> RiskLowText
    }
    val bgColor = when {
        isHigh -> RiskHighBg
        isModerate -> RiskModerateBg
        else -> RiskLowBg
    }
    val borderColor = when {
        isHigh -> RiskHighBorder
        isModerate -> RiskModerateBorder
        else -> RiskLowBorder
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Live Risk Preview",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = OnSurfaceVariant
                    )
                    Text(
                        text = "Score: $score of 6 symptoms (+1 per symptom)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnSurface
                    )
                }

                // Dynamic Risk Level Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(Color.White)
                        .border(1.dp, borderColor, RoundedCornerShape(999.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(textColor)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = riskLevel,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                }
            }

            // 6-Segment Progress Bar Preview
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (i in 1..6) {
                    val isFilled = i <= score
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .background(if (isFilled) textColor else Color.White)
                    )
                }
            }

            // Guidance Note
            Text(
                text = when {
                    isHigh -> "• Score 4-6: High Risk — Pediatric endocrine / gynecological review recommended."
                    isModerate -> "• Score 2-3: Moderate Risk — 3-month menstrual & lifestyle monitoring advised."
                    else -> "• Score 0-1: Low Risk — Normal pubertal physiological variation."
                },
                fontSize = 11.sp,
                color = textColor,
                lineHeight = 15.sp
            )
        }
    }
}
