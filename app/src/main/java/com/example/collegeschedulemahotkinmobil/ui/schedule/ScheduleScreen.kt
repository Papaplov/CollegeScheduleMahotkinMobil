package com.example.collegeschedulemahotkinmobil.ui.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.collegeschedulemahotkinmobil.data.dto.ScheduleByDateDto
import com.example.collegeschedulemahotkinmobil.ui.components.GroupSearchBar
import com.example.collegeschedulemahotkinmobil.utils.getWeekDateRange

@Composable
fun ScheduleScreen(
    modifier: Modifier = Modifier,
    viewModel: ScheduleViewModel = viewModel()
) {
    val groups by viewModel.groups.collectAsState()
    val schedule by viewModel.schedule.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val currentGroup by viewModel.currentGroup.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadGroups()
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Поиск групп (на всю ширину)
        GroupSearchBar(
            groups = groups,
            onGroupSelected = { group ->
                val (start, end) = getWeekDateRange()
                viewModel.loadSchedule(group, start, end)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // Кнопка добавления/удаления из избранного для текущей группы
        currentGroup?.let { group ->
            TextButton(
                onClick = { viewModel.toggleFavorite(group) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isFavorite) "★ Удалить из избранного" else "☆ Добавить в избранное",
                    fontSize = 16.sp,
                    color = if (isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Основной контент
        when {
            loading -> CircularProgressIndicator(
                modifier = Modifier.padding(16.dp)
            )
            error != null -> Text(
                text = "Ошибка: $error",
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error
            )
            else -> ScheduleListContent(schedule)
        }
    }
}

@Composable
fun ScheduleListContent(data: List<ScheduleByDateDto>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(data) { day ->
            // Заголовок дня
            Text(
                text = formatDate(day.lessonDate, day.weekday),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(8.dp)
            )

            if (day.lessons.isEmpty()) {
                Text(
                    text = "Информация отсутствует",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                day.lessons.forEach { lesson ->
                    Card(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            // Номер пары и время
                            Text(
                                text = "Пара ${lesson.lessonNumber} (${lesson.time})",
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.primary
                            )

                            androidx.compose.material3.Divider(
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            // Информация по группам
                            lesson.groupParts.forEach { (part, info) ->
                                if (info != null) {
                                    Column(
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    ) {
                                        // Название части группы на русском
                                        val partName = when (part.name) {
                                            "FULL" -> "Полная группа"
                                            "SUB1" -> "Подгруппа 1"
                                            "SUB2" -> "Подгруппа 2"
                                            else -> part.name
                                        }
                                        Text(
                                            text = partName,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.secondary
                                        )

                                        // Предмет
                                        Text(
                                            text = info.subject,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                                        )

                                        // Преподаватель
                                        Text(
                                            text = info.teacher,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        // Должность преподавателя
                                        if (info.teacherPosition.isNotEmpty()) {
                                            Text(
                                                text = info.teacherPosition,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }

                                        // Аудитория и корпус
                                        Text(
                                            text = "${info.building}, аудитория ${info.classroom}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        // Адрес
                                        Text(
                                            text = info.address,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    if (lesson.groupParts.keys.last() != part) {
                                        androidx.compose.material3.Divider(
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Функция для форматирования даты на русском
fun formatDate(dateStr: String, weekday: String): String {
    val weekdayRu = when (weekday.lowercase()) {
        "monday" -> "Понедельник"
        "tuesday" -> "Вторник"
        "wednesday" -> "Среда"
        "thursday" -> "Четверг"
        "friday" -> "Пятница"
        "saturday" -> "Суббота"
        "sunday" -> "Воскресенье"
        else -> weekday
    }

    // Парсим дату из строки
    val parts = dateStr.split("-")
    if (parts.size == 3) {
        val day = parts[2].toIntOrNull()
        val month = parts[1].toIntOrNull()
        val year = parts[0]

        val monthRu = when (month) {
            1 -> "января"
            2 -> "февраля"
            3 -> "марта"
            4 -> "апреля"
            5 -> "мая"
            6 -> "июня"
            7 -> "июля"
            8 -> "августа"
            9 -> "сентября"
            10 -> "октября"
            11 -> "ноября"
            12 -> "декабря"
            else -> ""
        }

        return if (day != null && monthRu.isNotEmpty()) {
            "$day $monthRu $year, $weekdayRu"
        } else {
            "$dateStr, $weekdayRu"
        }
    }

    return "$dateStr, $weekdayRu"
}