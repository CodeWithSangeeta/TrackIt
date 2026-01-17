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
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditExpenseScreen(
    expenseId: Int = 1,
    initialAmount: String = "2500",
    initialCategory: String = "Shopping",
    initialNote: String = "Weekly groceries",
    initialDate: String = "15-01-2026",
    onBackClick: () -> Unit = {},
    onUpdateClick: (amount: Double, category: String, note: String, date: String) -> Unit = { _, _, _, _ -> },
    onDeleteClick: (expenseId: Int) -> Unit = {}
) {
    var amount by remember { mutableStateOf(initialAmount) }
    var selectedCategory by remember { mutableStateOf<Category?>(Category("shopping", initialCategory)) }
    var note by remember { mutableStateOf(initialNote) }
    var date by remember { mutableStateOf(initialDate) }
    var showCategoryDropdown by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

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

    // Parse the initial date for the date picker
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val initialDateMillis = try {
        dateFormat.parse(initialDate)?.time ?: System.currentTimeMillis()
    } catch (e: Exception) {
        System.currentTimeMillis()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Expense",
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
                        if (newValue.isEmpty() || newValue.matches(Regex("^\\d*\\.?\\d*$"))) {
                            amount = newValue
                        }
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
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
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
                    text = "Note",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE5E7EB),
                        focusedBorderColor = Color(0xFF14B8A6),
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White
                    ),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
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
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Update Button
            Button(
                onClick = {
                    val amountValue = amount.toDoubleOrNull() ?: 0.0
                    if (amountValue > 0 && selectedCategory != null) {
                        onUpdateClick(amountValue, selectedCategory!!.name, note, date)
                    }
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
                    text = "Update Expense",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            // Delete Button
            OutlinedButton(
                onClick = { showDeleteDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color(0xFFFEF2F2),
                    contentColor = Color(0xFFDC2626)
                ),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFFFECACA))
                )
            ) {
                Text(
                    text = "Delete Expense",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFFDC2626)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
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

    // Delete Confirmation Dialog
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(
                    text = "Delete Expense",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1F2937)
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete this expense? This action cannot be undone.",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDeleteClick(expenseId)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFFDC2626)
                    )
                ) {
                    Text(
                        text = "Delete",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF6B7280)
                    )
                ) {
                    Text("Cancel")
                }
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(16.dp)
        )
    }
}
