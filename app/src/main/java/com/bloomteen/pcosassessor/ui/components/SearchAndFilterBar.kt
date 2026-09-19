package com.bloomteen.pcosassessor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bloomteen.pcosassessor.ui.theme.*

enum class RiskFilter(val label: String) {
    ALL("All Teens"),
    HIGH("Needs Care"),
    MODERATE("Monitoring"),
    LOW("Thriving")
}

@Composable
fun SearchAndFilterBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilter: RiskFilter,
    onFilterSelected: (RiskFilter) -> Unit,
    totalCount: Int,
    highCount: Int,
    moderateCount: Int,
    lowCount: Int,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Search Input Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(SurfaceContainerLowest),
            placeholder = {
                Text(
                    text = "Search teen name, PCOS signs...",
                    color = OnSurfaceVariant.copy(alpha = 0.7f),
                    fontSize = 13.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = OnSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.size(20.dp)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear",
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = SurfaceContainerLowest,
                unfocusedContainerColor = SurfaceContainerLowest,
                focusedBorderColor = Primary,
                unfocusedBorderColor = OutlineVariant,
                focusedTextColor = OnSurface,
                unfocusedTextColor = OnSurface
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() })
        )

        // Filter Chips Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChipItem(
                label = "All Teens ($totalCount)",
                isSelected = selectedFilter == RiskFilter.ALL,
                selectedBg = Primary,
                selectedText = OnPrimary,
                unselectedBorder = OutlineVariant,
                unselectedText = OnSurfaceVariant,
                onClick = { onFilterSelected(RiskFilter.ALL) }
            )

            FilterChipItem(
                label = "Needs Care ($highCount)",
                isSelected = selectedFilter == RiskFilter.HIGH,
                selectedBg = Tertiary,
                selectedText = OnPrimary,
                unselectedBorder = TertiaryFixed,
                unselectedText = Tertiary,
                onClick = { onFilterSelected(RiskFilter.HIGH) }
            )

            FilterChipItem(
                label = "Monitoring ($moderateCount)",
                isSelected = selectedFilter == RiskFilter.MODERATE,
                selectedBg = PrimaryContainer,
                selectedText = OnPrimary,
                unselectedBorder = PrimaryFixed,
                unselectedText = Primary,
                onClick = { onFilterSelected(RiskFilter.MODERATE) }
            )

            FilterChipItem(
                label = "Thriving ($lowCount)",
                isSelected = selectedFilter == RiskFilter.LOW,
                selectedBg = Secondary,
                selectedText = OnPrimary,
                unselectedBorder = SecondaryFixed,
                unselectedText = Secondary,
                onClick = { onFilterSelected(RiskFilter.LOW) }
            )
        }
    }
}

@Composable
private fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    selectedBg: Color,
    selectedText: Color,
    unselectedBorder: Color,
    unselectedText: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(if (isSelected) selectedBg else SurfaceContainerLowest)
            .border(
                1.dp,
                if (isSelected) Color.Transparent else unselectedBorder,
                RoundedCornerShape(999.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) selectedText else unselectedText
        )
    }
}
