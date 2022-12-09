package kg.nurtelecom.chat_engine.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import kg.nurtelecom.chat_engine.R

class DropDownInputField @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null) : LinearLayout(context, attributeSet) {

    lateinit var views: DropDownInputFiledData

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.chat_engine_form_item_drop_down, this, true)
        views = DropDownInputFiledData(
            view.findViewById(R.id.tv_label)
        )

    }

    fun setHint(hint: String) {
        views.tvTitle.apply {
            text = hint
            setTextColor(getColor(context, com.design.chili.R.color.gray_1_alpha_50))
        }
    }

    fun setText(text: String) {
        views.tvTitle.apply {
            this.text = text
            setTextColor(getColor(context, com.design.chili.R.color.black_5))
        }
    }
}

data class DropDownInputFiledData(
    val tvTitle: TextView
)