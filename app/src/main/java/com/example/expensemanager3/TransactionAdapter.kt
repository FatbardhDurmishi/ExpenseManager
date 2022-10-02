package com.example.expensemanager3


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

//RecyclerView is used to load views to the activity when you don’t know how many views we need,
// or have a large number of individual item views to display in one activity.
// It saves memory by reusing views when you scroll in an activity
// instead of creating them all at the beginning when the activity first loads.
//RecyclerView has three main parts:
//the layout, The layout is the view that will be created for each item to be loaded into the RecyclerView.
// (ne detyren tone eshte transaction_layout),dmth e perdor qet layout si shabllon per mi kriju views per transactions
//ViewHolder,A ViewHolder is used to cache the view objects in order to save memory.
//and adapter.The adapter creates new items in the form of ViewHolders,
// populates the ViewHolders with data, and returns information about the data.
class TransactionAdapter(var transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {
    private lateinit var db: AppDatabase

    class TransactionHolder(view: View) : RecyclerView.ViewHolder(view) {

        val item: TextView = view.findViewById(R.id.item)
        val amount: TextView = view.findViewById(R.id.amount)
        val expandedMenu: RelativeLayout = view.findViewById(R.id.expanded_menu)
        val itemLayout: ConstraintLayout = view.findViewById(R.id.item_layoutt)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
    }

    //This method calls onCreateViewHolder to create a new ViewHolder and
    // initializes some private fields to be used by RecyclerView.
    // in this method we inflate the layout that will be used in the recycler viewer
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        //"Inflation" is a term that refers to parsing XML and turning it into UI-oriented data structures.
        //to create the view and viewgroup objects from the elements and
        // their attributes specified within, and then adding the hierarchy
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.transactions_layout, parent, false)
        return TransactionHolder(view)

    }


    //RecyclerView takes advantage of the fact that as you scroll and new rows come on screen also old rows disappear off screen.
    // Instead of creating new view for each new row, an old view is recycled and reused by binding new data to it.
    //This happens exactly in onBindViewHolder().
    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction = transactions[position]
        val context = holder.item.context

        if (transaction.amount > 0) {
            holder.amount.text = "+ €%.2f".format(transaction.amount)
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.green))
        } else {
            holder.amount.text = "- €%.2f".format(kotlin.math.abs(transaction.amount))
            holder.amount.setTextColor(ContextCompat.getColor(context, R.color.red))
        }

        holder.item.text = transaction.item

        db = Room.databaseBuilder(
            holder.item.context,
            AppDatabase::class.java,
            "transactions"
        ).build()

        //this shows or hides the expanded menu where edit and delete button are
        holder.expandedMenu.visibility = if (transaction.visibility) View.VISIBLE else View.GONE
        //when we click on an item we either show or hide the expanded menu
        holder.itemLayout.setOnClickListener {
            transaction.visibility = !transaction.visibility
            //Notify any registered observers that the item at position has changed.
            notifyItemChanged(position)

        }

        //button to delete a transaction
        holder.btnDelete.setOnClickListener {
            GlobalScope.launch {
                //we access the delete method in the interface through the db variable and we give the transaction as a parameter
                db.transactionDao().delete(transaction)
                //val intent= Intent(holder.item.context,HomeActivity::class.java)
                //context.startActivity(intent)
                //HomeActivity.transactions.drop(position)
                //this is a way to call a method from an activity class,we need to call this method after we delete a transaction
                //to update the dashboard
                (context as HomeActivity).fetchData()
            }
            //to notify when we change an element or smth
            notifyDataSetChanged()
        }


        //to open the UpdateTransaction Activity
        holder.btnEdit.setOnClickListener {
            val intent = Intent(context, UpdateTransactionActivity::class.java)
            //putExtra let's us send data to another activity through the intent
            //as we see it takes to parameter the name(from which we access the data in the other activity),
            //and the data itself
            intent.putExtra("transaction", transaction)
            //to start an activity from classes and activities its different(in an activity we can directly call startActivity method,
            // but from a class we have to use the context). A context is the current state of the activity or the application(in this case,
            // the current state of the app)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return transactions.size
    }

    //here we have to fill the list of the transactions from the Home Page.
    fun setData(transactions: List<Transaction>) {
        this.transactions = transactions
        notifyDataSetChanged()
    }
}
