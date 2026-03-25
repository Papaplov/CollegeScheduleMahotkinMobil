package com.example.collegeschedulemahotkinmobil.ui.schedule

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.collegeschedulemahotkinmobil.data.dto.GroupDto
import com.example.collegeschedulemahotkinmobil.data.dto.ScheduleByDateDto
import com.example.collegeschedulemahotkinmobil.data.network.RetrofitInstance
import com.example.collegeschedulemahotkinmobil.data.repository.ScheduleRepository
import com.example.collegeschedulemahotkinmobil.utils.FavoritesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScheduleViewModel(private val context: Context) : ViewModel() {
    private val repository = ScheduleRepository(RetrofitInstance.api)
    private val favoritesManager = FavoritesManager(context)

    private val _groups = MutableStateFlow<List<GroupDto>>(emptyList())
    val groups: StateFlow<List<GroupDto>> = _groups.asStateFlow()

    private val _schedule = MutableStateFlow<List<ScheduleByDateDto>>(emptyList())
    val schedule: StateFlow<List<ScheduleByDateDto>> = _schedule.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _currentGroup = MutableStateFlow<GroupDto?>(null)
    val currentGroup: StateFlow<GroupDto?> = _currentGroup.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _favorites = MutableStateFlow<List<GroupDto>>(emptyList())
    val favorites: StateFlow<List<GroupDto>> = _favorites.asStateFlow()

    init {
        loadFavorites()
    }

    fun loadGroups() {
        viewModelScope.launch {
            _loading.value = true
            try {
                _groups.value = repository.getAllGroups()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun loadSchedule(group: GroupDto, start: String, end: String) {
        viewModelScope.launch {
            _loading.value = true
            _currentGroup.value = group
            _isFavorite.value = favoritesManager.isFavorite(group.groupId)
            try {
                _schedule.value = repository.loadSchedule(group.groupId, group.groupName, start, end)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun toggleFavorite(group: GroupDto) {
        if (favoritesManager.isFavorite(group.groupId)) {
            favoritesManager.removeFavorite(group.groupId)
            _isFavorite.value = false
        } else {
            favoritesManager.addFavorite(group)
            _isFavorite.value = true
        }
        loadFavorites()
    }

    fun removeFavorite(groupId: Int) {
        favoritesManager.removeFavorite(groupId)
        loadFavorites()
        if (_currentGroup.value?.groupId == groupId) {
            _isFavorite.value = false
        }
    }

    private fun loadFavorites() {
        _favorites.value = favoritesManager.getFavorites()
    }
}