package com.example.fersettings.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fersettings.ContentPrefs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AppearanceMode(val label: String) {
    LIGHT("Claro"),
    DARK("Oscuro"),
    SYSTEM("Sistema")
}
class SettingsViewModel : ViewModel() {

    private val _prefs = MutableStateFlow(ContentPrefs())
    val prefs: StateFlow<ContentPrefs> = _prefs.asStateFlow()

    private val _appearanceMode = MutableStateFlow(AppearanceMode.SYSTEM)
    val appearanceMode: StateFlow<AppearanceMode> = _appearanceMode.asStateFlow()

    fun onAppearanceChange(value: AppearanceMode) = _appearanceMode.update { value }
    fun onProfileChange(value: String) = _prefs.update { it.copy(profile = value) }

    fun onEducationChange(value: String) = _prefs.update { it.copy(educationLevel = value) }
}
