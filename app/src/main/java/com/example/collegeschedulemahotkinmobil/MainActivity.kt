package com.example.collegeschedulemahotkinmobil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.collegeschedulemahotkinmobil.ui.favorites.FavoritesScreen
import com.example.collegeschedulemahotkinmobil.ui.schedule.ScheduleScreen
import com.example.collegeschedulemahotkinmobil.ui.schedule.ScheduleViewModel
import com.example.collegeschedulemahotkinmobil.ui.theme.CollegeScheduleMahotkinMobilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CollegeScheduleMahotkinMobilTheme {
                CollegeScheduleMahotkinMobilApp()
            }
        }
    }
}

@PreviewScreenSizes
@Composable
fun CollegeScheduleMahotkinMobilApp() {
    var selectedTab by remember { mutableStateOf(0) }
    val context = LocalContext.current

    val scheduleViewModel: ScheduleViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return ScheduleViewModel(context) as T
            }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Нижняя навигация
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Расписание") },
                    label = { Text("Расписание") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Избранное") },
                    label = { Text("Избранное") },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AccountBox, contentDescription = "Профиль") },
                    label = { Text("Профиль") },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 }
                )
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> ScheduleScreen(
                modifier = Modifier.padding(innerPadding),
                viewModel = scheduleViewModel
            )
            1 -> FavoritesScreen(
                onGroupSelected = { group ->
                    val (start, end) = getWeekDateRange()
                    scheduleViewModel.loadSchedule(group, start, end)
                    selectedTab = 0
                },
                viewModel = scheduleViewModel,
                modifier = Modifier.padding(innerPadding)
            )
            2 -> ProfileScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Профиль студента",
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Здесь будет информация о студенте",
            modifier = Modifier.padding(top = 16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

fun getWeekDateRange(): Pair<String, String> {
    val today = java.time.LocalDate.now()
    val start = today
    val end = today.plusDays(6)
    val formatter = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

    return Pair(start.format(formatter), end.format(formatter))
}