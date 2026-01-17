package com.practice.trackit.ui.expense


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.practice.trackit.ui.dashboard.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.*

data class Category(
    val id: String,
    val name: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    type: String = "EXPENSE",
    onBackClick: () -> Unit = {},
    onSaveSuccess: () -> Unit = {},
    viewModel: DashboardViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
) {
    var amount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var note by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())) }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val categories = listOf(
        Category("food", "Food & Dining"),
        Category("rent", "Rent & Housing"),
        Category("travel", "Travel & Transport"),
        Category("shopping", "Shopping"),
        Category("entertainment", "Entertainment"),
        Category("utilities", "Utilities"),
        Category("healthcare", "Healthcare"),
        Category("other", "Other")
    )

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (type == "INCOME") "Add Income" else "Add Expense",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F2937)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF374151)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FAFB))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Amount Field
            Column {
                Text(
                    text = "Amount (₹)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = amount,
                    onValueChange = { newValue ->
                        // Only allow numbers and decimal point
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                            amount = newValue
                        }
                    },
                    placeholder = {
                        Text(
                            text = "0.00",
                            color = Color(0xFFD1D5DB),
                            fontSize = 18.sp
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE5E7EB),
                        focusedBorderColor = Color(0xFF14B8A6),
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 18.sp)
                )
            }

            // Category Dropdown
            Column {
                Text(
                    text = "Category",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = showCategoryDropdown,
                    onExpandedChange = { showCategoryDropdown = it }
                ) {
                    OutlinedTextField(
                        value = selectedCategory?.name ?: "",
                        onValueChange = {},
                        readOnly = true,
                        placeholder = {
                            Text(
                                text = "Select category",
                                color = Color(0xFF9CA3AF)
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowDown,
                                contentDescription = "Dropdown",
                                tint = Color(0xFF6B7280)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE5E7EB),
                            focusedBorderColor = Color(0xFF14B8A6),
                            unfocusedContainerColor = Color.White,
                            focusedContainerColor = Color.White
                        ),
                        singleLine = true
                    )

                    ExposedDropdownMenu(
                        expanded = showCategoryDropdown,
                        onDismissRequest = { showCategoryDropdown = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = category.name,
                                        fontSize = 14.sp,
                                        color = Color(0xFF374151)
                                    )
                                },
                                onClick = {
                                    selectedCategory = category
                                    showCategoryDropdown = false
                                }
                            )
                        }
                    }
                }
            }

            // Note Field
            Column {
                Text(
                    text = "Note (Optional)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = {
                        Text(
                            text = "Add a note...",
                            color = Color(0xFFD1D5DB)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE5E7EB),
                        focusedBorderColor = Color(0xFF14B8A6),
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true
                )
            }

            // Date Field
            Column {
                Text(
                    text = "Date",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = date,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.CalendarToday,
                            contentDescription = "Calendar",
                            tint = Color(0xFF6B7280),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDatePicker = true },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE5E7EB),
                        focusedBorderColor = Color(0xFF14B8A6),
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        disabledBorderColor = Color(0xFFE5E7EB),
                        disabledContainerColor = Color.White,
                        disabledTextColor = Color(0xFF374151)
                    ),
                    enabled = false,
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            println("Saving expense to Firestore")

            // Save Button
            Button(
                onClick = {
                    println("👉 SAVE BUTTON CLICKED")
                    val amountValue = amount.toDoubleOrNull()
                    if (amountValue == null || amountValue <= 0) {
                        println("❌ INVALID AMOUNT")
                        return@Button
                    }

                    if (selectedCategory == null) {
                        println("❌ CATEGORY NOT SELECTED")
                        return@Button
                    }
                    println("👉 CALLING viewModel.addExpense()")
                        viewModel.addExpense(
                            amount = amountValue,
                            category = selectedCategory!!.name,
                            note = note,
                            date = date,
                            type = type,
                            onSuccess = onSaveSuccess
                        )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF14B8A6),
                    disabledContainerColor = Color(0xFFD1D5DB)
                ),
                enabled = amount.isNotEmpty() && amount.toDoubleOrNull() != null &&
                        selectedCategory != null,
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 4.dp,
                    disabledElevation = 0.dp
                )
            ) {
                Text(
                    text = "Save Expense",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }



    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                            date = sdf.format(Date(millis))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK", color = Color(0xFF14B8A6))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = Color(0xFF6B7280))
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    selectedDayContainerColor = Color(0xFF14B8A6),
                    todayContentColor = Color(0xFF14B8A6),
                    todayDateBorderColor = Color(0xFF14B8A6)
                )
            )
        }
    }
}

