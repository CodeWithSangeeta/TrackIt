package com.practice.trackit.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.practice.trackit.R
import com.practice.trackit.data.model.Expense
import com.practice.trackit.ui.auth.AuthViewModel
import com.practice.trackit.ui.navigation.AppRoutes

data class Transaction(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val type: TransactionType,
    val date: String,
    val icon: ImageVector
)


enum class TransactionType {
    INCOME, EXPENSE
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    authViewModel: AuthViewModel,
    navController : NavController,
    viewModel : DashboardViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    onTransactionClick: (Transaction) -> Unit = {},
    onAddExpenseClick: () -> Unit = {},
    onAddIncomeClick: () -> Unit = {},
) {


    val expenses by viewModel.expenses.collectAsState()
    val loading by viewModel.loading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadExpenses()
    }

    val transactions = expenses.map { expense ->
        expense.toTransaction()
    }


    val totalIncome = expenses
        .filter { it.type == "INCOME" }
        .sumOf { it.amount }

    val totalExpense = expenses
        .filter { it.type == "EXPENSE" }
        .sumOf { it.amount }

    val balance = totalIncome - totalExpense

    var showAddMenu by remember { mutableStateOf(false) }

    val showMainUi = !loading && expenses.isNotEmpty()
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (showMainUi) {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Image(
                                painter = painterResource(R.drawable.trackit_logo),
                                contentDescription = "TrackIt Logo",
                                modifier = Modifier.size(62.dp)
                            )
                            Text(
                                text = "TrackIt",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFCCFBF1))
                                .clickable { showMenu = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "U",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0A6624)
                            )
                        }

                        // Dropdown Menu
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Logout") },
                                onClick = {
                                    showMenu = false
                                    authViewModel.signOut()
                                    navController.navigate(AppRoutes.LOGIN) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }

                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        },



        floatingActionButton = {
            if (showMainUi) {
                Box {
                    FloatingActionButton(
                        onClick = { showAddMenu = true },
                        containerColor = Color(0xFF0A6624),
                        contentColor = Color.White,
                        shape = CircleShape
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = "Add",
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showAddMenu,
                        onDismissRequest = { showAddMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Add Expense") },
                            onClick = {
                                showAddMenu = false
                                onAddExpenseClick()
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Add Income") },
                            onClick = {
                                showAddMenu = false
                                onAddIncomeClick()
                            }
                        )
                    }
                }
            }
        }

    ) { paddingValues ->

        when {
            loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            expenses.isEmpty() -> {
                EmptyExpenseStateScreen(
                    onAddExpenseClick = onAddExpenseClick
                )
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)

                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Total Balance Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 1.dp
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Total Balance",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "₹${String.format("%,.0f", balance)}",
                                        fontSize = 32.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFCCFBF1)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Wallet,
                                        contentDescription = "Balance Icon",
                                        tint = Color(0xFF14B8A6),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Income and Expense Cards
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Income Card
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFD1FAE5)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.ArrowCircleDown,
                                            contentDescription = "Income",
                                            tint = Color(0xFF059669),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "Income",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF047857)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "₹${String.format("%,.0f", totalIncome)}",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF047857)
                                    )
                                }
                            }

                            // Expense Card
                            Card(
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFFEE2E2)
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(20.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.ArrowCircleUp,
                                            contentDescription = "Expense",
                                            tint = Color(0xFFDC2626),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "Expense",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFFDC2626)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "₹${String.format("%,.0f", totalExpense)}",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFDC2626)
                                    )
                                }
                            }

                        }
                    }

                    // Recent Transactions Header
                    item {
                        Text(
                            text = "Recent Transactions",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }

                    // Transaction List
                    items(transactions, key = { it.id }) { transaction: Transaction ->
                        TransactionItem(
                            transaction = transaction,
                            onClick = { onTransactionClick(transaction) }
                        )
                    }

                    // Bottom Spacing for FAB
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            if (transaction.type == TransactionType.INCOME)
                                Color(0xFFD1FAE5)
                            else
                                 Color(0xFFFEE2E2)

                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = transaction.icon,
                        contentDescription = transaction.category,
                        tint = if (transaction.type == TransactionType.INCOME)
                            Color(0xFF059669)
                        else

                        Color(0xFFDC2626)
                    )
                }

                // Title and Category
                Column {
                    Text(
                        text = transaction.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = transaction.category,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Amount and Date
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${if (transaction.type == TransactionType.INCOME) "+" else "-"}₹${String.format("%,.0f", transaction.amount)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (transaction.type == TransactionType.INCOME)
                        Color(0xFF059669)
                    else
                        Color(0xFFDC2626)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = transaction.date,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


private fun Expense.toTransaction(): Transaction {
    return Transaction(
        id = id,
        title = title,
        category = category,
        amount = amount,
        type = if (type == "INCOME")
            TransactionType.INCOME
        else
            TransactionType.EXPENSE,
        date = date,
        icon = if (type == "INCOME")
            Icons.Outlined.TrendingUp
        else
            Icons.Outlined.ShoppingBag
    )
}