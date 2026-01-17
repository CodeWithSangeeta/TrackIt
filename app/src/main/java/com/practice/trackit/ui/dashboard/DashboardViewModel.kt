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
        val user = FirebaseAuth.getInstance().currentUser ?: return

        viewModelScope.launch {
            _loading.value = true
            _expenses.value = repository.getExpenses(user.uid)
            _loading.value = false
        }
    }


    fun addExpense(
        amount: Double,
        category: String,
        note: String,
        date: String,
        type : String,
        onSuccess: () -> Unit
    ) {
        println("👉 addExpense() CALLED")

        val user = FirebaseAuth.getInstance().currentUser
        println("👉 CURRENT USER = $user")

        if (user == null) {
            println("❌ USER IS NULL — EXITING addExpense()")
            return
        }

        val expense = Expense(
            title = note.ifEmpty { category },
            category = category,
            amount = amount,
            type = type,
            date = date,
            userId = user.uid
        )

        viewModelScope.launch {
            try {
                _loading.value = true
                repository.addExpense(expense)
                println("✅ FIRESTORE WRITE SUCCESS")
                loadExpenses()
                onSuccess()
            } catch (e: Exception) {
                println("❌ FIRESTORE ERROR = ${e.message}")
                e.printStackTrace()
            } finally {
                _loading.value = false
            }
        }
    }



}









