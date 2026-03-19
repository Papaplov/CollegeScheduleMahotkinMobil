package com.example.collegeschedulemahotkinmobil.data.repository

import com.example.collegeschedulemahotkinmobil.data.api.ScheduleApi
import com.example.collegeschedulemahotkinmobil.data.dto.ScheduleByDateDto

class ScheduleRepository(private val api: ScheduleApi) {
    suspend fun loadSchedule(group: String): List<ScheduleByDateDto> {
        return api.getSchedule(
            groupName = group,
            start = "2026-03-16",
            end = "2026-04-15"
        )
    }
}
