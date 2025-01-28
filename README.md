Проект реализует отправку сообщения от фрагмента 1 к фрагменту 2 и активити, от фрагмента 2 к фрагменту 1 и активити (урок 33, см. ниже)

<h2>Урок 32</h2>
Fragment - часть интерфейса, которую можно переиспользовать в приложении (работают быстрее чем Activity)

у Fragment свой цикл жизни, но он зависит от цикла жизни Activity

1. Создаем пустой fragment com.pract.lesson -> new -> Fragment -> Fragment (Blank)

onCreateView - для создания разметки

***
Класс в языке Kotlin может содержать companion-объекты. 

companion-объект определяется внутри некоторого класса и позволяет определить свойства и методы, которые будут общими для всех объектов этого класса. 
(типа статические поля и методы)

Общий синтаксис
```
class ClassName{
    // свойства и методы класса
    companion object {
        // свойства и методы companion-объекта
    }
}
```
Например, нам надо подсчитать, сколько было создано объектов определенного класса. Для этого определим следующую программу:
```
class Person(val name: String){
   
    init{
        counter++
    }
    companion object {
        var counter = 0
    }
}
fun main (){
    println(Person.counter) // 0
    Person("Tom")
    println(Person.counter) // 1
    Person("Bob")
    Person("Sam")
    println(Person.counter) // 3
 
}
```
2. Достаточно один раз вызвать newInstance, чтобы создать в памяти фрагмент (Синглтон)
```
    companion object {
        @JvmStatic
        fun newInstance() = BlankFragment()
    }
```
3. onViewCreated запускаетсяя после onCreateView, здесь можно найти элементы
   
4. в activity.xml добавляем FrameLayout
   
6. Отобразим BlankFragment на Activity
```
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.placeHolder, BlankFragment.newInstance())
            .commit()

    }
}
```
6. fragment через binding
```
class BlankFragment2 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentBlank2Binding.inflate(inflater)
        return binding.root
    }
    companion object {
        @JvmStatic
        fun newInstance() = BlankFragment2()
    }
}
```

<h2>Урок 33</h2>
Передача данных между фрагментами и активити

1. Добавить зависимости
```
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)
```
2. Создать два фрагмента и одно активити. На активити разместить оба фрагмента

Один из фрагментов:
```
<FrameLayout layout_width="match_parent" layout_height="match_parent">

    <LinearLayout gravity="center" orientation="vertical">

        <TextView
            android:id="@+id/tvMessage"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Message" />

        <Button
            android:id="@+id/btnSendMessageToFrag2"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Send message to Fragment 2" />

        <Button
            android:id="@+id/btnSendMessageToActivity"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Send message to Activity" />
    </LinearLayout>

</FrameLayout>
```
На активити textView (android:id="@+id/textView") и два фрагмента

Отобразим фрагменты на активити
```
    private fun openFrag (idHolder: Int, f: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(idHolder, f)
            .commit()
    }
        openFrag(R.id.placeHolder, BlankFragment.newInstance())
        openFrag(R.id.placeHolder2, BlankFragment2.newInstance())
```
3. Для передачи данных между активити и фрагментами создадим класс DataModel (: ViewModel)
   
Open class в Kotlin — это класс, который можно унаследовать. 
По умолчанию все классы в Kotlin имеют статус final, который блокирует возможность наследования. 
Чтобы сделать класс наследуемым, его нужно пометить ключевым словом open.
```
open class DataModel : ViewModel() {
    //by lazy - создает message только один раз,
    // при создании нового объекта DataModel данные в message можно обновить,
    // но они не создадутся заново
    val message: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}
```
4. Подключим к активити
```
class MainActivity : AppCompatActivity() {
    //viewModels берем из DataModel
    private val dataModel: DataModel by viewModels()
}
```
5. Создадим обсервер (observe) - наблюдатель, который наблюдает, когда можно обновить данные

!!!Почитать об этом подробнее
```
class MainActivity : AppCompatActivity() {
    private val dataModel: DataModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        // первый арг - за чьим циклом жизни обсервер будет наблюдать
        // второй арг - запустится, когда обновим что нибудь в message
	// выводим изменения сообщения при нажатии на кнопку с фрагмента 1 или фрагмента 2
        dataModel.message.observe(this, {
            //it - это обсервер
            // обновляем интерфейс
            binding.textView.text = it
        })
    }
}
```
6. Отправка сообщения от фрагмента 1
```
class BlankFragment : Fragment() {
    //activityViewModels, так как ViewModels берем с активити
    private val dataModel: DataModel by activityViewModels()

    //работа с интерфейсом???
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.btnSendMessageToActivity.setOnClickListener {
            dataModel.message.value = "Hello Activity from Fragment 1"
        }

        binding.btnSendMessageToFrag2.setOnClickListener {
            //меняем значение message в dataModel
            dataModel.message.value = "Hello Fragment 2 from Fragment 1"
        }
    }
}
```
7. Примем сообщение от фрагмента 1 во фрагменте 2
```
class BlankFragment2 : Fragment() {
    private val dataModel: DataModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataModel.message.observe(activity as LifecycleOwner, {
            binding.tvMessage.text = it
        })
    }
}
```
<h3>НО РАБОТАЕТ НЕВЕРНО:</h3>

сейчас при нажатии на любую кнопку во фрагменте 1 сообщение отображается и во фрагменте 2, и в активити,
ПОЭТОМУ нужно добавить в DataModel еще одну переменную message

ИСПРАВЛЯЕМ
```
open class DataModel : ViewModel() {
    val messageForActivity: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val messageForFragment1: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val messageForFragment2: MutableLiveData<String> by lazy { MutableLiveData<String>() }
}
```
```
class MainActivity : AppCompatActivity() {
    private val dataModel: DataModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        dataModel.messageForActivity.observe(this, {
            binding.textView.text = it
        })
    }
}
```
```
class BlankFragment : Fragment() {
    private val dataModel: DataModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataModel.messageForFragment1.observe(activity as LifecycleOwner, {
            binding.tvMessage.text = it
        })

        binding.btnSendMessageToActivity.setOnClickListener {
            dataModel.messageForActivity.value = "Hello Activity from Fragment 1"
        }

        binding.btnSendMessageToFrag2.setOnClickListener {
            dataModel.messageForFragment2.value = "Hello Fragment 2 from Fragment 1"
        }
    }
}
```
```
class BlankFragment2 : Fragment() {
    private val dataModel: DataModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataModel.messageForFragment2.observe(activity as LifecycleOwner, {
            binding.tvMessage.text = it
        })

        binding.btnSendMessageToActivity.setOnClickListener {
            dataModel.messageForActivity.value = "Hello Activity from Fragment 2"
        }

        binding.btnSendMessageToFrag1.setOnClickListener {
            dataModel.messageForFragment1.value = "Hello Fragment 1 from Fragment 2"
        }
    }
```
***ViewModel используется для передачи данных между фрагментами или между фрагментами и активити
