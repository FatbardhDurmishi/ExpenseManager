package com.example.expensemanager3

import androidx.room.*

//Data Access Objects are the main classes where you define your database interactions.
// They can include a variety of query methods.
// The class marked with @Dao should either be an interface or an abstract class.
// At compile time, Room will generate an implementation of this class when it is referenced by a Database
//we define here the function for the database
//functions can also be written as queries, but as methods ma easy
//these @Insert, @Delete, @Update tells the function what to do
@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions")
    fun getAll():List<Transaction>

    ///Marks a method in a Dao annotated class as an insert method.
    @Insert
    fun insertAll(vararg transaction: Transaction)

    //Marks a method in a Dao annotated class as a delete method.
    @Delete
    fun delete(transaction: Transaction)

    //Marks a method in a Dao annotated class as an update method.
    @Update
    fun update(vararg transaction: Transaction)
}