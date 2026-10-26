package com.example.fersettings

data class ContentPrefs(
    val profile: String? = null,
    val educationLevel: String? = null,
) {
    val isComplete: Boolean get() = profile != null && educationLevel != null
}
