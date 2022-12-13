package kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators

import android.content.Context
import android.view.Gravity
import android.widget.LinearLayout
import com.design.chili.view.input.BaseInputView

object UnsupportedItemCreator {

    fun create(context: Context, unsupportedTitleRes: Int): BaseInputView {
        return BaseInputView(context).apply {
            disableEdition()
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(
                    0,
                    resources.getDimensionPixelSize(com.design.chili.R.dimen.padding_4dp),
                    0,
                    resources.getDimensionPixelSize(com.design.chili.R.dimen.padding_4dp)
                )
            }
            setGravity(Gravity.START)
            setHint(resources.getString(unsupportedTitleRes))
        }
    }
}