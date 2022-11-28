package kg.nurtelecom.chat_engine.base.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.design.chili.R
import kg.nurtelecom.chat_engine.base.chat.adapter.BubbleMessagesDecor
import kg.nurtelecom.chat_engine.base.chat.adapter.ItemAnchor
import kg.nurtelecom.chat_engine.base.chat.adapter.MessagesAdapter
import kg.nurtelecom.chat_engine.databinding.ChatEngineFragmentBaseChatBinding
import kg.nurtelecom.chat_engine.model.ChatButton
import kg.nurtelecom.chat_engine.model.MessageAdapterItem

abstract class BaseChatFragment : Fragment(), View.OnClickListener {

    private var _vb: ChatEngineFragmentBaseChatBinding? = null
    protected val vb: ChatEngineFragmentBaseChatBinding
    get() = _vb!!

    private val synchronizedAdapterItems: MutableList<MessageAdapterItem> = mutableListOf()
    @Synchronized get

    private val messageAdapter: MessagesAdapter by lazy {
        MessagesAdapter { onButtonClick(it) }
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
        setupViews()
    }

    protected open fun setupViews() {
        vb.input.setOnSendButtonClickListener(::onInputFieldSendButtonClick)
        setupMessageAdapter()
    }

    private fun setupMessageAdapter() = with(vb.rvMessages) {
        adapter = messageAdapter
        layoutManager = LinearLayoutManager(requireActivity()).apply { stackFromEnd = true }
        addItemDecoration(BubbleMessagesDecor(
            0,
            resources.getDimensionPixelSize(R.dimen.padding_44dp),
            resources.getDimensionPixelSize(R.dimen.padding_2dp)
        ))
    }

    protected fun addAdapterItems(vararg items: MessageAdapterItem, removePrevItem: Boolean = false, removePrevButtons: Boolean = true) {
        val newList = synchronizedAdapterItems.asSequence()
            .filter { if (removePrevButtons) it !is ChatButton else true }
            .filter { it !is ItemAnchor }
            .toMutableList()
        if (removePrevItem && newList.size >= 1) newList.removeLast()
        newList.addAll(items.toList())
        newList.add(ItemAnchor)
        synchronizedAdapterItems.apply {
            clear()
            addAll(newList)
            messageAdapter.submitList(this.toList()) {
                vb.rvMessages.scrollToPosition(lastIndex)
            }
        }
    }

    protected fun setInputFieldVisibility(isVisible: Boolean) {
        vb.input.isVisible = isVisible
    }

    override fun onClick(v: View?) {
        when (v) {
            is Button -> onButtonClick(v.tag.toString())
        }
    }

    abstract fun onButtonClick(buttonId: String)
    abstract fun onInputFieldSendButtonClick(inputFieldText: String?)

    override fun onDestroyView() {
        super.onDestroyView()
        _vb = null
    }
}