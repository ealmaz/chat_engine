package kg.nurtelecom.chatengine

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.fragment.app.Fragment
import com.design.chili.view.navigation_components.ChiliToolbar
import kg.nurtelecom.chat_engine.base.chat.BaseChatFragment
import kg.nurtelecom.chat_engine.base.chat.adapter.ItemTyping
import kg.nurtelecom.chat_engine.model.*
import kg.nurtelecom.chatengine.bridges.SampleFlowResultReceiver
import kg.nurtelecom.chatengine.bridges.SampleFlowScreenNavigator
import java.util.*


class SampleChatFragment : BaseChatFragment(), ActivityResultCallback<Intent?>,
    SampleFlowResultReceiver {
    override fun setupToolbar(chiliToolbar: ChiliToolbar) {
        chiliToolbar.initToolbar(
            ChiliToolbar.Configuration(requireActivity(), "", true, kg.nurtelecom.chat_engine.R.drawable.chat_engine_ic_close, isNavigateUpButtonEnabled = true)
        )
        chiliToolbar.setupDividerVisibility(false)
        chiliToolbar.setToolbarBackgroundColor(Color.TRANSPARENT)
    }

    override fun setupViews() {
        super.setupViews()
        setInputFieldVisibility(true)
        addAdapterItems(*MessagesMocker.buttons, removePrevItem = false, removePrevButtons = true)
    }

    override fun onButtonClick(buttonId: String, additionalProperties: ButtonProperties?) {
        when (buttonId) {
            "LOADER" -> MessagesMocker.buttons.find { it.buttonId == buttonId }?.copy(isLoading = true)?.let {
                updateItemProperty(buttonId, it)
            }
            "ADD_RESPONSE" -> addAdapterItems(MessagesMocker.requestResponse(), *MessagesMocker.buttons)
            "ADD_REQUEST" -> addMessageDelay(MessagesMocker.requestRequest())
            "INPUT_FORM" -> openForm()
            "INPUT_SIGNATURE" -> openSignatureFragment()
        }
    }

    fun addMessageDelay(message: Message) {
        addAdapterItems(ItemTyping)
        vb.root.postDelayed({
            addAdapterItems(message, *MessagesMocker.buttons, removePrevItem = true)
        }, 1500)

    }

    private fun openSignatureFragment() {
        (requireActivity() as SampleFlowScreenNavigator).openInputSignatureScreen()
    }

    override fun onActivityResult(resultIntent: Intent?) {

    }

    override fun onGetSignature(path: String) {
        addAdapterItems(Message("${Date()}", path, MessageContentType.IMAGE_FILE_PATH, MessageType.USER), *MessagesMocker.buttons)
    }

    override fun onGetFilledInputForm(formResponse: FormResponse) {
        val messageContent = java.lang.StringBuilder()
        formResponse.values.forEach {
            it.enteredValue?.forEach {
                messageContent.append(it)
                messageContent.append("\n")
            }

        }
        addAdapterItems(Message("${Date()}", messageContent.toString(), MessageContentType.TEXT, MessageType.USER), *MessagesMocker.buttons)
    }

    override fun onGetPhotoPath(path: String) {
        addAdapterItems(Message("${Date()}", path, MessageContentType.IMAGE_FILE_PATH, MessageType.USER), *MessagesMocker.buttons)
    }


    override fun onInputFieldSendButtonClick(inputFieldText: String?) {
        vb.input.setText("")
        addAdapterItems(Message("${Date()}", inputFieldText ?: "", MessageContentType.TEXT, MessageType.USER), *MessagesMocker.buttons)
    }

    override fun onLinkClick(url: String) {
        Toast.makeText(requireContext(), url, Toast.LENGTH_SHORT).show()
    }

    override fun onOpenForm(inputFormId: String) {
        (requireActivity() as SampleFlowScreenNavigator).openInputFormScreen(getInputForm())
    }

    override fun onOpenWebView(webViewId: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"))
        startActivity(browserIntent)
    }

    companion object {
        fun create(): Fragment {
            return SampleChatFragment()
        }
    }
}

