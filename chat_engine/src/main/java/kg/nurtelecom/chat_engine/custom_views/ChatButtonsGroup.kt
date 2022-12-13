package kg.nurtelecom.chat_engine.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.databinding.ChatEngineViewChatButtonsGroupBinding
import kg.nurtelecom.chat_engine.model.ButtonType
import kg.nurtelecom.chat_engine.model.ChooseType
import kg.nurtelecom.chat_engine.model.Option

class ChatButtonsGroup @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.ChatButtonsGroupDefaultStyle,
    defStyle: Int = R.style.chat_engine_ChatButtonsGroupStyle
) : LinearLayout(context, attributeSet, defStyleAttr, defStyle),
    CompoundButton.OnCheckedChangeListener {

    private val buttonsMarginPx: Int by lazy {
        resources.getDimensionPixelSize(com.design.chili.R.dimen.padding_16dp)
    }

    private val buttonsLayoutParams: LayoutParams by lazy {
        LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            setMargins(buttonsMarginPx, buttonsMarginPx, buttonsMarginPx, 0)
        }
    }

    private var buttonsContainer: LinearLayout? = null

    private var onSelectedItemChanged: ((selectedId: List<String>) -> Unit)? = null

    private var chooseType: ChooseType = ChooseType.MULTIPLE
    private var buttonType: ButtonType = ButtonType.CHECK_BOX

    private var lastCheckedId: String? = null

    val buttons = mutableListOf<Option>()

    private val vb: ChatEngineViewChatButtonsGroupBinding by lazy {
        ChatEngineViewChatButtonsGroupBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        obtainAttributes(context, attributeSet, defStyleAttr, defStyle)
    }

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int, defStyle: Int) {
        context.obtainStyledAttributes(attributeSet, R.styleable.chat_engine_ChatButtonsGroup, defStyleAttr, defStyle).run {
            getInt(R.styleable.chat_engine_ChatButtonsGroup_buttonsType, ButtonType.CHECK_BOX.ordinal).let {
                setButtonTypeInt(it)
            }
            getInt(R.styleable.chat_engine_ChatButtonsGroup_chooseType, ChooseType.MULTIPLE.ordinal).let {
                setChooseTypeInt(it)
            }
            recycle()
        }
    }

    private fun setButtonTypeInt(intType: Int) {
        val buttonType = when (intType) {
            ButtonType.CHECK_BOX.ordinal -> ButtonType.CHECK_BOX
            ButtonType.TOGGLE.ordinal -> ButtonType.TOGGLE
            ButtonType.RADIO_BUTTON.ordinal -> ButtonType.RADIO_BUTTON
            else -> throw IllegalArgumentException() // todo
        }
        setButtonType(buttonType)
    }

    fun setAllButtons(list: List<Option>) {
        this.buttons.clear()
        this.buttons.addAll(list)
    }

    fun setButtonType(buttonType: ButtonType) {
        this.buttonType = buttonType
    }

    private fun setChooseTypeInt(chooseTypeInt: Int) {
        val chooseType = when (chooseTypeInt) {
            ChooseType.MULTIPLE.ordinal -> ChooseType.MULTIPLE
            ChooseType.SINGLE.ordinal -> ChooseType.SINGLE
            else -> throw IllegalArgumentException()
        }
        setChooseType(chooseType)
    }

    fun setChooseType(chooseType: ChooseType) {
        this.chooseType = chooseType
    }


    fun renderButtons() {
        buttonsContainer?.let { vb.flRoot.removeView(it) }
        prepareContainer().let { container ->
            buttons.forEach {
                val button = getButton().apply {
                    setOnCheckedChangeListener(this@ChatButtonsGroup)
                    tag = it.id
                    id = it.hashCode()
                    text = it.label
                }
                button.isChecked = it.isSelected ?: false
                container.addView(button)
            }
            val result = validateCheckedStatesAndGetResult()
            onSelectedItemChanged?.invoke(result)
            vb.flRoot.addView(container)
        }
    }

    private fun prepareContainer(): LinearLayout {
        return buttonsContainer ?: LinearLayout(context).apply {
            orientation = VERTICAL
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            buttonsContainer = this
        }
    }

    private fun getButton(): CompoundButton {

        return when (buttonType) {
            ButtonType.CHECK_BOX -> CheckBox(context).apply {
                layoutParams = buttonsLayoutParams
            }
            ButtonType.RADIO_BUTTON -> RadioButton(context).apply {
                layoutParams = buttonsLayoutParams
            }
            ButtonType.TOGGLE -> Switch(context).apply {
                layoutDirection = View.LAYOUT_DIRECTION_RTL
                layoutParams = buttonsLayoutParams
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        val optionId = buttonView?.tag?.toString()
        val option = buttons.find { it.id == optionId }
        if (option == null || option.isSelected == isChecked) return
        if (isChecked) uncheckPreviousCheckedButton()
        option.isSelected = isChecked
        val result = validateCheckedStatesAndGetResult()
        onSelectedItemChanged?.invoke(result)
    }

    private fun uncheckPreviousCheckedButton() {
        if (chooseType == ChooseType.MULTIPLE) return
        buttons.forEach {
            it.isSelected = false
        }
    }

    private fun validateCheckedStatesAndGetResult(): List<String> {
        val selectedIds = mutableListOf<String>()
        buttons.forEach {
            buttonsContainer?.findViewWithTag<CompoundButton>(it.id)?.apply {
                if (isChecked != it.isSelected) isChecked = it.isSelected ?: false
                if (isChecked) selectedIds.add(it.id)
            }
        }
        return selectedIds
    }

    fun isChecked(): Boolean {
        return lastCheckedId != null
    }

    fun setSelectedItemChangedListener(listener: (selectedId: List<String>) -> Unit) {
        this.onSelectedItemChanged = listener
    }
}