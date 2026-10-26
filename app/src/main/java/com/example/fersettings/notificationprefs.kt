package com.example.fersettings

enum class NotificationFrequency(val label: String) {
    IMMEDIATE("Al momento"),
    DAILY("Una vez al día"),
    WEEKLY("Una vez a la semana"),
}

enum class NotificationTopic(val label: String) {
    SCHOLARSHIPS("Becas y convocatorias"),
    EVENTS("Eventos"),
    NEWS("Noticias"),
    DEADLINES("Recordatorios de fechas límite"),
    STORIES("Historias de becarios"),
}

data class NotificationPrefs(
    /** null = el usuario aún no responde. */
    val enabled: Boolean? = null,
    val frequency: NotificationFrequency? = null,
    val topics: Set<NotificationTopic> = emptySet(),
) {
    /** Si dijo "No" basta con eso; si dijo "Sí" debe completar las opciones. */
    val canSave: Boolean
        get() = when (enabled) {
            null -> false
            false -> true
            true -> frequency != null && topics.isNotEmpty()
        }
}

