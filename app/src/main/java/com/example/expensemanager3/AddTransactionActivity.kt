package com.example.expensemanager3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.expensemanager3.databinding.ActivityAddTransactionBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddTransactionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.addBudgetBtn.setOnClickListener {
            val item = binding.itemInput.text.toString()
            val amount = binding.amountInput.text.toString()
            val description = binding.descriptionInput.text.toString()

            if (item.isEmpty()) {
                binding.itemInput.error = "Please enter the item"
            }
            if (amount.isEmpty()) {
                binding.amountInput.error = "Please enter the amount"
            }

            if (item.isNotEmpty() && amount.isNotEmpty()) {
                val transaction = Transaction(0, item, amount.toDouble(), description)
                insert(transaction)
            }
        }
        binding.spendBtn.setOnClickListener {
            val item = binding.itemInput.text.toString()
            val amount = binding.amountInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            if (item.isEmpty()) {
                binding.itemInput.error = "Please enter the item"
            }
            if (amount.isEmpty()) {
                binding.amountInput.error = "Please enter the amount"
            }

            if (item.isNotEmpty() && amount.isNotEmpty()) {
                val transaction = Transaction(0, item, -amount.toDouble(), description)
                insert(transaction)
            }
        }
    }

    private fun insert(transaction: Transaction) {
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "transactions"
        ).build()

        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }
}