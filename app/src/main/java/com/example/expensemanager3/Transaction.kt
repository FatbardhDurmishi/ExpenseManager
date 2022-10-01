package com.example.expensemanager3

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

//we need to tell that is a table and give it a name
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val item: String,
    val amount: Double,
    val description: String,
    //we need this visibility variable to show an expanded menu in the home page.
    var visibility: Boolean = false
) : Serializable
// to use the intent.putExtra we need to serialize the data.
