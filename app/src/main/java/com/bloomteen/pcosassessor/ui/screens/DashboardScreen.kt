package com.bloomteen.pcosassessor.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bloomteen.pcosassessor.ui.components.*
import com.bloomteen.pcosassessor.ui.theme.*
import com.bloomteen.pcosassessor.viewmodel.PcosViewModel

@Composable
fun DashboardScreen(
    viewModel: PcosViewModel,
    onNavigateToNewSelfCheck: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopHeaderBar()
        },
        bottomBar = {
            BottomNavBar(
                activeTab = "dashboard",
                onNavigateToSelfCheck = onNavigateToNewSelfCheck,
                onNavigateToDashboard = { /* Already here */ }
            )
        },
        floatingActionButton = {
            // Floating "+ New Self-Check" button matching web design
            Button(
                onClick = onNavigateToNewSelfCheck,
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier
                    .shadow(elevation = 12.dp, shape = RoundedCornerShape(999.dp), ambientColor = Primary, spotColor = Primary)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(999.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Primary, PrimaryContainer)
                            )
                        )
                        .padding(horizontal = 20.dp, vertical = 13.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null,
                            tint = OnPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "+ New Self-Check",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnPrimary,
                            letterSpacing = 0.3.sp
                        )
                    }
                }
            }
        },
        containerColor = Surface
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 80.dp)
        ) {
            // 1. Doctor & Care Team Banner
            item {
                CareTeamBanner()
            }

            // 2. Clinician Hero Card
            item {
                ClinicianHeroCard(patientCount = uiState.totalCount)
            }

            // 3. Summary Bento Grid (High, Moderate, Low counts)
            item {
                SummaryBentoGrid(
                    highCount = uiState.highCount,
                    moderateCount = uiState.moderateCount,
                    lowCount = uiState.lowCount
                )
            }

            // 4. Search and Filter Bar
            item {
                SearchAndFilterBar(
                    searchQuery = uiState.searchQuery,
                    onSearchQueryChange = viewModel::onSearchQueryChanged,
                    selectedFilter = uiState.selectedFilter,
                    onFilterSelected = viewModel::onFilterSelected,
                    totalCount = uiState.totalCount,
                    highCount = uiState.highCount,
                    moderateCount = uiState.moderateCount,
                    lowCount = uiState.lowCount
                )
            }

            // 5. Educational Rotterdam Consensus Protocol Card
            item {
                ProtocolCard()
            }

            // 6. Dynamic Patient Cards or Empty State
            if (uiState.filteredAssessments.isEmpty() && !uiState.isLoading) {
                item {
                    EmptySearchState(
                        onResetFilters = {
                            viewModel.onSearchQueryChanged("")
                            viewModel.onFilterSelected(RiskFilter.ALL)
                        }
                    )
                }
            } else {
                items(
                    items = uiState.filteredAssessments,
                    key = { it.id }
                ) { patient ->
                    PatientCardItem(patient = patient)
                }
            }
        }
    }
}

@Composable
private fun EmptySearchState(
    onResetFilters: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(SurfaceContainerLowest)
            .border(1.dp, OutlineVariant, RoundedCornerShape(20.dp))
            .padding(28.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .clip(CircleShape)
                    .background(SurfaceContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🌱", fontSize = 24.sp)
            }

            Text(
                text = "No teen records match",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurface
            )

            Text(
                text = "Try clearing your search query or switching your risk filter.",
                fontSize = 12.sp,
                color = OnSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onResetFilters,
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primary)
            ) {
                Text(
                    text = "Reset Search & Filters",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnPrimary
                )
            }
        }
    }
}
