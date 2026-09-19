package com.bloomteen.pcosassessor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.bloomteen.pcosassessor.ui.theme.*

@Composable
fun ProtocolCard(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFFFFF0F5),
                        Color(0xFFF7F2FE),
                        Color(0xFFEFF6FF)
                    )
                )
            )
            .border(1.dp, Color(0xFFFBCFE8).copy(alpha = 0.7f), RoundedCornerShape(16.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Ribbon Icon Box
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(SurfaceContainerLowest)
                .border(1.dp, SecondaryFixed, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🎗️", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Adolescent PCOS/PCOD Diagnostic Protocol",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface
                )
                Text(
                    text = "Rotterdam Consensus",
                    color = Secondary,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(SecondaryFixed)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }

            Text(
                text = "Diagnosis in adolescents requires persistent ovulatory dysfunction (>2 yrs post-menarche) AND clinical or biochemical hyperandrogenism. Gentle, supportive communication prevents distress.",
                fontSize = 11.sp,
                color = OnSurfaceVariant,
                lineHeight = 15.sp
            )
        }
    }
}
