package kg.nurtelecom.chatengine.bridges

import kg.nurtelecom.chat_engine.model.InputForm

interface SampleFlowScreenNavigator {
    fun openInputFormScreen(inputForm: InputForm)
    fun openInputSignatureScreen()
}