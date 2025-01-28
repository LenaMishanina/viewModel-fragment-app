package com.practicum.fragmentlesson

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.practicum.fragmentlesson.databinding.FragmentBlank2Binding

class BlankFragment2 : Fragment() {
    private val dataModel: DataModel by activityViewModels()
    private lateinit var binding: FragmentBlank2Binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBlank2Binding.inflate(inflater)
        return binding.root
    }

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

    companion object {
        @JvmStatic
        fun newInstance() = BlankFragment2()
    }
}