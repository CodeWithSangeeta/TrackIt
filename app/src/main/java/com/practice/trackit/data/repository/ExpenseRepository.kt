package com.practice.trackit.data.repository


import com.google.firebase.firestore.FirebaseFirestore
import com.practice.trackit.data.model.Expense
import kotlinx.coroutines.tasks.await

class ExpenseRepository {

    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("expenses")

    // ADD
    suspend fun addExpense(expense: Expense) {
        collection.add(expense).await()
    }

    // READ
    suspend fun getExpenses(userId: String): List<Expense> {
        val snapshot = collection
            .whereEqualTo("userId", userId)
            .get()
            .await()

        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(Expense::class.java)?.copy(id = doc.id)
        }
    }

    // UPDATE
    suspend fun updateExpense(expense: Expense) {
        collection.document(expense.id).set(expense).await()
    }

    // DELETE
    suspend fun deleteExpense(expenseId: String) {
        collection.document(expenseId).delete().await()
    }
}


