@file:Suppress("FunctionName")

package com.yourcompany.android.studdy.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yourcompany.android.studdy.alarm.ExactAlarms
import com.yourcompany.android.studdy.alarm.InexactAlarms
import com.yourcompany.android.studdy.navigation.BottomNavItem

@Composable
fun HomeScreen(
    exactAlarms: ExactAlarms,
    inexactAlarms: InexactAlarms,
    onSchedulingAlarmNotAllowed: () -> Unit,
    showStopAlarmButton: Boolean,
    onStopAlarmClicked: () -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        topBar = { HomeScreenTopBar() },
        bottomBar = { HomeScreenBottomNavigation(navController) }
    ) { scaffoldPadding ->
        NavHost(navController, startDestination = BottomNavItem.Study.screenRoute) {
            composable(BottomNavItem.Study.screenRoute) {
                StudyTab(exactAlarms, onSchedulingAlarmNotAllowed)
            }
            composable(BottomNavItem.Rest.screenRoute) {
                RestTab(inexactAlarms)
            }
        }

        if (showStopAlarmButton) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
            ) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp),
                    elevation = 8.dp,
                    color = MaterialTheme.colors.secondary
                ) {
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .clickable { onStopAlarmClicked.invoke() }) {
                        Text(
                            text = "Stop Alarm",
                            modifier = Modifier.align(Alignment.Center),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.surface
                        )
                    }

                }
            }
        }
    }
}

@Composable
private fun HomeScreenTopBar() {
    TopAppBar(contentPadding = PaddingValues(start = 8.dp, end = 8.dp)) {
        Text(
            text = "Studdy App",
            fontSize = 24.sp
        )
    }
}

@Composable
private fun HomeScreenBottomNavigation(navController: NavController) {
    val navItems = listOf(
        BottomNavItem.Study,
        BottomNavItem.Rest
    )
    BottomNavigation(
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.primaryVariant
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        navItems.forEach { navItem ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(navItem.icon),
                        contentDescription = navItem.title
                    )
                },
                label = { Text(text = navItem.title, fontSize = 8.sp) },
                selected = navBackStackEntry?.destination?.route == navItem.screenRoute,
                selectedContentColor = MaterialTheme.colors.onPrimary,
                unselectedContentColor = MaterialTheme.colors.onPrimary.copy(alpha = 0.4f),
                onClick = {
                    navController.apply {
                        popBackStack()
                        navigate(navItem.screenRoute)
                    }
                }
            )
        }
    }
}