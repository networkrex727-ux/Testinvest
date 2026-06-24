package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FundPlan
import com.example.data.model.Profile
import com.example.ui.components.MarbleBackground
import com.example.ui.components.StarbucksAvatar
import com.example.ui.components.StarbucksLogo
import com.example.ui.theme.*
import com.example.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToTab: (String) -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val fundPlans by viewModel.fundPlans.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
        viewModel.loadFundPlans(selectedTab)
    }

    LaunchedEffect(message) {
        message?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearMessage()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = BackgroundMint
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Section 1: Profile Banner (Height 220dp)
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        MarbleBackground(modifier = Modifier.fillMaxSize()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                StarbucksLogo(size = 160.dp)
                            }
                        }

                        // Small Starbucks avatar overlapping banner bottom
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .offset(y = 28.dp)
                        ) {
                            StarbucksAvatar(size = 56.dp)
                        }
                    }
                }

                // Section 2: User Info Card (Rounded, elevated, overlapping slightly)
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(top = 12.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "+91 | ${profile?.phone ?: "9876543210"}",
                                color = TextPrimary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                InfoColumn(
                                    label = "Balance",
                                    value = "₹${String.format(Locale.getDefault(), "%.2f", profile?.balance ?: 0.0)}"
                                )
                                InfoColumn(
                                    label = "Bonus",
                                    value = "₹${String.format(Locale.getDefault(), "%.2f", profile?.bonus ?: 0.0)}"
                                )
                                InfoColumn(
                                    label = "Recharge",
                                    value = "₹${String.format(Locale.getDefault(), "%.2f", profile?.rechargeTotal ?: 0.0)}"
                                )
                            }
                        }
                    }
                }

                // Section 3: Action Buttons Row
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val actions = listOf(
                            ActionItem("Deposit", Icons.Default.MonetizationOn) {
                                viewModel.addTestDeposit(5000.0)
                            },
                            ActionItem("Withdraw", Icons.Default.AccountBalanceWallet) {
                                viewModel.addTestWithdraw(2000.0)
                            },
                            ActionItem("Share", Icons.Default.Share) {
                                val code = profile?.referralCode ?: "SB1001"
                                val shareUrl = "https://www.starbucks-psp.com/register?ref=$code"
                                clipboardManager.setText(AnnotatedString(shareUrl))
                                scope.launch { snackbarHostState.showSnackbar("Referral link copied to clipboard!") }
                            },
                            ActionItem("Online", Icons.Default.Language) {
                                onNavigateToTab("support")
                            }
                        )

                        actions.forEach { action ->
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { action.onClick() },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .border(1.dp, AccentGreen, CircleShape)
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = action.icon,
                                        contentDescription = action.label,
                                        tint = PrimaryMediumGreen,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = action.label,
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // Section 4: Fund Tabs (Pills)
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val tabs = listOf(
                            TabItem("fixed", "Fixed Fund"),
                            TabItem("welfare", "Welfare Fund"),
                            TabItem("yearly", "Yearly Fund")
                        )

                        tabs.forEach { tab ->
                            val isSelected = selectedTab == tab.id
                            val containerCol = if (isSelected) PrimaryDarkGreen else Color.Transparent
                            val textCol = if (isSelected) Color.White else TextSecondary
                            val borderCol = if (isSelected) Color.Transparent else Color.LightGray

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(containerCol)
                                    .border(
                                        if (isSelected) 0.dp else 1.dp,
                                        borderCol,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { viewModel.loadFundPlans(tab.id) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tab.label,
                                    color = textCol,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Section 5: Fund Plans List
                if (isLoading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = AccentGreen)
                        }
                    }
                } else if (fundPlans.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No plans available",
                                color = TextSecondary,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else {
                    items(fundPlans) { plan ->
                        FundPlanCard(plan = plan) {
                            viewModel.buyPlan(plan)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun InfoColumn(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 12.dp)
    ) {
        Text(
            text = value,
            color = TextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 12.sp
        )
    }
}

@Composable
fun FundPlanCard(plan: FundPlan, onBuyClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Purple Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PurpleIcon),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Fund",
                        tint = Color.Yellow,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Name & Price info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "❄️ ${plan.name}",
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Price: ₹${String.format(Locale.getDefault(), "%.2f", plan.price)}",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }

                // Buy Now button
                Button(
                    onClick = onBuyClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryMediumGreen),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Buy Now",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = Color(0xFFF0F0F0))
            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Metrics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricColumn(label = "Revenue", value = "${plan.revenueDays} Days")
                MetricColumn(
                    label = "Daily Earnings",
                    value = "₹${String.format(Locale.getDefault(), "%.2f", plan.dailyEarnings)}"
                )
                MetricColumn(
                    label = "Total Revenue",
                    value = "₹${String.format(Locale.getDefault(), "%.2f", plan.totalRevenue)}"
                )
            }
        }
    }
}

@Composable
fun MetricColumn(label: String, value: String) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(
            text = label,
            color = TextSecondary,
            fontSize = 11.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            color = TextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

data class ActionItem(
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val onClick: () -> Unit
)

data class TabItem(
    val id: String,
    val label: String
)
