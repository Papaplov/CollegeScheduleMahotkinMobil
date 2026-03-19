package com.example.collegeschedulemahotkinmobil.ui.schedule

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.collegeschedulemahotkinmobil.data.dto.ScheduleByDateDto
import com.example.collegeschedulemahotkinmobil.data.network.RetrofitInstance
import com.example.collegeschedulemahotkinmobil.utils.getWeekDateRange

@Composable
fun ScheduleScreen() {
    var schedule by remember { mutableStateOf<List<ScheduleByDateDto>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val (start, end) = getWeekDateRange()
        try {
            // groupId = 1 (как в примере Swagger), groupName = "ИС-12"
            schedule = RetrofitInstance.api.getSchedule(
                groupId = 1,
                groupName = "ИС-12",
                start = start,
                end = end
            )
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    when {
        loading -> CircularProgressIndicator()
        error != null -> Text("Ошибка: $error")
        else -> ScheduleList(schedule)
    }
}