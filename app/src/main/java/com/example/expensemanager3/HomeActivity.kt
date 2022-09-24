package com.example.expensemanager3


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.expensemanager3.databinding.ActivityHomeBinding
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*


class HomeActivity : AppCompatActivity() {
    private lateinit var transactions: List<Transaction>
    // idea about data binding is to create an object that connects/maps/binds
    // two pieces of distant information together at compile time,
    // so that you don't have to look for it at runtime.
    private lateinit var binding: ActivityHomeBinding
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        transactions = listOf()
        db = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "transactions"
        ).build()

        // initialize transactionAdapter, it takes a parameter of transactions as a List
        transactionAdapter = TransactionAdapter(transactions)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = transactionAdapter

        //onClickListener to addTransaction button to add new transactions
        binding.addTransaction.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }


    }

    fun fetchData() {
        //all the code in this brackets will be called on the background thread,
        //calling them on the main thread can cause bad user experience
        GlobalScope.launch {
            transactions = db.transactionDao().getAll()
            //we update the recycle view and dashboard,but these 2 functions
            //interact with the UI thread and we change the thread to UIThread

            runOnUiThread {
                transactionAdapter.setData(transactions)
                updateDashboard()

            }
        }
    }

    // on this function we just update some stuff on the Home Page like , the balance,
    // the amount that is spent, and the list of transactions that have been added
    fun updateDashboard() {
        val balance = transactions.sumOf { it.amount }
        if (balance < 0) {
            binding.balanceAmount.setTextColor(ContextCompat.getColor(this, R.color.red))
            binding.balanceAmount.error = "You have exceeded the balance!"
            Toast.makeText(this, "You have exceeded the balance", Toast.LENGTH_SHORT).show()
        } else {
            binding.balanceAmount.setTextColor(ContextCompat.getColor(this, R.color.green))
            binding.balanceAmount.error = null
        }
        val expenses = transactions.filter { it.amount < 0 }.sumOf { it.amount }

        binding.balanceAmount.text = " €%.2f".format(balance)
        binding.expenseAmount.text = "- €%.2f".format(kotlin.math.abs(expenses))
    }


    // everytime we get back to the Home page we have to update the dashboard,
    // because probably there has been added a transaction or updated so we call the function fetchData
    override fun onResume() {
        super.onResume()
        fetchData()
    }
}



