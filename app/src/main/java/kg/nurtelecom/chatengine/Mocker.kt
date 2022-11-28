package kg.nurtelecom.chatengine

import kg.nurtelecom.chat_engine.model.*
import java.util.*

object MessagesMocker {

    var lastRequestId = -1
    var lastResponseId = -1

    val states = listOf(MessageStatus.DONE)
    val messageContentType = listOf(MessageContentType.TEXT,MessageContentType.TEXT, MessageContentType.IMAGE_URL)

    val images = listOf<String>(
        "https://habrastorage.org/r/w1560/webt/i2/gy/g-/i2gyg--ncl86i1kb6yst8xx5iri.png",
        "https://c8.alamy.com/comp/B04JTM/golden-gate-bridge-from-fort-point-vertical-portrait-orientation-B04JTM.jpg",
        "https://previews.123rf.com/images/wajan/wajan2001/wajan200100005/140170638-eiffel-tower-in-paris-portrait-orientation.jpg",
        "https://o.kg/upload/iblock/db7/%D0%9E%21%20lottery%20car%202022_1920x360%20site%20O%21%20ru.png",
        "https://o.kg/upload/iblock/67f/1920%D1%85336_%D0%BC2%D0%BC_%D1%80%D1%83.jpg",
        "https://o.kg/upload/iblock/79b/get_cashback_vis_1200x400_ru.jpg",
        "https://o.kg/upload/iblock/d69/identification_5_1200x400_site_ru.jpg",
        "https://o.kg/upload/iblock/517/bnv-o.kg-940x250.png",
        "https://o.kg/upload/medialibrary/34b/get_cashback_vis_1200x400_ru.jpg",
        "https://o.kg/upload/medialibrary/a04/superhype100-1200x400-ru.png",
        "https://o.kg/upload/medialibrary/37b/yandexplus-30-840x250-RU.png",
        "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__480.jpg",
        "https://media.istockphoto.com/photos/wild-grass-in-the-mountains-at-sunset-picture-id1322277517?b=1&k=20&m=1322277517&s=170667a&w=0&h=BSN_5NMGYJY2qPwI3_vOcEXVSX_hmGBOmXebMBxTLX0=",
        "https://images.pexels.com/users/avatars/706370/tobias-bjorkli-624.jpeg?auto=compress&fit=crop&h=256&w=256"
    )

    val messagesText = listOf<String>(
        "1)  Способы получения SIM-карты:\neSIM в приложении за 5 минут. \nВ О!Store.\nС доставкой по адресу.",
        "2)  Вам был отправлен код подвтерждения. ",
        "3)  Мой номер: 996700000123\nКатегория номера: стандарт\nСтроимость номера: 25 с",
        "4)  Код не пришел",
        "5)  Имя: Иван\nОтчество: Игоревич\nНомер документа: РФ322430-М\nОрган, выдавший документ: \nИО32-1222",
        "6)  На почту assshd@gmail.com \nвысланы договор и QR-код с инструкциями для активации eSIM. \nДля завершения процесса активируйте eSIM \nи авторизуйтесь.",
        "7)  Подпись принята",
        "8)  Оплачено",
        "9)  Пройти персонификацию",
        "10) Примерно вот так должно получиться",
        "11) Чуйская обл.\nОктябрьский р-н\nг. Бишкек\nCкрябина 17А,18 ",
        "12) Укажите следующие данные:\nКодовое слово\nEmail\nДополнительный номер телефона"
    )

    fun requestRequest(): Message {
        return when(messageContentType.random()) {
            MessageContentType.TEXT -> {
                lastRequestId =  (lastRequestId + 1) % messagesText.size
                Message("${Date()}", messagesText[lastRequestId], MessageContentType.TEXT, MessageType.REQUEST, states.random())
            }
            else -> Message("${Date()}", images.random(), MessageContentType.IMAGE_URL, MessageType.REQUEST, states.random())
        }

    }

    fun requestResponse(): Message {
        return when(messageContentType.random()) {
            MessageContentType.TEXT -> {
                lastResponseId = (lastResponseId + 1) % messagesText.size
                Message("${Date()}", messagesText[lastResponseId], MessageContentType.TEXT, MessageType.RESPONSE, states.random())
            }
            else -> Message("${Date()}", images.random(), MessageContentType.IMAGE_URL, MessageType.RESPONSE, states.random())
        }
    }

    val buttons = arrayOf(
        ChatButton("INPUT_SIGNATURE", "Input signature", ButtonStyle.SECONDARY),
        ChatButton("INPUT_FORM", "Input form", ButtonStyle.SECONDARY),
        ChatButton("ADD_RESPONSE", "Add response", ButtonStyle.SECONDARY),
        ChatButton("ADD_REQUEST", "Add request", ButtonStyle.ACCENT),
    )
}

fun getInputForm() = InputForm(
    "ADDRESS",
    "Адрес по прописке",
    ChatButton("ADD_RESPONSE", "Add RESPONSE", ButtonStyle.SECONDARY),
    listOf(
        FormItem(
            FormItemType.INPUT_FIELD,
            InputField(
                "INPUT_REGION",
                null,
                "Region",
                null,
                null,
                "^(?!\\s*\$).+"
            )
        ),
        FormItem(
            FormItemType.INPUT_FIELD,
            InputField(
                "INPUT_CITY",
                null,
                "City",
                null,
                null,
                "^(?!\\s*\$).+"
            )
        ),
        FormItem(
            FormItemType.INPUT_FIELD,
            InputField(
                "INPUT_STREET",
                null,
                "Street",
                null,
                null,
                "^(?!\\s*\$).+"
            )
        ),
        FormItem(
            FormItemType.INPUT_FIELD,
            InputField(
                "INPUT_PHONE",
                null,
                null,
                InputFieldInputType.NUMBER,
                "+996 XXX XXX XXX",
                null
            )
        ),
        FormItem(
            FormItemType.GROUP_BUTTON_FORM_ITEM,
            GroupButtonFormItem(
                "SELECTOR_RADIO",
                listOf(
                    Option("GREEN1", "Green1", true),
                    Option("RED1", "Red1", false),
                    Option("YELLOW1", "Yellow1", false),
                ),
                ChooseType.SINGLE,
                ButtonType.RADIO_BUTTON,
                true
            )
        ),
        FormItem(
            FormItemType.GROUP_BUTTON_FORM_ITEM,
            GroupButtonFormItem(
                "ADDRESS_EQUALS",
                listOf(
                    Option("ADDRESS_EQUALS_TRUE", "Адрес места жительства совпадает \n" +
                            "с адресом прописки. ", false),
                ),
                ChooseType.SINGLE,
                ButtonType.CHEK_BOX,
                false
            )
        ),
        FormItem(
            FormItemType.GROUP_BUTTON_FORM_ITEM,
            GroupButtonFormItem(
                "SELECT_COLOR",
                listOf(
                    Option("GREEN2", "Green2", true),
                    Option("RED2", "Red2", false),
                    Option("YELLOW2", "Yellow2", false),
                ),
                ChooseType.SINGLE,
                ButtonType.TOGGLE,
                true
            )
        )
    )
)
