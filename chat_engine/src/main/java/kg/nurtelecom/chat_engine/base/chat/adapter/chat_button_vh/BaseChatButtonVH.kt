package kg.nurtelecom.chat_engine.base.chat.adapter.chat_button_vh

import android.content.res.Resources
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.model.ButtonProperties
import kg.nurtelecom.chat_engine.model.ChatButton
import java.util.Date
import java.util.concurrent.TimeUnit

abstract class BaseChatButtonVH(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var additionalButtonProperties: ButtonProperties? = null

    private var countDownTimer : CountDownTimer? = null
    private var shimmerDelayTimer: CountDownTimer? = null

    abstract val btn: Button
    abstract val progress: ProgressBar
    abstract val shimmer: ShimmerFrameLayout?

    fun onBind(chatButton: ChatButton) = with(btn) {
        setupTimer(chatButton.properties, chatButton.text)
        startShimmerDelayTimer()
        tag = chatButton.buttonId
        text = chatButton.text
        additionalButtonProperties = chatButton.properties
        setupLoader(chatButton.isLoading)
    }

    private fun setupLoader(isLoading: Boolean) {
        progress.isInvisible = !isLoading
        btn.isInvisible = isLoading
        if (isLoading) stopShimmer() else startShimmer()
    }

    private fun setupTimer(btnProperties: ButtonProperties?, btnText: String?) {
        countDownTimer?.cancel()
        btn.isEnabled = true
        if (btnProperties?.enableAt == null) return
        val mills = btnProperties.enableAt - Date().time
        if (mills <= 0) return
        btn.isEnabled = false
        stopShimmer()
        countDownTimer = createTimer(mills, {
            btn.apply { text = resources.getRetryAfterText(it) }
        }, {
            btn.apply {
                text = btnText
                isEnabled = true
                startShimmerDelayTimer()
            }
        })
        countDownTimer?.start()
    }

    fun Resources.getRetryAfterText(millisUntilFinished: Long): String {
        return getString(R.string.retry_after, millisUntilFinished.toTimeFromMillis)
    }

    private fun startShimmerDelayTimer() {
        if (!btn.isEnabled) return
        shimmerDelayTimer = shimmerDelayTimer ?: createTimer(SHIMMER_DELAY_MILLS, {}, {
            startShimmerAnimation()
        })
        shimmerDelayTimer?.start()
    }

    private fun startShimmerAnimation() {
        if (!btn.isVisible) return
        shimmer?.isVisible = true
        shimmer?.startShimmer()
        shimmer?.postDelayed({
            try {
                stopShimmer()
                startShimmerDelayTimer()
            } catch (ex: Throwable) {}
        }, SHIMMER_DURATION_MILLS)
    }

    protected fun startShimmer() {
        if (!btn.isEnabled || !btn.isVisible) return
        shimmer?.isVisible = true
        shimmer?.startShimmer()
    }

    protected fun stopShimmer() {
        shimmer?.stopShimmer()
        shimmer?.isVisible = false
    }

    fun stopShimmerAndShimmerDelay() {
        shimmerDelayTimer?.cancel()
        stopShimmer()
    }


    companion object {
        const val SHIMMER_DELAY_MILLS = 5000L
        const val SHIMMER_DURATION_MILLS = 2000L
    }

    private fun createTimer(mills: Long, onTick: (Long) -> Unit, onFinish: () -> Unit): CountDownTimer {
        return object : CountDownTimer(mills, 1000) {
            override fun onTick(millisUntilFinished: Long) { onTick(millisUntilFinished) }
            override fun onFinish() { onFinish() }
        }
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