package com.bloomteen.pcosassessor.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun SummaryBentoGrid(
    highCount: Int,
    moderateCount: Int,
    lowCount: Int,
    modifier: Modifier = Modifier
) {
    val total = (highCount + moderateCount + lowCount).coerceAtLeast(1)
    val highRatio = highCount.toFloat() / total
    val modRatio = moderateCount.toFloat() / total
    val lowRatio = lowCount.toFloat() / total

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // High Risk Bento Card
        BentoItem(
            modifier = Modifier.weight(1f),
            title = "HIGH RISK",
            count = highCount,
            subtitle = "PCOS / PCOD ≥4/6",
            dotColor = Tertiary,
            barColor = Tertiary,
            trackColor = TertiaryFixed,
            progress = highRatio,
            borderColor = TertiaryFixed
        )

        // Moderate Bento Card
        BentoItem(
            modifier = Modifier.weight(1f),
            title = "MODERATE",
            count = moderateCount,
            subtitle = "Monitoring 2-3/6",
            dotColor = PrimaryContainer,
            barColor = PrimaryContainer,
            trackColor = PrimaryFixed,
            progress = modRatio,
            borderColor = PrimaryFixed
        )

        // Low / Mild Bento Card
        BentoItem(
            modifier = Modifier.weight(1f),
            title = "LOW / MILD",
            count = lowCount,
            subtitle = "Balanced 0-1/6",
            dotColor = Secondary,
            barColor = Secondary,
            trackColor = SecondaryFixed,
            progress = lowRatio,
            borderColor = SecondaryFixed
        )
    }
}

@Composable
private fun BentoItem(
    title: String,
    count: Int,
    subtitle: String,
    dotColor: Color,
    barColor: Color,
    trackColor: Color,
    progress: Float,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceContainerLowest)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header: Title + Indicator Dot
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = dotColor,
                    letterSpacing = 0.5.sp
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(dotColor)
                )
            }

            // Big Count & Subtitle
            Column {
                Text(
                    text = count.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnSurface,
                    lineHeight = 26.sp
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = dotColor
                )
            }

            // Micro progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(trackColor)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress.coerceIn(0.08f, 1f))
                        .clip(RoundedCornerShape(999.dp))
                        .background(barColor)
                )
            }
        }
    }
}
