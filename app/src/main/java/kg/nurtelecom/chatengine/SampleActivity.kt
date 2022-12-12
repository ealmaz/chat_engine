package kg.nurtelecom.chatengine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentResultListener
import kg.nurtelecom.chat_engine.base.additional_fragment.input_form.InputFormFragment
import kg.nurtelecom.chat_engine.base.additional_fragment.InputSignatureFragment
import kg.nurtelecom.chat_engine.model.FormResponse
import kg.nurtelecom.chat_engine.model.InputForm
import kg.nurtelecom.chatengine.bridges.SampleFlowResultReceiver
import kg.nurtelecom.chatengine.bridges.SampleFlowScreenNavigator

class SampleActivity : AppCompatActivity(), SampleFlowScreenNavigator, FragmentResultListener {

    lateinit var resultReceiver: SampleFlowResultReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)
        resultReceiver = if (savedInstanceState == null) {
            SampleChatFragment.create().also {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_container, it, SampleChatFragment::class.java.canonicalName)
                    .commit()
            } as SampleFlowResultReceiver
        } else {
            supportFragmentManager.findFragmentByTag(SampleChatFragment::class.java.canonicalName) as SampleFlowResultReceiver
        }
    }

    override fun onFragmentResult(requestKey: String, result: Bundle) {
        when (requestKey) {
            InputFormFragment.INPUT_FORM_RESULT -> {
                (result.getSerializable(InputFormFragment.INPUT_FORM_RESULT) as FormResponse).let {
                    resultReceiver.onGetFilledInputForm(it)
                }
            }
            InputSignatureFragment.SIGNATURE_REQUEST_CODE -> {
                (result.getString(InputSignatureFragment.SIGNATURE_PATH_BUNDLE))?.let {
                    resultReceiver.onGetSignature(it)
                }
            }
        }
    }

    override fun openInputFormScreen(inputForm: InputForm) {
        InputFormFragment.show(supportFragmentManager, R.id.fl_container, inputForm, this, this)
    }


    override fun openInputSignatureScreen() {
        InputSignatureFragment.show(supportFragmentManager, R.id.fl_container, this, this)
    }
}