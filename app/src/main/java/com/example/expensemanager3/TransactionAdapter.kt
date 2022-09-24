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


class TransactionAdapter( var transactions: List<Transaction>) :
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
    // Returns the position of the given ViewHolder in the given Adapter .
    // Returns the total number of items in the data set held by the adapter.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionHolder {
        //"Inflation" is a term that refers to parsing XML and turning it into UI-oriented data structures.
        //"Inflating" a view means taking the layout XML and parsing it to create the view and viewgroup
        // objects from the elements and their attributes specified within, and then adding the hierarchy
        // of those views and viewgroups to the parent ViewGroup
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

        if (transaction.amount > +0) {
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
        holder.btnDelete.setOnClickListener {
            GlobalScope.launch {
                db.transactionDao().delete(transaction)
                //val intent= Intent(holder.item.context,HomeActivity::class.java)
                //context.startActivity(intent)
                //HomeActivity.transactions.drop(position)
                (context as HomeActivity).fetchData()
            }
            //to notify when we change an element or smth
            notifyDataSetChanged()
        }


        //to open the UpdateTransaction Activity
        holder.btnEdit.setOnClickListener {
            val intent = Intent(context,UpdateTransactionActivity::class.java)
            //putExtra let's us send data to another activity through the intent
            //as we see it takes to parameter the name(from which we access the data in the other activity),
            //and the data itself
            intent.putExtra("transaction",transaction)
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
