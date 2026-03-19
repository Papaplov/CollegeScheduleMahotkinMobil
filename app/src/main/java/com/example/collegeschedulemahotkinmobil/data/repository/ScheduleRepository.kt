package com.example.collegeschedulemahotkinmobil.data.repository

import com.example.collegeschedulemahotkinmobil.data.api.ScheduleApi
import com.example.collegeschedulemahotkinmobil.data.dto.ScheduleByDateDto

class ScheduleRepository(private val api: ScheduleApi) {
    suspend fun loadSchedule(groupId: Int, groupName: String, start: String, end: String): List<ScheduleByDateDto> {
        return api.getSchedule(groupId, groupName, start, end)
    }
}