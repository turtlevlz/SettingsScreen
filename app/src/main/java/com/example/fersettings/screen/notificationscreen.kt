package com.example.fersettings.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.NotificationsActive
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fersettings.NotificationFrequency
import com.example.fersettings.NotificationPrefs
import com.example.fersettings.NotificationTopic
import com.example.fersettings.R
import com.example.fersettings.components.CheckboxQuestion
import com.example.fersettings.components.FerTopBar
import com.example.fersettings.components.RadioQuestion
import com.example.fersettings.components.SettingsCard
import com.example.fersettings.viewmodel.NotificationsViewModel

private const val YES = "Sí"
private const val NO = "No"

/** Conecta el ViewModel, el permiso del sistema y el Toast. */
@Composable
fun NotificationsRoute(
    onBack: () -> Unit,
    viewModel: NotificationsViewModel = viewModel(),
) {
    val prefs by viewModel.prefs.collectAsState()
    val context = LocalContext.current

    // Android 13+ exige pedir el permiso en tiempo de ejecución.
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        viewModel.onEnabledChange(granted)
        if (!granted) {
            Toast.makeText(
                context,
                "Activa el permiso de notificaciones en los ajustes del sistema",
                Toast.LENGTH_LONG,
            ).show()
        }
    }

    NotificationsScreen(
        prefs = prefs,
        onBack = onBack,
        onEnabledChange = { wantsNotifications ->
            val needsPermission = wantsNotifications &&
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                    ContextCompat.checkSelfPermission(
                        context, Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED

            if (needsPermission) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                viewModel.onEnabledChange(wantsNotifications)
            }
        },
        onFrequencyChange = viewModel::onFrequencyChange,
        onTopicToggle = viewModel::onTopicToggle,
        onSave = {
            viewModel.save()
            Toast.makeText(context, "Preferencias guardadas", Toast.LENGTH_SHORT).show()
            onBack()
        },
    )
}

/** UI pura. */
@Composable
fun NotificationsScreen(
    prefs: NotificationPrefs,
    onBack: () -> Unit,
    onEnabledChange: (Boolean) -> Unit,
    onFrequencyChange: (NotificationFrequency) -> Unit,
    onTopicToggle: (NotificationTopic) -> Unit,
    onSave: () -> Unit,
) {
    Scaffold(
        containerColor = colorResource(R.color.bg),
        topBar = { FerTopBar() },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Volver")
                }
                Text("Notificaciones", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            }
            Spacer(Modifier.height(16.dp))

            // 1) ¿Quiere recibir notificaciones?
            SettingsCard {
                RadioQuestion(
                    icon = Icons.Outlined.NotificationsActive,
                    title = "¿Quieres recibir notificaciones?",
                    subtitle = "Te avisaremos cuando haya contenido nuevo.",
                    options = listOf(YES, NO),
                    selected = when (prefs.enabled) {
                        true -> YES
                        false -> NO
                        null -> null
                    },
                    onSelect = { onEnabledChange(it == YES) },
                )
            }

            // 2) Opciones extra: solo si respondió "Sí"
            AnimatedVisibility(visible = prefs.enabled == true) {
                Column {
                    Spacer(Modifier.height(16.dp))
                    SettingsCard {
                        RadioQuestion(
                            icon = Icons.Outlined.Schedule,
                            title = "¿Cada cuánto quieres recibirlas?",
                            subtitle = "Elige la frecuencia que prefieras.",
                            options = NotificationFrequency.entries.map { it.label },
                            selected = prefs.frequency?.label,
                            onSelect = { label ->
                                NotificationFrequency.entries
                                    .first { it.label == label }
                                    .let(onFrequencyChange)
                            },
                        )
                        HorizontalDivider()
                        CheckboxQuestion(
                            icon = Icons.Outlined.Category,
                            title = "¿De qué quieres recibir avisos?",
                            subtitle = "Puedes elegir varias opciones.",
                            options = NotificationTopic.entries.map { it.label },
                            selected = prefs.topics.map { it.label }.toSet(),
                            onToggle = { label ->
                                NotificationTopic.entries
                                    .first { it.label == label }
                                    .let(onTopicToggle)
                            },
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = onSave,
                enabled = prefs.canSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.fer_blue)),
            ) {
                Text("Guardar preferencias", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}
