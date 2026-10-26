package com.example.fersettings.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fersettings.ContentPrefs
import com.example.fersettings.R
import com.example.fersettings.components.FerTopBar
import com.example.fersettings.components.RadioQuestion
import com.example.fersettings.components.SettingsCard
import com.example.fersettings.components.SettingsRow
import com.example.fersettings.viewmodel.SettingsViewModel

private val profileOptions = listOf("Aspirante", "Becario", "Otro")
private val educationOptions = listOf("Preparatoria", "Universidad", "Otro")

@Composable
fun SettingsRoute(viewModel: SettingsViewModel = viewModel()) {
    val prefs by viewModel.prefs.collectAsState()
    val context = LocalContext.current

    SettingsScreen(
        prefs = prefs,
        onProfileChange = viewModel::onProfileChange,
        onEducationChange = viewModel::onEducationChange,
        onSave = { Toast.makeText(context, "Se guardaron las preferencias", Toast.LENGTH_SHORT).show() },
    )
}

@Composable
fun SettingsScreen(
    prefs: ContentPrefs,
    onProfileChange: (String) -> Unit,
    onEducationChange: (String) -> Unit,
    onSave: () -> Unit,
    onNotificationsClick: () -> Unit = {},
    onAppearanceClick: () -> Unit = {},
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("Ajustes", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)

            SettingsCard {
                SettingsRow(Icons.Outlined.Notifications, "Notificaciones", onNotificationsClick)
                HorizontalDivider()
                SettingsRow(Icons.Outlined.DarkMode, "Apariencia", onAppearanceClick)
            }

            Text(
                "Preferencias de contenido",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = colorResource(R.color.gray_text),
            )

            SettingsCard {
                RadioQuestion(
                    icon = Icons.Outlined.Person,
                    title = "¿Cuál es tu perfil?",
                    subtitle = "Selecciona la opción que mejor te describa.",
                    options = profileOptions,
                    selected = prefs.profile,
                    onSelect = onProfileChange,
                )
                HorizontalDivider()
                RadioQuestion(
                    icon = Icons.AutoMirrored.Outlined.MenuBook,
                    title = "¿Cuál es tu nivel educativo?",
                    subtitle = "Selecciona el nivel académico de tu interés.",
                    options = educationOptions,
                    selected = prefs.educationLevel,
                    onSelect = onEducationChange,
                )
                Button(
                    onClick = onSave,
                    enabled = prefs.isComplete,
                    modifier = Modifier
                        .padding(16.dp)
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
}
