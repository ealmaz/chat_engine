package kg.nurtelecom.chat_engine.base.additional_fragment.input_form

import android.os.Bundle
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
import com.design.chili.view.navigation_components.ChiliToolbar
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators.DropDownFieldCreator
import kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators.GroupButtonsCreator
import kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators.InputFieldCreator
import kg.nurtelecom.chat_engine.base.additional_fragment.input_form.item_creators.UnsupportedItemCreator
import kg.nurtelecom.chat_engine.custom_views.ChatButtonsGroup
import kg.nurtelecom.chat_engine.custom_views.DropDownInputField
import kg.nurtelecom.chat_engine.databinding.ChatEngineFragmentInputFormBinding
import kg.nurtelecom.chat_engine.extensions.closeCurrentFragment
import kg.nurtelecom.chat_engine.extensions.hideKeyboard
import kg.nurtelecom.chat_engine.model.*

open class InputFormFragment : Fragment() {

    open val unsupportedTitleRes = R.string.unsupported_field
    open val buttonTextRes = R.string.next

    private var _vb: ChatEngineFragmentInputFormBinding? = null
    private val vb: ChatEngineFragmentInputFormBinding
        get() = _vb!!

    protected val inputForm: InputForm? by lazy {
       parseInputFormArgument()
    }

    protected open fun parseInputFormArgument(): InputForm? {
        return arguments?.getSerializable(INPUT_FORM_ARGUMENT) as? InputForm
    }

    protected val result = HashMap<String, List<String>?>()

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
            setText(buttonTextRes)
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
        return UnsupportedItemCreator.create(requireContext(), unsupportedTitleRes)
    }

    private fun createInputField(inputField: InputField): MaskedInputView {
        result[inputField.fieldId] = null
        return InputFieldCreator.create(requireContext(), inputField) { values, isValid ->
            result[inputField.fieldId] = if (isValid) values else null
            toggleButton()
        }
    }


    private fun createButtonGroup(groupInfo: GroupButtonFormItem): ChatButtonsGroup {
        result[groupInfo.fieldId] = null
        return GroupButtonsCreator.create(requireContext(), groupInfo) { values, isValid ->
            result[groupInfo.fieldId] = if (isValid) values else null
            toggleButton()
        }

    }

    private fun createDropDownField(dropDownList: DropDownFieldInfo): View {
        result[dropDownList.fieldId] = null
        if (dropDownList.isNeedToFetchOptions == true) {
            needToFetchOptionsFor(dropDownList.fieldId, dropDownList.parentFieldId)
        }
        return DropDownFieldCreator.create(requireContext(), dropDownList) { values, isValid ->
            result[dropDownList.fieldId] = if (isValid) values else null
            onDropDownListItemSelectionChanged(dropDownList.fieldId, values)
            toggleButton()
        }
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

    protected fun toggleButton() {
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

    open fun onDropDownListItemSelectionChanged(dropDownId: String, selectedItemId: List<String>) {}

    open fun needToFetchOptionsFor(formId: String, parentIds: List<String>?) {}

    fun setOptionsForDropDownField(fieldId: String, newOptions: List<Option>) {
        vb.root.findViewWithTag<DropDownInputField>(fieldId)?.options = newOptions
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
                add(containerId, fragment)
                addToBackStack(null)
            }
        }
    }
}