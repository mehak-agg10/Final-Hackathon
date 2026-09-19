package com.bloomteen.pcosassessor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Diversity1
import androidx.compose.material.icons.filled.FactCheck
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bloomteen.pcosassessor.ui.theme.*

@Composable
fun BottomNavBar(
    activeTab: String,
    onNavigateToSelfCheck: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White.copy(alpha = 0.98f))
            .border(1.dp, PrimaryFixed.copy(alpha = 0.6f), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .padding(vertical = 8.dp, horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Self-Check Tab
            val isSelfCheck = activeTab == "self_check"
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(if (isSelfCheck) PrimaryFixed.copy(alpha = 0.8f) else Color.Transparent)
                    .clickable(onClick = onNavigateToSelfCheck)
                    .padding(horizontal = 24.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.FactCheck,
                    contentDescription = "Self-Check",
                    tint = if (isSelfCheck) Primary else OnSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "Self-Check",
                    fontSize = 11.sp,
                    fontWeight = if (isSelfCheck) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelfCheck) Primary else OnSurfaceVariant
                )
            }

            // Community (Dashboard) Tab
            val isCommunity = activeTab == "dashboard"
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(999.dp))
                    .background(if (isCommunity) PrimaryFixed.copy(alpha = 0.8f) else Color.Transparent)
                    .clickable(onClick = onNavigateToDashboard)
                    .padding(horizontal = 24.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Diversity1,
                    contentDescription = "Community",
                    tint = if (isCommunity) Primary else OnSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
                Text(
                    text = "Community",
                    fontSize = 11.sp,
                    fontWeight = if (isCommunity) FontWeight.Bold else FontWeight.Medium,
                    color = if (isCommunity) Primary else OnSurfaceVariant
                )
            }
        }
    }
}
