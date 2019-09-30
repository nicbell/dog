package net.nicbell.dogbreeds.adapters

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.squareup.picasso.Picasso
import net.nicbell.dogbreeds.BR

/**
 * Binding helpers
 */
@BindingAdapter("entries", "layout")
fun <T> setEntries(viewGroup: LinearLayout, entries: List<T>?, layoutId: Int) {
    viewGroup.removeAllViews()

    if (entries != null) {
        val inflater = viewGroup.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        for (i in entries.indices) {
            val entry = entries[i]
            val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, viewGroup, true)

            binding.setVariable(BR.data, entry)
        }
    }
}

@BindingAdapter("entries", "handlers", "layout")
fun <T, H> setEntries(viewGroup: LinearLayout, entries: List<T>?, handlers: H, layoutId: Int) {
    viewGroup.removeAllViews()

    if (entries != null) {
        val inflater = viewGroup.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        for (i in entries.indices) {
            val entry = entries[i]
            val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, layoutId, viewGroup, true)

            binding.setVariable(BR.data, entry)
            binding.setVariable(BR.handlers, handlers)
        }
    }
}

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, oldImageUrl: String?, newImageUrl: String?) {
    if (newImageUrl != null && !TextUtils.equals(oldImageUrl, newImageUrl) && !TextUtils.isEmpty(newImageUrl)) {
        val picasso = Picasso.get()
        //picasso.setIndicatorsEnabled(true)

        picasso.load(newImageUrl)
            .into(view)
    } else {
        view.setImageURI(null)
    }
}