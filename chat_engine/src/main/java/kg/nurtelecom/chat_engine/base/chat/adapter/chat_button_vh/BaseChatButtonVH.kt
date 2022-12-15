package kg.nurtelecom.chat_engine.base.chat.adapter.chat_button_vh

import android.content.res.Resources
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.model.ButtonProperties
import kg.nurtelecom.chat_engine.model.ChatButton
import java.util.*
import java.util.concurrent.TimeUnit

abstract class BaseChatButtonVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var countDownTimer : CountDownTimer? = null

    abstract val btn: Button
    abstract val progress: ProgressBar

    fun onBind(chatButton: ChatButton) = with(btn) {
        setupTimer(chatButton.properties, chatButton.text)
        tag = chatButton.buttonId
        text = chatButton.text
        setupLoader(chatButton.isLoading)
    }

    private fun setupLoader(isLoading: Boolean) {
        progress.isVisible = isLoading
        btn.isVisible = !isLoading
    }

    private fun setupTimer(btnProperties: ButtonProperties?, btnText: String?) {
        countDownTimer?.cancel()
        btn.isEnabled = true
        if (btnProperties?.enableAt == null) return
        val mills = btnProperties.enableAt - Date().time
        if (mills <= 0) return
        btn.isEnabled = false
        countDownTimer = createTimer(mills, btnText)
        countDownTimer?.start()
    }

    private fun createTimer(mills: Long, btnText: String?): CountDownTimer {
        return object : CountDownTimer(mills, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                btn.apply {
                    text = resources.getRetryAfterText(millisUntilFinished)
                }
            }

            override fun onFinish() {
                btn.apply {
                    text = btnText
                    isEnabled = true
                }
            }
        }
    }

    fun Resources.getRetryAfterText(millisUntilFinished: Long): String {
        return getString(R.string.retry_after, millisUntilFinished.toTimeFromMillis)
    }

}

val Long.toTimeFromMillis: String
    get() {
        val hours = TimeUnit.MILLISECONDS.toHours(this)
        val second = this / 1000 % 60
        val minute = this / (1000 * 60) % 60
        return if (hours > 0) String.format("%02d:%02d:%02d", hours, minute, second)
        else String.format("%02d:%02d", minute, second)
    }