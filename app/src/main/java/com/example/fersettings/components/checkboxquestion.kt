package com.example.fersettings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fersettings.R

@Composable
fun CheckboxQuestion(
    icon: ImageVector,
    title: String,
    subtitle: String,
    options: List<String>,
    selected: Set<String>,
    onToggle: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = colorResource(R.color.gray_text))
            Spacer(Modifier.width(12.dp))
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 17.sp)
        }
        Text(
            subtitle,
            fontSize = 12.sp,
            color = colorResource(R.color.gray_text),
            modifier = Modifier.padding(start = 36.dp, top = 2.dp, bottom = 4.dp),
        )
        options.forEach { option ->
            val checked = option in selected
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = checked,
                        onValueChange = { onToggle(option) },
                        role = Role.Checkbox,
                    )
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = checked,
                    onCheckedChange = null, // lo maneja la fila completa
                    modifier = Modifier.padding(start = 8.dp, end = 12.dp),
                    colors = CheckboxDefaults.colors(checkedColor = colorResource(R.color.fer_blue)),
                )
                Text(option, fontWeight = FontWeight.Medium, fontSize = 17.sp)
            }
        }
    }
}
