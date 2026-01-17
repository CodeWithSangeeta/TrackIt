package com.practice.trackit.data.model


data class Expense(
    val id: String = "",
    val title: String = "",
    val category: String = "",
    val amount: Double = 0.0,
    val type: String = "EXPENSE",
    val date: String = "",
    val userId: String = ""
)
