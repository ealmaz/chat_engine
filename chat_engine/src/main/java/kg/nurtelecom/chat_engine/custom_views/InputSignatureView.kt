package kg.nurtelecom.chat_engine.custom_views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Environment
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.StringRes
import androidx.annotation.WorkerThread
import androidx.core.view.isVisible
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.databinding.ChatEngineViewInputSignatureBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.*

class InputSignatureView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.InputSignatureViewDefaultStyle,
    defStyle: Int = R.style.chat_engine_InputSignatureViewStyle
) : FrameLayout(context, attributeSet, defStyleAttr, defStyle), TouchAndDrawViewCallback {

    private lateinit var vb: ChatEngineViewInputSignatureBinding

    private var drawingListener: TouchAndDrawViewCallback? = null

    init {
        initView(context)
        obtainAttributes(context, attributeSet, defStyleAttr, defStyle)
    }

    private fun initView(context: Context) {
        val layoutInflater = LayoutInflater.from(context)
        vb = ChatEngineViewInputSignatureBinding.inflate(layoutInflater, this, true)
    }

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int, defStyle: Int) {
        context.obtainStyledAttributes(attributeSet, R.styleable.chat_engine_InputSignatureView, defStyleAttr, defStyle).run {
            setHint(getString(R.styleable.chat_engine_InputSignatureView_hint))
            setDescription(getString(R.styleable.chat_engine_InputSignatureView_description))
            getResourceId(R.styleable.chat_engine_InputSignatureView_descriptionTextAppearance, -1).takeIf { it != -1 }?.let {
                setDescriptionTextAppearance(it)
            }
            getResourceId(R.styleable.chat_engine_InputSignatureView_hintTextAppearance, -1).takeIf { it != -1 }?.let {
                setHintTextAppearance(it)
            }
            recycle()
        }
        setupViews()
    }

    private fun setupViews() {
        vb.drawingCanvas.setupView(this)
        vb.ivClear.setOnClickListener { vb.drawingCanvas.clearCanvas() }
    }

    fun setHint(hint: String?) {
        vb.tvHint.text = hint
    }

    fun setHint(@StringRes resId: Int) {
        vb.tvHint.setText(resId)
    }

    fun setHintTextAppearance(textAppearanceRes: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vb.tvHint.setTextAppearance(textAppearanceRes)
        } else {
            vb.tvHint.setTextAppearance(context, textAppearanceRes)
        }
    }

    fun setDescription(description: String?) {
        vb.tvDescription.text = description
    }

    fun setDescription(@StringRes resId: Int) {
        vb.tvDescription.setText(resId)
    }

    fun setDescriptionTextAppearance(textAppearanceRes: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vb.tvDescription.setTextAppearance(textAppearanceRes)
        } else {
            vb.tvDescription.setTextAppearance(context, textAppearanceRes)
        }
    }

    fun setDrawingListener(listener: TouchAndDrawViewCallback) {
        this.drawingListener = listener
    }

    @WorkerThread
    fun saveSignatureToFile(): String  {
        val signatureData = getSignatureCanvasByteArray()
        val file = createFile()
        val fos = FileOutputStream(file)
        fos.write(signatureData)
        fos.flush()
        fos.close()
        return file.path
    }

    @WorkerThread
    private fun getSignatureCanvasByteArray(): ByteArray {
        val drawingBitmap = getDrawingBitmap()
        val bitmapStream = ByteArrayOutputStream()
        drawingBitmap.compress(Bitmap.CompressFormat.PNG, 0, bitmapStream)
        return  bitmapStream.toByteArray()
    }

    @WorkerThread
    private fun getDrawingBitmap(): Bitmap {
        val bitmap = Bitmap.createBitmap(vb.drawingCanvas.width, vb.drawingCanvas.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        vb.drawingCanvas.draw(canvas)
        return bitmap
    }

    private fun createFile() : File {
        val directory = when (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            true -> File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/signatures")
            else -> File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "/signatures")
        }
        if (!directory.exists()) {
            directory.mkdirs()
        }
        return File.createTempFile(Date().toString(), ".png", directory)
    }

    override fun onStartDrawing() {
        vb.tvHint.isVisible = false
        vb.ivClear.isVisible = true
        drawingListener?.onStartDrawing()
    }

    override fun onClearCanvas() {
        vb.tvHint.isVisible = true
        vb.ivClear.isVisible = false
        drawingListener?.onClearCanvas()
    }
}