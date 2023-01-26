package kg.nurtelecom.chat_engine.base.additional_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.fragment.app.commit
import androidx.lifecycle.LifecycleOwner
import com.design.chili.view.navigation_components.ChiliToolbar
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kg.nurtelecom.chat_engine.R
import kg.nurtelecom.chat_engine.custom_views.TouchAndDrawViewCallback
import kg.nurtelecom.chat_engine.databinding.ChatEngineFragmentInputSignatureBinding
import kg.nurtelecom.chat_engine.extensions.closeCurrentFragment

class InputSignatureFragment : Fragment(), TouchAndDrawViewCallback {

    private var _vb: ChatEngineFragmentInputSignatureBinding? = null
    val vb: ChatEngineFragmentInputSignatureBinding
        get() = _vb!!

    private val disposable = CompositeDisposable()

    private val toolbarConfig: ChiliToolbar.Configuration by lazy {
        ChiliToolbar.Configuration(
            hostActivity = requireActivity(),
            title = resources.getString(R.string.sign),
            centeredTitle = false,
            navigationIconRes = R.drawable.chat_engine_ic_close,
            onNavigateUpClick = { closeCurrentFragment() },
            isNavigateUpButtonEnabled = true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _vb = ChatEngineFragmentInputSignatureBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() = with(vb) {
        tb.initToolbar(toolbarConfig)
        signatureInput.setDrawingListener(this@InputSignatureFragment)
        btnContinue.setOnClickListener { saveSignature() }
    }

    private fun saveSignature()  {
        vb.btnContinue.setIsLoading(true)
        disposable.add(Single.create<String> {
            val path = vb.signatureInput.saveSignatureToFile()
            it.onSuccess(path)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { vb.btnContinue.setIsLoading(false) }
            .subscribe(
                { setResultAndClose(it) },
                { Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show() }
            )
        )
    }

    private fun setResultAndClose(path: String) {
        parentFragmentManager.setFragmentResult(
            SIGNATURE_REQUEST_CODE,
            bundleOf(SIGNATURE_PATH_BUNDLE to path)
        )
        requireActivity().closeCurrentFragment()
    }

    override fun onStartDrawing() {
        vb.btnContinue.isEnabled = true
    }

    override fun onClearCanvas() {
        vb.btnContinue.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _vb = null
        disposable.dispose()
    }

    companion object {

        const val SIGNATURE_REQUEST_CODE = "signature_code"
        const val SIGNATURE_PATH_BUNDLE = "signature_bundle"

        fun show(
            fragmentManager: FragmentManager,
            containerId: Int,
            lifecycleOwner: LifecycleOwner,
            fragmentResultListener: FragmentResultListener
        ) {
            fragmentManager.setFragmentResultListener(
                SIGNATURE_REQUEST_CODE,
                lifecycleOwner,
                fragmentResultListener
            )
            fragmentManager.commit {
                add(containerId, InputSignatureFragment())
                addToBackStack(null)
            }
        }
    }
}