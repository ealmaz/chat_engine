package kg.nurtelecom.chat_engine.base.additional_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.*
import androidx.lifecycle.LifecycleOwner
import com.design.chili.view.navigation_components.ChiliToolbar
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.databinding.ChatEngineFragmentInputFormBinding
import kg.nurtelecom.chat_engine.extensions.closeCurrentFragment
import kg.nurtelecom.chat_engine.extensions.hideKeyboard
import kg.nurtelecom.chat_engine.model.*

class InputFormFragment : Fragment(), InputFormResultHandler {

    private val result = HashMap<String, List<String>?>()

    private val itemAdapter: FormItemsAdapter by lazy {
        FormItemsAdapter(this)
    }

    private var _vb: ChatEngineFragmentInputFormBinding? = null
    private val vb: ChatEngineFragmentInputFormBinding
        get() = _vb!!

    private val toolbarConfig: ChiliToolbar.Configuration by lazy {
        ChiliToolbar.Configuration(
            hostActivity = requireActivity(),
            title = inputForm?.title,
            centeredTitle = true,
            navigationIconRes = R.drawable.chat_engine_ic_close,
            onNavigateUpClick = { closeCurrentFragment() },
            isNavigateUpButtonEnabled = true)
    }

    private val inputForm: InputForm? by lazy {
        arguments?.getSerializable(INPUT_FORM_ARGUMENT) as? InputForm
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _vb = ChatEngineFragmentInputFormBinding.inflate(layoutInflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupForm()
    }


    private fun setupViews() = with(vb) {
        tb.initToolbar(toolbarConfig)
        btnDone.setOnClickListener { setFragmentResultAndClose() }
        rvItem.adapter = itemAdapter
    }

    private fun setupForm() {
        val inputForm = arguments?.getSerializable(INPUT_FORM_ARGUMENT) as? InputForm
        itemAdapter.items = inputForm?.formItems ?: listOf()
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
        val temp = itemAdapter.items
        vb.btnDone.isEnabled = isFilledAll()
    }

    private fun isFilledAll(): Boolean {
        result.forEach {
            if (it.value == null) return false
        }
        return true
    }

    override fun onFieldTextChanged(fieldTag: String?, fieldValue: String?, isValid: Boolean?) {
        if (fieldTag == null) return
        result[fieldTag] = if (isValid == true) listOf(fieldValue ?: "") else null
        toggleButton()
    }

    override fun onGroupButtonSelected(
        groupTag: String?,
        selectedValuesId: List<String>?,
        isValid: Boolean?,
    ) {
        if (groupTag == null) return
        result[groupTag] = if (isValid == true) selectedValuesId ?: listOf() else null
        toggleButton()
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
            )}
            fragmentManager.commit {
                replace(containerId, fragment)
                addToBackStack(null)
            }
        }
    }
}