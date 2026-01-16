package com.practice.trackit.data.model


data class Expense(
    val title: String = "",
    val category: String = "",
    val amount: Double = 0.0,
    val type: String = "",
    val date: String = "",
    val userId: String = ""
)
