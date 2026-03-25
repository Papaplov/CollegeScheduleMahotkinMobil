package com.example.collegeschedulemahotkinmobil.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.collegeschedulemahotkinmobil.data.dto.GroupDto
import com.example.collegeschedulemahotkinmobil.ui.schedule.ScheduleViewModel

@Composable
fun FavoritesScreen(
    onGroupSelected: (GroupDto) -> Unit,
    viewModel: ScheduleViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val favorites by viewModel.favorites.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Избранные группы",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        if (favorites.isEmpty()) {
            Text(
                text = "Нет избранных групп.\nДобавьте группы в избранное из расписания",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favorites) { group ->
                    FavoriteGroupCard(
                        group = group,
                        onRemove = { viewModel.removeFavorite(group.groupId) },
                        onClick = { onGroupSelected(group) }
                    )
                }
            }
        }
    }
}

@Composable
fun FavoriteGroupCard(
    group: GroupDto,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClick() }
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = group.groupName,
                    style = MaterialTheme.typography.titleMedium
                )
                group.specialtyName?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                group.course?.let {
                    Text(
                        text = "$it курс",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            IconButton(onClick = onRemove) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Удалить из избранного"
                )
            }
        }
    }
}