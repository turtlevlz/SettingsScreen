package com.example.fersettings.viewmodel

import androidx.lifecycle.ViewModel
import com.example.fersettings.NotificationFrequency
import com.example.fersettings.NotificationPrefs
import com.example.fersettings.NotificationTopic
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotificationsViewModel : ViewModel() {

    private val _prefs = MutableStateFlow(NotificationPrefs())
    val prefs: StateFlow<NotificationPrefs> = _prefs.asStateFlow()

    fun onEnabledChange(enabled: Boolean) = _prefs.update { it.copy(enabled = enabled) }

    fun onFrequencyChange(frequency: NotificationFrequency) =
        _prefs.update { it.copy(frequency = frequency) }

    fun onTopicToggle(topic: NotificationTopic) = _prefs.update {
        val topics = if (topic in it.topics) it.topics - topic else it.topics + topic
        it.copy(topics = topics)
    }

    fun save() {
        // TODO: persistir _prefs.value (DataStore / API).
        // Si enabled == false, ignora frequency y topics al guardar.
    }
}

