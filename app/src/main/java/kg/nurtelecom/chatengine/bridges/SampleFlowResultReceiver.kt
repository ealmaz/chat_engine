package kg.nurtelecom.chatengine.bridges

import kg.nurtelecom.chat_engine.model.FormResponse

interface SampleFlowResultReceiver {
    fun onGetSignature(path: String)
    fun onGetFilledInputForm(formResponse: FormResponse)
    fun onGetPhotoPath(path: String)
}