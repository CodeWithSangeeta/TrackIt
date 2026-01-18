package com.practice.trackit.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.practice.trackit.data.model.Expense
import com.practice.trackit.data.repository.ExpenseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

    private val repository = ExpenseRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading


    // READ
    fun loadExpenses() {
        val user = auth.currentUser ?: return
        viewModelScope.launch {
            _loading.value = true
            _expenses.value = repository.getExpenses(user.uid)
            _loading.value = false
        }
    }

    // ADD
    fun addExpense(
        amount: Double,
        category: String,
        note: String,
        date: String,
        type: String,
        onSuccess: () -> Unit
    ) {
        val user = auth.currentUser ?: return

        val expense = Expense(
            title = note.ifEmpty { category },
            category = category,
            amount = amount,
            type = type,
            date = date,
            userId = user.uid
        )

        viewModelScope.launch {
            repository.addExpense(expense)
            loadExpenses()
            onSuccess()
        }
    }

    // UPDATE
    fun updateExpense(expense: Expense, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.updateExpense(expense)
            loadExpenses()
            onSuccess()
        }
    }

    // DELETE

    fun deleteExpense(id: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteExpense(id)
            loadExpenses()
            onSuccess()
        }
    }
}


