package kg.nurtelecom.chat_engine.custom_views

import android.content.Context
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import com.design.chili.view.input.text_watchers.MaskedTextWatcher
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.databinding.ChatEngineViewChatInputFieldBinding

class ChatInputFieldView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.ChatInputFieldViewDefaultStyle,
    defStyleRes: Int = R.style.chat_engine_ChatInputFieldViewStyle
) : LinearLayout(context, attributeSet, defStyleAttr, defStyleRes) {

    private val vb: ChatEngineViewChatInputFieldBinding by lazy {
        ChatEngineViewChatInputFieldBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        obtainAttributes(context, attributeSet, defStyleAttr, defStyleRes)
        setupViews()
    }

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attributeSet, R.styleable.chat_engine_ChatInputFieldView, defStyleAttr, defStyleRes).run {
            getString(R.styleable.chat_engine_ChatInputFieldView_android_text)?.let {
                setText(it)
            }
            getString(R.styleable.chat_engine_ChatInputFieldView_android_hint)?.let {
                setHint(it)
            }
            getString(R.styleable.chat_engine_ChatInputFieldView_mask)?.let {
                setupMask(it)
            }
            getInt(R.styleable.chat_engine_ChatInputFieldView_android_inputType, InputType.TYPE_CLASS_TEXT).let {
                setupInputType(it)
            }
            getBoolean(R.styleable.chat_engine_ChatInputFieldView_android_enabled, true).let {
                setIsEnabled(it)
            }
            recycle()
        }
    }

    private fun setupViews() {
        vb.etInput.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                vb.ivSend.callOnClick()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    fun setText(text: String) {
        vb.etInput.setText(text)
    }

    fun setText(resId: Int) {
        vb.etInput.setText(resId)
    }

    fun setHint(hint: String) {
        vb.etInput.hint = hint
    }

    fun setHint(hintResId: Int) {
        vb.etInput.setHint(hintResId)
    }

    fun setupMask(mask: String, maskSymbols: List<Char>? = null) {
        val maskTextWatcher = MaskedTextWatcher.Builder()
            .setInputMask(mask)
        maskSymbols?.let { maskTextWatcher.setInputMaskSymbols(it) }
        vb.etInput.addTextChangedListener(maskTextWatcher.build(vb.etInput))
    }

    fun setupInputType(inputType: Int) {
        vb.etInput.inputType = inputType
    }

    fun setIsEnabled(isEnabled: Boolean) {
        vb.etInput.isEnabled = isEnabled
    }

    fun addTextChangedListener(textWatcher: TextWatcher) {
        vb.etInput.addTextChangedListener(textWatcher)
    }

    fun setOnSendButtonClickListener(action: (String?) -> Unit) {
        vb.ivSend.setOnClickListener { action.invoke(vb.etInput.text?.toString()) }
    }

    fun getInputText(): String? {
        return vb.etInput.text?.toString()
    }
}