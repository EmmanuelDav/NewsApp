package com.cyberiyke.newsApp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cyberiyke.newsApp.R
import com.cyberiyke.newsApp.databinding.FragmentSettingsBinding
import dagger.hilt.android.HiltAndroidApp


class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SettingsViewModel::class.java]


        viewModel.isDarkMode.observe(viewLifecycleOwner, { isDarkMode ->
            val themeSwitch = binding.root.findViewById<SwitchCompat>(R.id.themeToggle)
            themeSwitch.isChecked = isDarkMode
        })

        val themeSwitch = binding.root.findViewById<SwitchCompat>(R.id.themeToggle)
        themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onThemeToggleChanged(isChecked)
        }


        return binding.root
    }
}