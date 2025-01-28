package com.practicum.fragmentlesson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.practicum.fragmentlesson.databinding.FragmentBlankBinding

class BlankFragment : Fragment() {
    //activityViewModels, так как ViewModels берем с активити
    private val dataModel: DataModel by activityViewModels()
    private lateinit var binding: FragmentBlankBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBlankBinding.inflate(inflater)
        return binding.root
    }

    //работа с интерфейсом???
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dataModel.messageForFragment1.observe(activity as LifecycleOwner, {
            binding.tvMessage.text = it
        })

        binding.btnSendMessageToActivity.setOnClickListener {
            dataModel.messageForActivity.value = "Hello Activity from Fragment 1"
        }

        binding.btnSendMessageToFrag2.setOnClickListener {
            //меняем значение message в dataModel
            dataModel.messageForFragment2.value = "Hello Fragment 2 from Fragment 1"
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = BlankFragment()
    }
}