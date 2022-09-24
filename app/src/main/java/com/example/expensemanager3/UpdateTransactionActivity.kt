package com.example.expensemanager3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.room.Room
import com.example.expensemanager3.databinding.ActivityUpdateTransactionBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UpdateTransactionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateTransactionBinding
    private lateinit var transactionFromIntent:Transaction
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactionFromIntent = intent.getSerializableExtra("transaction") as Transaction

        binding.itemInput.setText(transactionFromIntent.item)
        binding.amountInput.setText(transactionFromIntent.amount.toString())
        binding.descriptionInput.setText(transactionFromIntent.description)

        binding.updateBackBtn.setOnClickListener {
            finish()
        }

        binding.btnSave.setOnClickListener {
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
                val transaction = Transaction(transactionFromIntent.id, item, amount.toDouble(), description)
                update(transaction)
            }
        }
    }
    private fun update(transaction: Transaction) {
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "transactions"
        ).build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
    }
}





