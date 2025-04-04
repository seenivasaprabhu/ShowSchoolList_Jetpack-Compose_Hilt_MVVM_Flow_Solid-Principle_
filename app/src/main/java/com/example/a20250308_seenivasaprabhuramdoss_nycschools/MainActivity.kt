package com.example.a20250308_seenivasaprabhuramdoss_nycschools

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
                        SchoolDetailScreen(viewModel, dbn,navController)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolDetailScreen(viewModel: SchoolViewModel, dbn: String, navController: NavController) {
    // Fetch school details and SAT score
    LaunchedEffect(dbn) { viewModel.getSchoolDetails(dbn) }

    // Collect data, loading and error states from ViewModel
    val satScore by viewModel.satScore.collectAsState()
    val isLoading by viewModel.loading.collectAsState()

    // Extract the school name or use "Unknown School" if it is null
    val schoolName = satScore?.school_name ?: ""

    Scaffold(
        topBar = {
            TopAppBar(
                title = {  },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
            ) {
                // Show loader while data is loading
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center)
                    ) {
                        CircularProgressIndicator() // Loader in the center of the screen
                    }
                } else {
                    // School name at the top of the detail page
                    Text(
                        schoolName,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                    )

                    // Check if SAT score is available
                    if (satScore != null) {
                        // Display SAT scores
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("DBN Number: ${satScore?.dbn}")
                            Text("Math Score: ${satScore?.sat_math_avg_score}")
                            Text("Reading Score: ${satScore?.sat_critical_reading_avg_score}")
                            Text("Writing Score: ${satScore?.sat_writing_avg_score}")
                        }
                    } else {
                        // Display message if no SAT score data
                        Text(
                            "No data available",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp).fillMaxSize()
                        )
                    }

                    // Optionally, display more details about the school
                    satScore?.let { details ->
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Overall Attenders: ${details.num_of_sat_test_takers}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun SchoolListScreen(viewModel: SchoolViewModel, onSchoolClick: (String) -> Unit) {
    // Collect school list and loading state from the ViewModel
    val schools by viewModel.schoolList.collectAsState()
    val isLoading by viewModel.loading.collectAsState() // Loading state

    Box(modifier = Modifier.fillMaxSize()) {
        // Show loader while data is loading
        if ( isLoading && schools.isEmpty() ){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator() // Loader in the center of the screen
            }
        } else {
            // If not loading, show the school list
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(6.dp) // Add padding to LazyColumn for better spacing
            ) {
                items(schools) { school ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp) // Space between each card
                            .clickable { onSchoolClick(school.dbn) },
                        elevation = CardDefaults.cardElevation(6.dp), // Use Material3 elevation
                        shape = MaterialTheme.shapes.medium, // Rounded corners
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Light background color for the card
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp) // Padding inside the card
                        ) {
                            // School Name
                            school.school_name?.let {
                                Text(
                                    it,
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(bottom = 8.dp),
                                )
                            }

                            // School Location
                            school.location?.takeIf { it.isNotEmpty() }?.let {
                                Text(
                                    "Location: $it",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 4.dp),
                                )
                            }

                            // School Phone Number
                            school.phone_number?.takeIf { it.isNotEmpty() }?.let {
                                Text(
                                    "Phone: $it",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 4.dp),
                                )
                            }

                            // School Email Address
                            school.school_email?.takeIf { it.isNotEmpty() }?.let {
                                Text(
                                    "Email: $it",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 4.dp),
                                )
                            }

                            // School Website (Clickable)
                            school.website?.takeIf { it.isNotEmpty() }?.let {
                                Text(
                                    text = "Website: $it",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary, // Highlight website in primary color
                                    modifier = Modifier
                                        .padding(bottom = 8.dp)
                                )
                            }

                            // Overview (Truncated to 2 lines)
                            school.overview_paragraph?.takeIf { it.isNotEmpty() }?.let {
                                Spacer(modifier = Modifier.height(16.dp)) // Extra space before overview
                                Text(
                                    it,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2, // Limit to 2 lines
                                    overflow = TextOverflow.Ellipsis, // Show ellipsis for truncated text
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


