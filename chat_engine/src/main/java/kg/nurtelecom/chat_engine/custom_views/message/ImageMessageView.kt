package kg.nurtelecom.chat_engine.custom_views.message

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.databinding.ChatEngineViewImageMessageBinding
import kg.nurtelecom.chat_engine.model.MessageStatus
import kg.nurtelecom.chat_engine.model.MessageType
import java.io.File

class ImageMessageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.baseMessageViewDefaultStyle,
    defStyle: Int = R.style.chat_engine_BaseMessageViewStyle
): BaseMessageView<ChatEngineViewImageMessageBinding>(context, attrs, defStyleAttr, defStyle) {

    //todo: Подумать нужно ли выносить glide в переменную

    override fun inflateView(): ChatEngineViewImageMessageBinding {
        return ChatEngineViewImageMessageBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun obtainAttributes(typedArray: TypedArray) {
        super.obtainAttributes(typedArray)
        typedArray.run {
            getResourceId(R.styleable.chat_engine_BaseMessageView_android_src, -1).takeIf { it != -1 }?.let {
                setImageResource(it)
            }
            getString(R.styleable.chat_engine_BaseMessageView_imageUrl)?.let {
                loadImageFromUrl(it)
            }
            getLayoutDimension(R.styleable.chat_engine_BaseMessageView_imageWidth, -1).takeIf { it != -1 }?.let {
                vb.ivImage.layoutParams.width = it
            }
            getLayoutDimension(R.styleable.chat_engine_BaseMessageView_imageHeight, -1).takeIf { it != -1 }?.let {
                vb.ivImage.layoutParams.height = it
            }
            getInt(R.styleable.chat_engine_BaseMessageView_android_scaleType, -1).takeIf { it != -1 }?.let {
                val types = ImageView.ScaleType.values()
                val scaleType = types[it]
                vb.ivImage.scaleType = scaleType
            }
        }
    }

    override fun setupMessageStatus(status: MessageStatus) {
        when (messageType) {
            MessageType.RESPONSE -> setupMessageStatusForResponse(status)
            MessageType.REQUEST -> setupMessageStatusForRequest(status)
            else -> return
        }
    }

    private fun setupMessageStatusForRequest(status: MessageStatus) {
        resetPreviousStatuses()
        when (status) {
            MessageStatus.LOADING -> setupStatusIcon(R.drawable.chat_engine_ic_clock_white_bg)
            MessageStatus.ERROR -> setupStatusIcon(R.drawable.chat_engine_ic_circle_warning)
            else -> {}
        }
    }

    private fun setupMessageStatusForResponse(status: MessageStatus) {
        resetPreviousStatuses()
        when (status) {
            MessageStatus.LOADING -> {
                vb.cvImageContainer.foreground = AppCompatResources.getDrawable(context, R.drawable.chat_engine_image_loading_foreground)
                vb.pbProgress.isVisible = true
            }
            MessageStatus.DONE -> setupStatusIcon(R.drawable.chat_engine_ic_circle_done)
            MessageStatus.ERROR -> setupStatusIcon(R.drawable.chat_engine_ic_circle_warning)
        }
    }

    private fun resetPreviousStatuses() = with(vb) {
        cvImageContainer.foreground = null
        pbProgress.isVisible = false
        ivStatus.isVisible = false
    }

    private fun setupStatusIcon(@DrawableRes iconRes: Int) {
        vb.ivStatus.apply{
            isVisible = true
            setImageResource(iconRes)
        }
    }

    override fun setupAsResponseMessage() {
        super.setupAsResponseMessage()
        val constraintSet = ConstraintSet()
        constraintSet.clone(vb.clRoot)
        constraintSet.connect(R.id.cv_image_container, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        constraintSet.clear(R.id.cv_image_container, ConstraintSet.START)
        constraintSet.applyTo(vb.clRoot)
    }

    override fun setupAsRequestMessage() {
        super.setupAsRequestMessage()
        val constraintSet = ConstraintSet()
        constraintSet.clone(vb.clRoot)
        constraintSet.connect(R.id.cv_image_container, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        constraintSet.clear(R.id.cv_image_container, ConstraintSet.END)
        constraintSet.applyTo(vb.clRoot)
    }

    fun loadImageFromUrl(url: String) {
        if (vb.ivImage.tag == url) return
        startImageShimmer()
        Glide.with(context)
            .load(url)
            .override(Target.SIZE_ORIGINAL)
            .listener(imageListenerForShimmer)
            .into(vb.ivImage)
    }

    fun loadImageFormFilePath(filePath: String) {
        if (vb.ivImage.tag == filePath) return
        startImageShimmer()
        Glide.with(context)
            .load(File(filePath))
            .override(Target.SIZE_ORIGINAL)
            .listener(imageListenerForShimmer)
            .into(vb.ivImage)
    }

    fun setImageResource(@DrawableRes imageRes: Int) {
        vb.ivImage.setImageResource(imageRes)
    }

    fun getImageView(): ImageView {
        return vb.ivImage
    }

    private fun startImageShimmer() = with(vb) {
        ivImage.isVisible = false
        shimmer.isVisible = true
        shimmer.startShimmer()
    }

    private fun stopShimmer() = with(vb) {
        ivImage.isVisible = true
        shimmer.stopShimmer()
        shimmer.isVisible = false
    }

    private val imageListenerForShimmer = object : RequestListener<Drawable> {

        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            vb.ivImage.tag = ""
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean, ): Boolean {
            if (resource == null) return false
            stopShimmer()
            vb.ivImage.tag = model.toString()
            return false
        }
    }
}