package com.thatwaz.unloadpro

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.hilt.navigation.compose.hiltViewModel
import com.thatwaz.unloadpro.ui.presentation.UnloadScreen
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runCurrent
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

//@HiltAndroidTest
//@OptIn(ExperimentalCoroutinesApi::class)
//class UnloadScreenTest {
//
//    @get:Rule(order = 0)
//    val hiltRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 1)
//    val composeTestRule = createAndroidComposeRule<MainActivity>()
//
//    @Inject
//    lateinit var delayProvider: suspend (Long) -> Unit
//
//    @Before
//    fun init() {
//        hiltRule.inject()
//    }
//
//    @Test
//    fun testChronometerAfterOneHour() = runBlockingTest {
//        // Override the delay provider to advance time
//        delayProvider = { time ->
//            testScheduler.apply { advanceTimeBy(time); runCurrent() }
//        }
//
//        // Initialize the ViewModel with mock delay using Hilt injection
//        composeTestRule.setContent {
//            UnloadScreen(unloadViewModel = hiltViewModel(), initialCartonCount = 100)
//        }
//
//
//        // Click the Start Unload button to start the timer
//        composeTestRule.onNodeWithText("Start Unload").performClick()
//
//        // Advance time by 1 hour (3600 seconds)
//        testScheduler.apply { advanceTimeBy(3600L * 1000L); runCurrent() }
//        runCurrent() // Ensure all pending tasks are executed
//
//        // Verify the chronometer displays 60:00
//        composeTestRule.onNodeWithText("60:00").assertExists()
//    }
//}
