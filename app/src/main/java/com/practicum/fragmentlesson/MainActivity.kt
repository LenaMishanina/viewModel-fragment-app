package com.practicum.fragmentlesson

/** Урок 32-33 **/

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.practicum.fragmentlesson.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //viewModels берем из DataModel
    private val dataModel: DataModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        openFrag(R.id.placeHolder, BlankFragment.newInstance())
        openFrag(R.id.placeHolder2, BlankFragment2.newInstance())

        // первый арг - за чьим циклом жизни обсервер будет наблюдать
        // второй арг - запустится, когда обновим что нибудь в message
        dataModel.messageForActivity.observe(this, {
            //it - это обсервер
            // обновляем интерфейс
            binding.textView.text = it
        })
    }

    private fun openFrag (idHolder: Int, f: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(idHolder, f)
            .commit()
    }
}