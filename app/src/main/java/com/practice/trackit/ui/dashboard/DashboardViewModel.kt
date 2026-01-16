package com.practice.trackit.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.practice.trackit.data.model.Expense
import com.practice.trackit.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val repository = ExpenseRepository()

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadExpenses() {
        viewModelScope.launch {
            _loading.value = true
            _expenses.value = repository.getExpenses()
            _loading.value = false
        }
    }


    fun addExpense(
        amount: Double,
        category: String,
        note: String,
        date: String,
        onSuccess: () -> Unit
    ) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val expense = Expense(
            title = note.ifEmpty { category },
            category = category,
            amount = amount,
            type = "EXPENSE",
            date = date,
            userId = userId
        )

        viewModelScope.launch {
            _loading.value = true
            repository.addExpense(expense)
            loadExpenses()   // refresh dashboard data
            _loading.value = false
            onSuccess()
        }
    }


}









