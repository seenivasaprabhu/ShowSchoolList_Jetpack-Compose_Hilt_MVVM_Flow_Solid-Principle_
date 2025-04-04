package com.example.a20250308_seenivasaprabhuramdoss_nycschools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint



@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val viewModel: SchoolViewModel = hiltViewModel()
            NavHost(navController, startDestination = "schoolList") {
                composable("schoolList") {
                    SchoolListScreen(viewModel) { dbn -> navController.navigate("schoolDetail/$dbn") }
                }
                composable("schoolDetail/{dbn}") { backStackEntry ->
                    backStackEntry.arguments?.getString("dbn")?.let { dbn ->
                        SchoolDetailScreen(viewModel, dbn)
                    }
                }
            }
        }
    }
}

@Composable
fun SchoolListScreen(viewModel: SchoolViewModel, onSchoolClick: (String) -> Unit) {
    val schools by viewModel.schoolList.collectAsState()
    LazyColumn {
        items(schools) { school ->
            Card(modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onSchoolClick(school.dbn) }) {
                Column(modifier = Modifier.padding(16.dp)) {
                    school.school_name?.let { Text(it, style = MaterialTheme.typography.titleLarge) }
                    school.overview_paragraph?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
                }
            }
        }
    }
}

@Composable
fun SchoolDetailScreen(viewModel: SchoolViewModel, dbn: String) {
    LaunchedEffect(dbn) { viewModel.getSchoolDetails(dbn) }
    val satScore by viewModel.satScore.collectAsState()
    satScore?.let {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Math Score: ${it.sat_math_avg_score}")
            Text("Reading Score: ${it.sat_critical_reading_avg_score}")
            Text("Writing Score: ${it.sat_writing_avg_score}")
        }
    }
}


