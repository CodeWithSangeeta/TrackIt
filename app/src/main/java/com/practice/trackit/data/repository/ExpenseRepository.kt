package com.practice.trackit.data.repository


import com.google.firebase.firestore.FirebaseFirestore
import com.practice.trackit.data.model.Expense
import kotlinx.coroutines.tasks.await

class ExpenseRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun getExpenses(): List<Expense> {
        return db.collection("expenses")
            .whereEqualTo("userId", "demo_user")
            .get()
            .await()
            .toObjects(Expense::class.java)
    }

    // ✅ ADD THIS FUNCTION
    suspend fun addExpense(expense: Expense) {
        db.collection("expenses")
            .add(expense)
            .await()
    }
}
