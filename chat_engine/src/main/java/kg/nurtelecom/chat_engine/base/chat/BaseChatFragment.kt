package kg.nurtelecom.chat_engine.base.chat

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.design.chili.R
import com.design.chili.view.navigation_components.ChiliToolbar
import kg.nurtelecom.chat_engine.base.chat.adapter.BubbleMessagesDecor
import kg.nurtelecom.chat_engine.base.chat.adapter.ItemAnchor
import kg.nurtelecom.chat_engine.base.chat.adapter.ItemTyping
import kg.nurtelecom.chat_engine.base.chat.adapter.MessagesAdapter
import kg.nurtelecom.chat_engine.databinding.ChatEngineFragmentBaseChatBinding
import kg.nurtelecom.chat_engine.model.*

abstract class BaseChatFragment : Fragment() {

    private var _vb: ChatEngineFragmentBaseChatBinding? = null
    protected val vb: ChatEngineFragmentBaseChatBinding
    get() = _vb!!

    private val synchronizedAdapterItems: MutableList<MessageAdapterItem> = mutableListOf()
    @Synchronized get

    private val messageAdapter: MessagesAdapter by lazy { createMessagesAdapter() }

    open fun createMessagesAdapter(): MessagesAdapter {
        return MessagesAdapter (::onButtonClick, { onLinkClick(it) })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _vb = ChatEngineFragmentBaseChatBinding.inflate(layoutInflater)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(vb.mainToolbar)
        setupViews()
    }

    abstract fun setupToolbar(chiliToolbar: ChiliToolbar)

    protected open fun setupViews() {
        vb.input.setOnSendButtonClickListener(::onInputFieldSendButtonClick)
        setupMessageAdapter()
    }

    private fun setupMessageAdapter() = with(vb.rvMessages) {
        adapter = messageAdapter
        layoutManager = LinearLayoutManager(requireActivity()).apply { stackFromEnd = true }
        addItemDecoration(BubbleMessagesDecor(
            resources.getDimensionPixelSize(R.dimen.padding_2dp),
            resources.getDimensionPixelSize(R.dimen.padding_8dp),
            resources.getDimensionPixelSize(R.dimen.padding_44dp),
            resources.getDimensionPixelSize(R.dimen.padding_16dp)
        ))
    }

    @Synchronized
    protected fun addAdapterItems(vararg items: MessageAdapterItem, removePrevButtons: Boolean = true) {
        synchronizedAdapterItems.apply {
            val newList = this.asSequence()
                .filter { if (removePrevButtons) it !is ChatButton else true }
                .filter { it !is ItemAnchor }
                .toMutableList()
            newList.addAll(items.toList())
            newList.add(ItemAnchor)
            newList.sortBy {
                when(it) {
                    is ItemAnchor -> 3
                    is ChatButton -> 2
                    is ItemTyping -> 1
                    else -> 0
                }
            }
            clear()
            addAll(newList.distinctBy { it.getItemId() })
            messageAdapter.submitList(this.toList()) {
                tryScrollToPosition(lastIndex)
            }
        }
    }

    @Synchronized
    protected fun updateItemProperty(id: String, updatedItem: MessageAdapterItem) {
        val newList = synchronizedAdapterItems.map { if (it.getItemId() == id) updatedItem else it }
        synchronizedAdapterItems.apply {
            clear()
            addAll(newList.distinctBy { it.getItemId() })
            messageAdapter.submitList(this.toList()) {
                tryScrollToPosition(lastIndex)
            }
        }
    }

    private fun tryScrollToPosition(position: Int) {
        try { vb.rvMessages.scrollToPosition(position) }
        catch (ex: Throwable) {}
    }

    protected fun showTyping(removePrevButtons: Boolean = false) {
        addAdapterItems(ItemTyping, removePrevButtons = removePrevButtons)
    }

    protected fun hideTyping() {
        synchronizedAdapterItems.apply {
            removeAll { it.getItemId() == ItemTyping.getItemId() }
            messageAdapter.submitList(this.toList()) {
                tryScrollToPosition(lastIndex)
            }
        }
    }

    protected fun setInputFieldVisibility(isVisible: Boolean) {
        vb.input.isVisible = isVisible
    }

    protected open fun setupInputField(field: InputField?) {
        if (field == null) {
            setInputFieldVisibility(false)
        } else {
            setInputFieldVisibility(true)
            vb.input.apply {
                tag = field.fieldId
                setHint(field.hint ?: "")
                setupMask(field.mask, field.maskSymbols?.map { it.first() })
                setInputType(field.inputType)
                setValidation(field.validations)
                setText(field.value ?: "")
                setIsLoading(false)
            }
        }
    }

    open fun onButtonClick(buttonId: String, additionalProperties: ButtonProperties?): Boolean {
        return when {
            additionalProperties?.formIdToOpen != null -> {
                onOpenForm(additionalProperties.formIdToOpen)
                true
            }
            additionalProperties?.webViewIdToOpen != null -> {
                onOpenWebView(additionalProperties.webViewIdToOpen)
                true
            }
            else -> false
        }
    }
    abstract fun onInputFieldSendButtonClick(inputFieldText: String?)
    abstract fun onLinkClick(url: String)
    abstract fun onOpenForm(inputFormId: String)
    abstract fun onOpenWebView(webViewId: String)

    override fun onDestroyView() {
        super.onDestroyView()
        _vb = null
    }
}