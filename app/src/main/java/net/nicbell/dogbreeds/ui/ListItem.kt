package net.nicbell.dogbreeds.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import com.google.android.material.textview.MaterialTextView
import kotlinx.android.synthetic.main.list_item.view.*
import net.nicbell.dogbreeds.R


@BindingMethods(BindingMethod(type = ListItem::class, attribute = "onTitleClick", method = "onTitleClick"))
class ListItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var txtTitle: MaterialTextView
    private var txtSubtitle: MaterialTextView

    init {
        inflate(context, R.layout.list_item, this)

        txtTitle = findViewById(R.id.txtTitle)
        txtSubtitle = findViewById(R.id.txtSubtitle)


        attrs?.let {
            val attributes = context.obtainStyledAttributes(it, R.styleable.ListItem)
            attributes.getString(R.styleable.ListItem_title)?.run { title = this }
            attributes.getString(R.styleable.ListItem_subTitle)?.run { subtitle = this }

            attributes.recycle()
        }
    }

    var title: CharSequence?
        get() = txtTitle.text
        set(value) {
            txtTitle.text = value
        }

    var subtitle: CharSequence?
        get() = txtSubtitle.text
        set(value) {
            txtSubtitle.text = value
        }

    fun setOnTitleClick(listener: OnClickListener) {
        txtTitle.setOnClickListener(listener)
    }


    //Adding views
    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child is AppCompatCheckBox) {
            removeView(layFrame)
            super.addView(child, index, layFrame.layoutParams)
        } else {
            super.addView(child, index, params)
        }
    }
}