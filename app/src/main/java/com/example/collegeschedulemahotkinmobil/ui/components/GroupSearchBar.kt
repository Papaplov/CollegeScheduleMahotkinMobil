package com.example.collegeschedulemahotkinmobil.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.collegeschedulemahotkinmobil.data.dto.GroupDto

@Composable
fun GroupSearchBar(
    groups: List<GroupDto>,
    onGroupSelected: (GroupDto) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var showDropdown by remember { mutableStateOf(false) }

    // Фильтрация групп по введенному тексту
    val filteredGroups = if (searchText.isEmpty()) {
        groups
    } else {
        groups.filter {
            it.groupName.contains(searchText, ignoreCase = true)
        }
    }

    Column(modifier = modifier) {
        // Поле поиска
        TextField(
            value = searchText,
            onValueChange = {
                searchText = it
                showDropdown = true
            },
            label = { Text("Название группы") },
            placeholder = { Text("Введите название группы") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Кнопка для открытия списка всех групп
        TextButton(
            onClick = {
                showDropdown = !showDropdown
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            Text("▼ Показать все группы (${groups.size})")
        }

        // Выпадающий список
        if (showDropdown && filteredGroups.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)  // Используем height вместо heightIn
                ) {
                    items(filteredGroups) { group ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onGroupSelected(group)
                                    searchText = group.groupName
                                    showDropdown = false
                                }
                                .padding(12.dp)
                        ) {
                            Text(
                                text = group.groupName,
                                style = MaterialTheme.typography.titleMedium
                            )
                            if (group.specialtyName != null) {
                                Text(
                                    text = group.specialtyName,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}