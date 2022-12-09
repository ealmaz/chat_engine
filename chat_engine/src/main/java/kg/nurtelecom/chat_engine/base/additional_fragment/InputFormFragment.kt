package kg.nurtelecom.chat_engine.base.additional_fragment

import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.commit
import androidx.lifecycle.LifecycleOwner
import com.design.chili.view.input.MaskedInputView
import com.design.chili.view.modals.bottom_sheet.serach_bottom_sheet.Option
import com.design.chili.view.modals.bottom_sheet.serach_bottom_sheet.SearchSelectorBottomSheet
import com.design.chili.view.navigation_components.ChiliToolbar
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.custom_views.ChatButtonsGroup
import kg.nurtelecom.chat_engine.databinding.ChatEngineFragmentInputFormBinding
import kg.nurtelecom.chat_engine.extensions.closeCurrentFragment
import kg.nurtelecom.chat_engine.extensions.hideKeyboard
import kg.nurtelecom.chat_engine.model.*

open class InputFormFragment : Fragment() {

    open val unsupportedTitle = "Неподдерживаемое поле"
    open val buttonText = "Далее"

    private var _vb: ChatEngineFragmentInputFormBinding? = null
    private val vb: ChatEngineFragmentInputFormBinding
        get() = _vb!!

    private val inputForm: InputForm? by lazy {
        arguments?.getSerializable(INPUT_FORM_ARGUMENT) as? InputForm
    }

    private val result = HashMap<String, List<String>?>()

    private val toolbarConfig: ChiliToolbar.Configuration by lazy {
        ChiliToolbar.Configuration(
            hostActivity = requireActivity(),
            title = inputForm?.title,
            centeredTitle = true,
            navigationIconRes = R.drawable.chat_engine_ic_close,
            onNavigateUpClick = { closeCurrentFragment() },
            isNavigateUpButtonEnabled = true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View {
        _vb = ChatEngineFragmentInputFormBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupInputForm()
    }

    private fun setupViews() = with(vb) {
        tb.initToolbar(toolbarConfig)
        btnDone.apply {
            setOnClickListener { setFragmentResultAndClose() }
            text = buttonText
        }
    }

    private fun setupInputForm() {
        val container = LinearLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            orientation = LinearLayout.VERTICAL
        }
        inputForm?.formItems?.forEach {
            val view = when (it.formItem) {
                is InputField -> createInputField(it.formItem)
                is GroupButtonFormItem -> createButtonGroup(it.formItem)
                is DropDownFieldInfo -> createDropDownField(it.formItem)
                else -> createUnsupportedItem(it)
            }
            container.addView(view)
        }
        vb.formContainer.addView(container)
    }

    protected open fun createUnsupportedItem(formItem: FormItem): View {
        return MaskedInputView(requireContext()).apply {
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
            setHint(unsupportedTitle)
        }
    }

    private fun createInputField(inputField: InputField): MaskedInputView {
        result[inputField.fieldId] = null
        return MaskedInputView(requireContext()).apply {
            tag = inputField.fieldId
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(
                    0,
                    resources.getDimensionPixelSize(com.design.chili.R.dimen.padding_4dp),
                    0,
                    resources.getDimensionPixelSize(com.design.chili.R.dimen.padding_4dp)
                )
            }
            setGravity(Gravity.START)
            setHint((inputField.hint ?: ""))
            setupNewMask(inputField.mask ?: "*")
            when (inputField.inputType) {
                InputFieldInputType.NUMBER -> setInputType(InputType.TYPE_CLASS_NUMBER)
                else -> setInputType(InputType.TYPE_CLASS_TEXT)
            }
            setupClearTextButton()
            setSimpleTextChangedListener {
                val input = listOf(getInputText())
                if (validateFormItem(inputField.validations, input) && isInputMaskFilled()) {
                    result[this.tag.toString()] = input
                } else {
                    result[this.tag.toString()] = null
                }
                toggleButton()
            }
            setText(inputField.value ?: "")
        }
    }


    private fun createButtonGroup(groupInfo: GroupButtonFormItem): ChatButtonsGroup {
        result[groupInfo.formItemId] = null
        return ChatButtonsGroup(requireContext()).apply {
            tag = groupInfo.formItemId
            setSelectedItemChangedListener {
                if (validateFormItem(groupInfo.validations, it)) {
                    result[this.tag.toString()] = it
                } else {
                    result[this.tag.toString()] = null
                }
                toggleButton()
            }
            setButtonType(groupInfo.buttonType)
            setChooseType(groupInfo.chooseType)
            setAllButtons(groupInfo.options)
            renderButtons()
        }
    }

    private fun createDropDownField(dropDownList: DropDownFieldInfo): MaskedInputView {
        result[dropDownList.formItemId] = null
        return MaskedInputView(requireContext()).apply {
            tag = dropDownList.formItemId
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                setMargins(
                    0,
                    resources.getDimensionPixelSize(com.design.chili.R.dimen.padding_4dp),
                    0,
                    resources.getDimensionPixelSize(com.design.chili.R.dimen.padding_4dp)
                )
            }
            setGravity(Gravity.START)
            setHint((dropDownList.label ?: ""))
            disableEdition()
            setOnClickListener {
                val options = mutableListOf<Option>()
                dropDownList.options.forEach {
                    options.add(Option(it.id, it.value, it.isSelected))
                }
                val bs = SearchSelectorBottomSheet(requireContext(), options, true)
                bs.setOnDismissListener {
                    result[dropDownList.formItemId] = options.map { it.id }
                }
                bs.show()
            }
        }
    }

    private fun validateFormItem(validations: List<Validation>?, value: List<String>): Boolean {
        validations?.forEach {
            when (it.type) {
                ValidationType.REQUIRED -> {
                    if (it.value == "true" && (value.isEmpty() || value.firstOrNull().isNullOrBlank())) return false
                }
                ValidationType.REGEX -> {
                    if (it.value != null && (!(value.firstOrNull() ?: "").matches(it.value.toRegex()))) {
                        return false
                    }
                }
            }
        }
        return true
    }

    private fun setFragmentResultAndClose() {
        requireActivity().supportFragmentManager.setFragmentResult(INPUT_FORM_RESULT, bundleOf(
            INPUT_FORM_RESULT to collectResult()
        ))
        requireActivity().closeCurrentFragment()
    }

    private fun collectResult(): FormResponse {
        val resultValues = mutableListOf<EnteredValue>()
        result.forEach {
            resultValues.add(EnteredValue(it.key, it.value))
        }
        return FormResponse(resultValues)
    }

    private fun toggleButton() {
        vb.btnDone.isEnabled = isFormFilled()
    }

    private fun isFormFilled(): Boolean {
        result.forEach {
            if (it.value == null) return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().hideKeyboard()
        _vb = null
    }

    companion object {

        const val INPUT_FORM_RESULT = "input_form_result"
        const val INPUT_FORM_ARGUMENT = "inputForm"

        fun show(fragmentManager: FragmentManager, containerId: Int, inputForm: InputForm, lifecycleOwner: LifecycleOwner, fragmentResultListener: FragmentResultListener) {
            fragmentManager.setFragmentResultListener(INPUT_FORM_RESULT, lifecycleOwner, fragmentResultListener)
            val fragment = InputFormFragment().apply { arguments = bundleOf(
                INPUT_FORM_ARGUMENT to inputForm
            )
            }
            fragmentManager.commit {
                replace(containerId, fragment)
                addToBackStack(null)
            }
        }
    }
}