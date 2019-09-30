package net.nicbell.dogbreeds.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

/**
 * Generic list adapter
 */
open class ListAdapter<T>(private val layoutId: Int, private val itemBinder: ItemBinder<T>) :
    RecyclerView.Adapter<ListAdapter.ViewHolder<T>>() {
    private val data: MutableList<T>

    init {
        data = ArrayList()
    }

    fun update(newData: List<T>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, viewType, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        val item = data[position]
        holder.bind(item, itemBinder)
    }

    override fun getItemViewType(position: Int): Int {
        return layoutId
    }

    override fun getItemCount(): Int {
        return data.size
    }


    /**
     * View holder.
     */
    class ViewHolder<T>(private val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: T, itemBinder: ItemBinder<T>) {
            itemBinder.bindItem(binding, item)
            binding.executePendingBindings()
        }
    }

    interface ItemBinder<T> {
        fun bindItem(binding: ViewDataBinding, item: T)
    }
}