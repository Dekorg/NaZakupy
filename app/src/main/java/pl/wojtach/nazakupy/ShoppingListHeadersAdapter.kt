package pl.wojtach.nazakupy

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.shopping_list_header.*
import pl.wojtach.nazakupy.ShoppingListHeadersAdapter.ShoppingListHeaderViewHolder

internal class ShoppingListHeadersAdapter
    : ListAdapter<ShoppingListHeader, ShoppingListHeaderViewHolder>(HeadersComparator()) {

    var items: List<ShoppingListHeader> = emptyList()
        set(value) {
            field = value.reversed()
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

    class ShoppingListHeaderViewHolder(
            override val containerView: View,
            private val onRemoveListener: (ShoppingListHeader) -> Unit
    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun onBind(data: ShoppingListHeader) {
            shopping_list_header_date.text = data.formattedDate
            shopping_list_header_name.text = data.name
            shopping_list_header_remove.setOnClickListener { onRemoveListener(data) }
        }
    }

    private class HeadersComparator : DiffUtil.ItemCallback<ShoppingListHeader>() {

        override fun areItemsTheSame(oldItem: ShoppingListHeader, newItem: ShoppingListHeader): Boolean =
                oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ShoppingListHeader, newItem: ShoppingListHeader): Boolean =
                oldItem == newItem
    }
}