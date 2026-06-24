package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrimaryDarkGreen

@Composable
fun StarbucksLogo(
    size: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(PrimaryDarkGreen),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.92f)
                .clip(CircleShape)
                .background(Color.Transparent)
                .drawBehind {
                    drawCircle(
                        color = Color.White,
                        radius = this.size.minDimension / 2f,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 4f)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.2f)
                )
                Text(
                    text = "STARBUCKS",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = (size.value * 0.12f).sp,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "PSP",
                    color = Color(0xFF52B788),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = (size.value * 0.1f).sp,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

@Composable
fun StarbucksAvatar(
    size: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(Color.White)
            .padding(2.dp)
            .clip(CircleShape)
            .background(PrimaryDarkGreen),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(size * 0.4f)
        )
    }
}

@Composable
fun MarbleBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE5E9E5),
                        Color(0xFFFAF9F6)
                    )
                )
            )
            .drawBehind {
                val width = size.width
                val height = size.height
                val veinBrush = Color(0x0C000000)

                // Vein lines
                drawLine(
                    color = veinBrush,
                    start = Offset(width * 0.1f, 0f),
                    end = Offset(width * 0.45f, height * 0.35f),
                    strokeWidth = 3f
                )
                drawLine(
                    color = veinBrush,
                    start = Offset(width * 0.45f, height * 0.35f),
                    end = Offset(width * 0.32f, height * 0.65f),
                    strokeWidth = 2f
                )
                drawLine(
                    color = veinBrush,
                    start = Offset(width * 0.32f, height * 0.65f),
                    end = Offset(width * 0.75f, height * 1.0f),
                    strokeWidth = 3f
                )

                drawLine(
                    color = veinBrush,
                    start = Offset(width * 0.85f, 0f),
                    end = Offset(width * 0.62f, height * 0.45f),
                    strokeWidth = 2f
                )
                drawLine(
                    color = veinBrush,
                    start = Offset(width * 0.62f, height * 0.45f),
                    end = Offset(width * 0.95f, height * 0.85f),
                    strokeWidth = 4f
                )
            }
    ) {
        content()
    }
}
