package com.example.fersettings.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fersettings.ContentPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {

    private val _prefs = MutableStateFlow(ContentPrefs())
    val prefs: StateFlow<ContentPrefs> = _prefs.asStateFlow()

    fun onProfileChange(value: String) = _prefs.update { it.copy(profile = value) }

    fun onEducationChange(value: String) = _prefs.update { it.copy(educationLevel = value) }
}
