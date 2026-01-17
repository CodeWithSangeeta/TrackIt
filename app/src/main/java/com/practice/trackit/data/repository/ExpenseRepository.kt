package com.practice.trackit.data.repository


import com.google.firebase.firestore.FirebaseFirestore
import com.practice.trackit.data.model.Expense
import kotlinx.coroutines.tasks.await

class ExpenseRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun addExpense(expense: Expense) {
        println("👉 REPOSITORY addExpense() CALLED")
        db.collection("expenses")
            .add(expense)
            .await()
    }

    suspend fun getExpenses(userId: String): List<Expense> {
        println("👉 FETCHING expenses for userId = $userId")
        return db.collection("expenses")
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .toObjects(Expense::class.java)
    }
}

