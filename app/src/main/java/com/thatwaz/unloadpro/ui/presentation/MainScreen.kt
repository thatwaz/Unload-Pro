package com.thatwaz.unloadpro.ui.presentation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavController
import com.thatwaz.unloadpro.viewmodel.MainViewModel


@Composable
fun MainScreen(viewModel: MainViewModel, navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    var cartonCountInput by remember { mutableStateOf("") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { showDialog = true }) {
            Text("Start New Unload")
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Enter Initial Carton Count") },
                text = {
                    TextField(
                        value = cartonCountInput,
                        onValueChange = { cartonCountInput = it },
                        label = { Text("Carton Count") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val count = cartonCountInput.toIntOrNull()
                            if (count != null) {
                                viewModel.updateInitialCartonCount(count)
                                Log.d("MainScreen", "Carton count input: $count")
                                showDialog = false
                                navController.navigate("displayCounter/$count")
                            } else {
                                Log.d("MainScreen", "Invalid input for carton count: $cartonCountInput")
                            }
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}







//@Composable
//fun MainScreen(viewModel: MainViewModel, navController: NavController) {
//    var showDialog by remember { mutableStateOf(false) }
//    var cartonCountInput by remember { mutableStateOf("") }
//
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Button(onClick = { showDialog = true }) {
//            Text("Start New Unload")
//        }
//
//        if (showDialog) {
//            AlertDialog(
//                onDismissRequest = { showDialog = false },
//                title = { Text("Enter Initial Carton Count") },
//                text = {
//                    TextField(
//                        value = cartonCountInput,
//                        onValueChange = { cartonCountInput = it },
//                        label = { Text("Carton Count") },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
//                    )
//                },
//                confirmButton = {
//                    Button(
//                        onClick = {
//                            val count = cartonCountInput.toIntOrNull()
//                            if (count != null) {
//                                viewModel.updateCartonCount(count)
//                                showDialog = false
//                                navController.navigate("displayCounter")
//                            } else {
//                                Log.d("MainScreen", "Invalid input for carton count: $cartonCountInput")
//                            }
//                        }
//                    ) {
//                        Text("Confirm")
//                    }
//                },
//                dismissButton = {
//                    Button(onClick = { showDialog = false }) {
//                        Text("Cancel")
//                    }
//                }
//            )
//        }
//    }
//}



//@Composable
//fun MainScreen(viewModel: MainViewModel, navController: NavController) {
//    var showDialog by remember { mutableStateOf(false) }
//    var cartonCountInput by remember { mutableStateOf("") }
//
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Button(onClick = { showDialog = true }) {
//            Text("Start New Unload")
//        }
//
//        if (showDialog) {
//            AlertDialog(
//                onDismissRequest = { showDialog = false },
//                title = { Text("Enter Initial Carton Count") },
//                text = {
//                    TextField(
//                        value = cartonCountInput,
//                        onValueChange = { cartonCountInput = it },
//                        label = { Text("Carton Count") }
//                    )
//                },
//                confirmButton = {
//                    Button(
//                        onClick = {
//                            val count = cartonCountInput.toIntOrNull()
//                            if (count != null) {
//                                viewModel.updateCartonCount(count)
//                                showDialog = false
//                                navController.navigate("displayCounter")
//                            }
//                        }
//                    ) {
//                        Text("Confirm")
//                    }
//                },
//                dismissButton = {
//                    Button(onClick = { showDialog = false }) {
//                        Text("Cancel")
//                    }
//                }
//            )
//        }
//    }
//}


//@Composable
//fun MainScreen(viewModel: MainViewModel, navController: NavController) {
//    var showDialog by remember { mutableStateOf(false) }
//
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Button(onClick = { showDialog = true }) {
//            Text("Start New Unload")
//        }
//
//        if (showDialog) {
//            AlertDialog(
//                onDismissRequest = { showDialog = false },
//                title = { Text("Enter Initial Carton Count") },
//                text = {
//                    TextField(
//                        value = viewModel.initialCartonCount.value.toString(),
//                        onValueChange = { viewModel.initialCartonCount.value },
//                        label = { Text("Carton Count") }
//                    )
//                },
//                confirmButton = {
//                    Button(
//                        onClick = {
//                            showDialog = false
//                            navController.navigate("displayCounter")  // Adjust this to the correct route
//                        }
//                    ) {
//                        Text("Confirm")
//                    }
//                },
//                dismissButton = {
//                    Button(onClick = { showDialog = false }) {
//                        Text("Cancel")
//                    }
//                }
//            )
//        }
//    }
//}






//@Composable
//fun MainScreen(viewModel: MainViewModel, navController: NavController) {
//    var showDialog by remember { mutableStateOf(false) }
//    val cartonCount by remember { viewModel.cartonCount }
//
//    if (showDialog) {
//        AlertDialog(
//            onDismissRequest = { showDialog = false },
//            title = { Text("Enter Initial Carton Count") },
//            text = {
//                TextField(
//                    value = cartonCount,
//                    onValueChange = { viewModel.updateCartonCount(it) },
//                    label = { Text("Carton Count") }
//                )
//            },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        showDialog = false
//                        // You might want to navigate or trigger another action here
//                        // navController.navigate("nextScreenRoute")
//                    }
//                ) {
//                    Text("Confirm")
//                }
//            },
//            dismissButton = {
//                Button(onClick = { showDialog = false }) {
//                    Text("Cancel")
//                }
//            }
//        )
//    }
//
//    Button(
//        onClick = {
//            showDialog = false
//            navController.navigate("nextScreenRoute")  // Make sure this route is correctly defined in your NavGraph
//        }
//    ) {
//        Text("Confirm")
//    }
//
//}


