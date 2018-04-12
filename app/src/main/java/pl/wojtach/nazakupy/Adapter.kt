package pl.wojtach.nazakupy

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.shopping_list_header.view.*

internal class ShoppingListHeaderAdapter
    : ListAdapter<ShoppingListHeader, ShoppingListHeaderViewHolder>(object : DiffUtil.ItemCallback<ShoppingListHeader>() {
    override fun areItemsTheSame(oldItem: ShoppingListHeader, newItem: ShoppingListHeader): Boolean =
            oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: ShoppingListHeader, newItem: ShoppingListHeader): Boolean =
            oldItem == newItem

}) {

    var items: List<ShoppingListHeader> = emptyList()
        set(value) {
            field = ArrayList(value.asReversed())
            submitList(field)
            Log.d("Adapter", "current items: $field")

        }

    var onRemoveListener: (ShoppingListHeader) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListHeaderViewHolder =
            LayoutInflater.from(parent.context).inflate(R.layout.shopping_list_header, parent, false)
                    .let { ShoppingListHeaderViewHolder(it, onRemoveListener) }


    override fun onBindViewHolder(holder: ShoppingListHeaderViewHolder, position: Int) {
        holder.onBind(items[position])
    }
}

internal class ShoppingListHeaderViewHolder(val view: View, val onRemoveListener: (ShoppingListHeader) -> Unit) : RecyclerView.ViewHolder(view) {

    private val dateView = view.shopping_list_header_date
    private val nameView = view.shopping_list_header_name

    fun onBind(data: ShoppingListHeader) {
        dateView.text = data.formattedDate
        nameView.text = data.name
        view.setOnClickListener { onRemoveListener(data) }
    }
}