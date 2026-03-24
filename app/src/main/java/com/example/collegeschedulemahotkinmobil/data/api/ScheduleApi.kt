package com.example.collegeschedulemahotkinmobil.data.api

import com.example.collegeschedulemahotkinmobil.data.dto.GroupDto
import com.example.collegeschedulemahotkinmobil.data.dto.ScheduleByDateDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ScheduleApi {
    @GET("api/schedule/groups")
    suspend fun getAllGroups(): List<GroupDto>

    @GET("api/schedule/group/{groupId}")
    suspend fun getSchedule(
        @Path("groupId") groupId: Int,          // числовой ID группы (например, 1)
        @Query("groupName") groupName: String,  // название группы (обязательный query-параметр)
        @Query("start") start: String,
        @Query("end") end: String
    ): List<ScheduleByDateDto>
}