package com.bloomteen.pcosassessor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bloomteen.pcosassessor.ui.screens.DashboardScreen
import com.bloomteen.pcosassessor.ui.screens.NewSelfCheckScreen
import com.bloomteen.pcosassessor.ui.theme.BloomTeenTheme
import com.bloomteen.pcosassessor.viewmodel.PcosViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BloomTeenTheme {
                val navController = rememberNavController()
                val pcosViewModel: PcosViewModel = viewModel()

                NavHost(
                    navController = navController,
                    startDestination = "dashboard"
                ) {
                    composable("dashboard") {
                        DashboardScreen(
                            viewModel = pcosViewModel,
                            onNavigateToNewSelfCheck = {
                                pcosViewModel.resetSymptomForm()
                                navController.navigate("new_self_check")
                            }
                        )
                    }

                    composable("new_self_check") {
                        NewSelfCheckScreen(
                            viewModel = pcosViewModel,
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
